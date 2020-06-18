package com.l1yp.util;

public class VarintElem extends BaseElem {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String hex = HexUtil.bin2hex(getBuffer(), getOff(), getLen());
        return String.format("%s {value: %d}", hex, value);
    }
}
