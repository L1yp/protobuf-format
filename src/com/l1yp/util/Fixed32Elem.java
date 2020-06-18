package com.l1yp.util;

public class Fixed32Elem extends BaseElem {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getValueLE() {
        return Integer.reverseBytes(value);
    }

    @Override
    public String toString() {
        String hex = HexUtil.bin2hex(getBuffer(), getOff(), getLen());
        return String.format("%s {value: %d, valueLE: %d}", hex, value, Integer.reverseBytes(value));
    }
}
