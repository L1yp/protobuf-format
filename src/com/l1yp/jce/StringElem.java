package com.l1yp.jce;

import com.l1yp.util.Packet;

import java.util.Arrays;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class StringElem extends BaseElem {

    public String val;
    public byte[] bytes;


    @Override
    public void read(Packet packet) {
        super.read(packet);
        if (head.type == HeadData.STRING1){
            byte len = packet.read();
            bytes = packet.readBytes(len);
            val = new String(bytes);
        }else if (head.type == HeadData.STRING4){
            int len = packet.readInt();
            if (len > packet.remaining()){
                throw new IllegalArgumentException(String.format("StringElem.read len: %d, remaining: %d", len, packet.remaining()));
            }
            bytes = packet.readBytes(len);
            val = new String(bytes);
        }
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringElem that = (StringElem) o;
        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
