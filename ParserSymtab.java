public class ParserSymtab {
    public static String getIndex(int other) {
        return switch (other) {
            case 0 -> "UND";
            case 0xfff1 -> "ABS";
            case 0xff00 -> "LOPROC";
            case 0xff1f -> "HIPROC";
            case 0xfff2 -> "COMMON";
            case 0xffff -> "XINDEX";
            case 0xff3f -> "HIOS";
            case 0xff10 -> "LOOS";
            case 0xff01 -> "AFTER";
            default -> other + "";
        };
    }

    public static String getVis(int other) {
        other = (other % 4) / 2;
        return switch (other) {
            case 0 -> "DEFAULT";
            case 1 -> "HIDDEN";
            default -> throw new AssertionError("Unknown vis");
        };
    }

    public static String getBind(int shndx) {
        return switch (shndx) {
            case 0 -> "LOCAL";
            case 1 -> "GLOBAL";
            case 2 -> "WEAK";
            case 13 -> "LOPROC";
            case 15 -> "HIPROC";
            default -> throw new AssertionError("Unknown bind");
        };
    }

    public static String getType(int info) {
        return switch (info) {
            case 0 -> "NOTYPE";
            case 1 -> "OBJECT";
            case 2 -> "FUNC";
            case 3 -> "SECTION";
            case 4 -> "FILE";
            case 13 -> "LOPROC";
            case 15 -> "HIPROC";
            default -> throw new AssertionError("Unknown type");
        };
    }
}
