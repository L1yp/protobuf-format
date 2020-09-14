package com.l1yp.jce;

import com.l1yp.util.HexUtil;

import java.util.Arrays;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class SimpleListElem extends BaseElem {

    public byte[] bytes;

    @Override
    public String toString() {
        return (bytes == null || bytes.length == 0) ? "" : HexUtil.bin2hex(bytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleListElem elem = (SimpleListElem) o;
        return Arrays.equals(bytes, elem.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
