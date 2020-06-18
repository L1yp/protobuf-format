package com.l1yp.util;

public class TagElem extends BaseElem {
    private int key;
    private int tag;
    private int type;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String hex = HexUtil.bin2hex(getBuffer(), getOff(), getLen());
        return String.format("%s(%d)[%d, %s]", hex, key, tag, typeNames[type]);
    }
}
