package com.l1yp.pb;

import com.l1yp.util.HexUtil;

public class LengthElem extends BaseElem {
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        String hex = HexUtil.bin2hex(getBuffer(), getOff(), getLen());
        return String.format("%s {length: %d}", hex, length);
    }
}
