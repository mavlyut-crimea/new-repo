public class ParserText {
    public static String parseIL(String func3) {
        return switch (func3) {
            case "000" -> "lb";
            case "001" -> "lh";
            case "010" -> "lw";
            case "100" -> "lbu";
            case "101" -> "lhu";
            default -> throw new AssertionError("Unknown I-type instruction");
        };
    }

    public static String parseICsr(String func3) {
        return switch (func3) {
            case "001" -> "csrrw";
            case "010" -> "csrrs";
            case "011" -> "csrrc";
            case "101" -> "csrrwi";
            case "110" -> "csrrsi";
            case "111" -> "csrrci";
            default -> throw new AssertionError("Unknown I-type instruction");
        };
    }

    public static String parseJ() {
        return "jal";
    }

    public static String parseIArifm(String func3) {
        return switch (func3) {
            case "000" -> "addi";
            case "010" -> "slti";
            case "011" -> "sltiu";
            case "100" -> "xori";
            case "110" -> "ori";
            case "111" -> "andi";
            default -> throw new AssertionError("Unknown I-type instruction");
        };
    }

    public static String parseISr(String func3, String func7) {
        return switch (func3) {
            case "001" -> "slli";
            case "101" -> switch (func7) {
                case "0000000" -> "srli";
                case "0100000" -> "srai";
                default -> throw new AssertionError("Unknown I-type instruction");
            };
            default -> throw new AssertionError("Unknown I-type instruction");
        };
    }

    public static String parseS(String func3) {
        return switch (func3) {
            case "000" -> "sb";
            case "001" -> "sh";
            case "010" -> "sw";
            default -> throw new AssertionError("Unknown S-type instruction");
        };
    }

    public static String parseU(String opcode) {
        if (opcode.equals("0110111")) {
            return "lui";
        } else if (opcode.equals("0010111")) {
            return "auipc";
        }
        throw new AssertionError("Unknown U-type instruction");
    }

    public static String parseB(String func3) {
        return switch (func3) {
            case "000" -> "beq";
            case "001" -> "bne";
            case "100" -> "blt";
            case "101" -> "bge";
            case "110" -> "bltu";
            case "111" -> "bgeu";
            default -> throw new AssertionError("Unknown B-type instruction");
        };
    }

    public static String parseR(String func7, String func3) {
        if (check(func7, "0000000", func3, "000")) {
            return "add";
        } else if (check(func7, "0100000", func3, "000")) {
            return "sub";
        } else if (check(func7, "0000000", func3, "001")) {
            return "sll";
        } else if (check(func7, "0000000", func3, "010")) {
            return "slt";
        } else if (check(func7, "0000000", func3, "011")) {
            return "sltu";
        } else if (check(func7, "0000000", func3, "100")) {
            return "xor";
        } else if (check(func7, "0000000", func3, "101")) {
            return "srl";
        } else if (check(func7, "0100000", func3, "101")) {
            return "sra";
        } else if (check(func7, "0000000", func3, "110")) {
            return "or";
        } else if (check(func7, "0000000", func3, "111")) {
            return "and";
        } else if (check(func7, "0000001", func3, "000")) {
            return "mul";
        } else if (check(func7, "0000001", func3, "001")) {
            return "mulh";
        } else if (check(func7, "0000001", func3, "010")) {
            return "mulhsu";
        } else if (check(func7, "0000001", func3, "011")) {
            return "mulhu";
        } else if (check(func7, "0000001", func3, "100")) {
            return "div";
        } else if (check(func7, "0000001", func3, "101")) {
            return "divu";
        } else if (check(func7, "0000001", func3, "110")) {
            return "rem";
        } else if (check(func7, "0000001", func3, "111")) {
            return "remu";
        }
        throw new AssertionError("Unknown R-type instruction");
    }

    private static boolean check(String func7, String eqFunc7, String func3, String eqFunc3) {
        return func7.equals(eqFunc7) && func3.equals(eqFunc3);
    }
}
