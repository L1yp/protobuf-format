package com.l1yp.jce;

import com.l1yp.util.Packet;

import java.util.Objects;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class ShortElem extends BaseElem {

    public short val;

    @Override
    public void read(Packet packet) {
        super.read(packet);
        val = packet.readShort();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortElem shortElem = (ShortElem) o;
        return val == shortElem.val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public String toString() {
        return val + "";
    }
}
