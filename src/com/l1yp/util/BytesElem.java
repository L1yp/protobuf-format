package com.l1yp.util;

import java.nio.charset.StandardCharsets;

public class BytesElem extends BaseElem {

    Boolean printable = null;

    public Boolean getPrintable() {
        return printable;
    }

    @Override
    public String toString() {
        if (isLeaf() && CharacterUtil.isPrintable(getBuffer(), getOff(), getLen(), StandardCharsets.UTF_8)) {
            printable = Boolean.TRUE;
            return new String(getBuffer(), getOff(), getLen(), StandardCharsets.UTF_8);
        } else {
            printable = Boolean.FALSE;
            return HexUtil.bin2hex(getBuffer(), getOff(), getLen());
        }
    }
}
