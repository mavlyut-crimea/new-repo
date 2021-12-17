import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static int[] bytes;
    private static FileWriter out;
    private static DataInputStream in;
    private static Map<String, Section> sections;

    public static void main(String[] args) {
        try {
            in = new DataInputStream(new FileInputStream(args[0]));
            out = new FileWriter(args[1], StandardCharsets.UTF_8);
            enterData();
            parseHeader();
            parseText();
            parseSymtab();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error after open files: " + e.getMessage());
        }
    }

    private static void enterData() throws IOException {
        byte[] bytes1 = in.readAllBytes();
        bytes = new int[bytes1.length];
        for (int i = 0; i < bytes1.length; i++) {
            bytes[i] = ((bytes1[i] < 0) ? 256 : 0) + bytes1[i];
        }
    }

    private static void parseHeader() {
        int e_shoff = cnt(32, 4);
        int e_shentsiz = cnt(46, 2);
        int e_shnum = cnt(48, 2);
        int e_shstrndx = cnt(50, 2);
        int go = e_shoff + e_shentsiz * e_shstrndx;
        int sh_offset12 = cnt(go + 16, 4);
        sections = new HashMap<>();
        for (int j = 0, tmp = e_shoff; j < e_shnum; j++, tmp += 40) {
            String name = getName(sh_offset12 + cnt(tmp, 4));
            int[] param = new int[10];
            for (int r = 0; r < 10; r++) {
                param[r] = cnt(tmp + 4 * r, 4);
            }
            if (name.length() == 0) {
                name = ".";
            }
            sections.put(name.substring(1), new Section(param));
        }
    }

    private static void parseText() throws IOException {
        out.write(".text\n");

        int ind = sections.get("text").getOffset();
        int num = sections.get("text").getSize() / 4;
        int addr = sections.get("text").getAddr();
        for (int i = 0; i < num; i++) {
            String[] ans = getAns(ind + 4 * i);
            try {
                out.write(String.format("%08x", addr + 4 * i));
                for (int j = 0; j < 5; j++) {
                    if (ans[j].length() > 0) {
                        out.write(String.format(" %s", ans[j]));
                        if (j == 2 || j == 3) {
                            out.write(", ");
                        }
                    }
                }
                out.write("\n");
            } catch (ArrayIndexOutOfBoundsException e) {
                //System.out.println(e.getMessage());
            }
        }
    }

    private static void parseSymtab() throws IOException {
        out.write("\n.symtab\n");
        int ind = sections.get("symtab").getOffset();
        int num = sections.get("symtab").getSize() / 16;
        out.write(String.format("%s %-15s %7s %-8s %-8s %-8s %6s %s\n",
                "Symbol", "Value", "Size", "Type", "Bind", "Vis", "Index", "Name"));
        for (int i = 0; i < num; i++) {
            int name = cnt(ind + i * 16, 4);
            int value = cnt(ind + i * 16 + 4, 4);
            int size = cnt(ind + i * 16 + 8, 4);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                sb.append(new StringBuilder(decToBin(bytes[ind + i * 16 + 12 + j])).reverse());
            }
            int info = Integer.parseInt(new StringBuilder(sb.substring(0, 4)).reverse().toString(), 2);
            int shndx = Integer.parseInt(new StringBuilder(sb.substring(4, 8)).reverse().toString(), 2);
            int other = Integer.parseInt(new StringBuilder(sb.substring(8)).reverse().toString(), 2);
            out.write(String.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s\n",
                    i, value, size, ParserSymtab.getType(info), ParserSymtab.getBind(shndx), ParserSymtab.getVis(other),
                    ParserSymtab.getIndex(other >> 8), getName(name + sections.get("strtab").getOffset())));
        }
    }

    private static int cnt(int left, int num) {
        int ans = 0;
        for (int i = num - 1; i >= 0; i--) {
            ans = ans * 256 + bytes[left + i];
        }
        return ans;
    }

    private static String getName(int left) {
        StringBuilder sb = new StringBuilder();
        while (bytes[left] != 0) {
            sb.append((char) bytes[left++]);
        }
        return sb.toString();
    }

    private static String[] getAns(int left) {
        if (decToBin(bytes[left]).endsWith("11")) {
            return parseRiscV(left);
        }
        return parseRcv(left);
    }

    private static String[] parseRcv(int left) {
        throw new AssertionError("This function isn't work");
    }

    private static String[] parseRiscV(int left) {
        StringBuilder sb = new StringBuilder()
                .append(decToBin(bytes[left + 3])).append(decToBin(bytes[left + 2]))
                .append(decToBin(bytes[left + 1])).append(decToBin(bytes[left]));
        String opcode = sb.substring(25, 32);
        String rd = sb.substring(20, 25);
        String func3 = sb.substring(17, 20);
        String rs1 = sb.substring(12, 17);
        String rs2 = sb.substring(7, 12);
        String func7 = sb.substring(0, 7);

        //System.out.println(opcode + " " + func3 + " " + func7);

        if (opcode.equals("0110011")) {
            return new String[]{"", ParserText.parseR(func7, func3), reg(rd), reg(rs1), reg(rs2)};
        } else if (opcode.equals("0110111") || opcode.equals("0010111")) {
            int imm_u = Integer.parseInt(sb.substring(0, 20), 2);
            return new String[]{"", ParserText.parseU(opcode), reg(rd), imm_u + ""};
        } else if (sb.toString().equals("00000000000100000000000001110011")) {
            return new String[]{"", "EBREAK", "", "", ""};
        } else if (sb.toString().equals("00000000000000000000000001110011")) {
            return new String[]{"", "ECALL", "", "", ""};
        } else if (opcode.equals("1100011")) {
            int imm_b = (int) Long.parseLong(
                    (sb.charAt(0) + "").repeat(20) + sb.charAt(24) + sb.substring(1, 7) + sb.substring(20, 24) + "0",2
            );
            return new String[]{"", ParserText.parseB(func3), reg(rs1), reg(rs2), imm_b + ""};
        } else if (opcode.equals("0100011")) {
            int imm_s = (int) Long.parseLong(
                    (sb.charAt(0) + "").repeat(20) + sb.substring(0, 7) + sb.substring(20, 25), 2
            );
            return new String[]{"", ParserText.parseS(func3), "", reg(rs2), imm_s + ""};
        } else if (opcode.equals("0010011")) {
            try {
                int imm_i = (int) Long.parseLong((sb.charAt(0) + "").repeat(20) + sb.substring(0, 12), 2);
                return new String[]{"", ParserText.parseIArifm(func3), reg(rd), reg(rs1), imm_i + ""};
            } catch (AssertionError ignored) {
                return new String[]{"", ParserText.parseISr(func3, func7), reg(rd), reg(rs1), sb.substring(7, 11)};
            }
        } else if (opcode.equals("0000011")) {
            int imm_i = (int) Long.parseLong((sb.charAt(0) + "").repeat(20) + sb.substring(0, 12), 2);
            return new String[]{"", ParserText.parseIL(func3), "", reg(rd), imm_i + ""};
        } else if (opcode.equals("1101111")) {
            int imm_j = (int) Long.parseLong(
                    (sb.charAt(0) + "").repeat(12) + sb.substring(12, 20)
                            + (sb.charAt(20) + "") + sb.substring(1, 11) + "0", 2
            );
            return new String[]{"", ParserText.parseJ(), reg(rd), "", imm_j + ""};
        } else if (opcode.equals("1100111")) {
            int imm_i = (int) Long.parseLong((sb.charAt(0) + "").repeat(20) + sb.substring(0, 12), 2);
            return new String[]{"", "jalr", reg(rd), "", imm_i + ""};
        } else if (opcode.equals("1110011")) {
            return new String[]{"", ParserText.parseICsr(func3), reg(rd), reg(sb.substring(0, 12)), reg(rs1)};
        } else {
            throw new AssertionError("Instruction not found: " + sb);
        }
    }

    private static String reg(String a) {
        int reg = Integer.parseInt(a, 2);
        if (reg == 0) {
            return "zero";
        } else if (reg == 1) {
            return "ra";
        } else if (reg == 2) {
            return "sp";
        } else if (reg == 3) {
            return "gp";
        } else if (reg == 4) {
            return "tp";
        } else if (reg == 5) {
            return "t0";
        } else if (6 <= reg && reg <= 7) {
            String s = "t";
            s += (char) (reg - 5 + '0');
            return s;
        } else if (reg == 8) {
            return "s0";
        } else if (reg == 9) {
            return "s1";
        } else if (10 <= reg && reg <= 11) {
            String s = "a";
            s += (char) (reg - 10 + '0');
            return s;
        } else if (12 <= reg && reg <= 17) {
            String s = "a";
            s += (char) (reg - 10 + '0');
            return s;
        } else if (18 <= reg && reg <= 27) {
            String s = "s";
            s += (char) (reg - 16 + '0');
            return s;
        } else if (28 <= reg && reg <= 31) {
            String s = "t";
            s += (char) (reg - 25 + '0');
            return s;
        }
        return null;
    }

    private static String decToBin(int b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(b % 2);
            b /= 2;
        }
        return sb.reverse().toString();
    }
}
