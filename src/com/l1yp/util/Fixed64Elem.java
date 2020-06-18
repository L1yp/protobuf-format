package com.l1yp.util;

public class Fixed64Elem extends BaseElem {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValueLE() {
        return Long.reverseBytes(value);
    }

    @Override
    public String toString() {
        String hex = HexUtil.bin2hex(getBuffer(), getOff(), getLen());
        return String.format("%s {value: %d, valueLE: %d}", hex, value, Long.reverseBytes(value));
    }
}
