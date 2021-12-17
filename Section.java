public class Section {
    private final int name, type, flags, addr, offset, size, link, info, addralign, entsize;

    public Section(int[] param) {
        name = param[0];
        type = param[1];
        flags = param[2];
        addr = param[3];
        offset = param[4];
        size = param[5];
        link = param[6];
        info = param[7];
        addralign = param[8];
        entsize = param[9];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(type).append(" ").append(flags).append(" ").append(addr).append(" ")
                .append(offset).append(" ").append(size).append(" ").append(link).append(" ").append(info).append(" ")
                .append(addralign).append(" ").append(entsize);
        return sb.toString();
    }

    public int getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getFlags() {
        return flags;
    }

    public int getAddr() {
        return addr;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    public int getLink() {
        return link;
    }

    public int getInfo() {
        return info;
    }

    public int getAddralign() {
        return addralign;
    }

    public int getEntsize() {
        return entsize;
    }
}
