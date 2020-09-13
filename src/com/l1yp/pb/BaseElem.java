package com.l1yp.pb;

import java.util.LinkedList;
import java.util.List;

public class BaseElem {

    protected static final String[] typeNames = new String[]{
            "Varint(0)",
            "Fixed64(1)",
            "LengthDelimited(2)",
            "ItemTag(3)",
            "ItemEndTag(4)",
            "Fixed32(5)",
    };

    private BaseElem parent;

    private byte[] buffer;

    private int off;

    private int len;

    private final List<BaseElem> children = new LinkedList<>();

    public void setParent(BaseElem parent) {
        this.parent = parent;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getOff() {
        return off;
    }

    public void setOff(int off) {
        this.off = off;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public List<BaseElem> getChildren() {
        return children;
    }

    public void add(BaseElem elem) {

        children.add(elem);
    }

    public void clear() {
        children.clear();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

}
