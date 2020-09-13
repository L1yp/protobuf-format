package com.l1yp.jce;

import java.util.Arrays;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class SimpleListElem extends BaseElem {

    public byte[] bytes;


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
