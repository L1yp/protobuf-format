package com.l1yp.jce;

import com.l1yp.util.Packet;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class JceReader {

    private final Packet packet;

    public JceReader(Packet packet){
        this.packet = packet;
    }

    public long readNumber(int tag){
        HeadData head = packet.readHead();
        if (head.tag != tag){
            throw new IllegalArgumentException("readNumber expect tag: " + tag + ", but: " + head.tag);
        }
        if (head.type == HeadData.BYTE){
            return packet.read();
        }else if (head.type == HeadData.SHORT){
            return packet.readShort();
        }else if (head.type == HeadData.INT){
            return packet.readInt();
        }else if (head.type == HeadData.LONG){
            return packet.readLong();
        }else if (head.type == HeadData.ZERO_TAG){
            return 0;
        }else {
            throw new IllegalArgumentException("type mismatch, expect number, but " + head.type);
        }
    }

    public int readInt(int tag){
        HeadData head = packet.readHead();
        if (head.tag != tag){
            throw new IllegalArgumentException("readInt expect tag: " + tag + ", but: " + head.tag);
        }
        if (head.type == HeadData.BYTE){
            return packet.read();
        }else if (head.type == HeadData.SHORT){
            return packet.readShort();
        }else if (head.type == HeadData.INT){
            return packet.readInt();
        }else if (head.type == HeadData.ZERO_TAG){
            return 0;
        }else {
            throw new IllegalArgumentException("type mismatch, expect number, but " + head.type);
        }
    }

    public String readString(int tag){
        HeadData head = packet.readHead();
        if (head.tag != tag){
            throw new IllegalArgumentException("readString expect tag: " + tag + ", but: " + head.tag);
        }
        if (head.type == HeadData.STRING1){
            int len = packet.read() & 0xFF;
            byte[] bytes = packet.readBytes(len);
            return new String(bytes);
        }else if (head.type == HeadData.STRING4){
            int len = packet.readInt();
            byte[] bytes = packet.readBytes(len);
            return new String(bytes);
        }else {
            throw new IllegalArgumentException("readString expect type: STRING1, STRING4 , but: " + head.type);
        }

    }

    public byte[] readSimpleList(int tag){
        HeadData head = packet.readHead();
        if (head.tag != tag){
            throw new IllegalArgumentException("readSimpleList expect tag: " + tag + ", but: " + head.tag);
        }

        byte b = packet.read();
        if (b != 0){
            throw new IllegalArgumentException("readSimpleList expect head(2): 0, but: " + b);
        }

        int len = readInt(0);
        if (len < 0 || packet.remaining() < len){
            throw new IllegalArgumentException("readSimpleList len: " + len + ", remaining: " + packet.remaining());
        }

        byte[] bytes = packet.readBytes(len);
        return bytes;

    }

    public void skipField(){
        HeadData head = packet.readHead();
        switch (head.type){
            case HeadData.BYTE:{
                packet.skip(1);
            }
            case HeadData.SHORT:{
                packet.skip(2);
            }
            case HeadData.INT:{
                packet.skip(4);
            }
            case HeadData.FLOAT:{
                packet.skip(4);
            }
            case HeadData.LONG:{
                packet.skip(8);
            }
            case HeadData.DOUBLE:{
                packet.skip(8);
            }
            case HeadData.STRING1:{
                int len = packet.read() & 0xFF;
                packet.skip(len);
            }
            case HeadData.STRING4:{
                int len = packet.readInt();
                packet.skip(len);
            }
            case HeadData.SIMPLE_LIST:{
                byte b = packet.read();
                if (b != 0){
                    throw new IllegalArgumentException("skip SimpleList error with head: " + b);
                }
                int len = readInt(0);
                packet.skip(len);
            }
            case HeadData.MAP:{
                int len = readInt(0);
                for (int i = 0; i < len; i++) {
                    HeadData headData = packet.readHead();
                    if (headData.tag != 0){
                        throw new IllegalArgumentException("skipMap key tag expect 0, but " + headData.tag);
                    }
                    skipField(headData);
                    headData = packet.readHead();
                    if (headData.tag != 1){
                        throw new IllegalArgumentException("skipMap val tag expect 1, but " + headData.tag);
                    }
                    skipField(headData);
                }
            }
            case HeadData.STRUCT_BEGIN:{
                HeadData headData = null;
                do {
                    headData = packet.readHead();
                    skipField(headData);
                }while (head.type != HeadData.STRUCT_END);
            }
            case HeadData.STRUCT_END, HeadData.ZERO_TAG:{
                break;
            }
            case HeadData.LIST:{
                int size = readInt(0);
                for (int i = 0; i < size; i++) {
                    HeadData headData = packet.readHead();
                    skipField(headData);
                }
            }

        }
    }

    public void skipField(HeadData head){
        switch (head.type){
            case HeadData.BYTE:{
                packet.skip(1);
            }
            case HeadData.SHORT:{
                packet.skip(2);
            }
            case HeadData.INT:{
                packet.skip(4);
            }
            case HeadData.FLOAT:{
                packet.skip(4);
            }
            case HeadData.LONG:{
                packet.skip(8);
            }
            case HeadData.DOUBLE:{
                packet.skip(8);
            }
            case HeadData.STRING1:{
                int len = packet.read() & 0xFF;
                packet.skip(len);
            }
            case HeadData.STRING4:{
                int len = packet.readInt();
                packet.skip(len);
            }
            case HeadData.SIMPLE_LIST:{
                byte b = packet.read();
                if (b != 0){
                    throw new IllegalArgumentException("skip SimpleList error with head: " + b);
                }
                int len = readInt(0);
                packet.skip(len);
            }
            case HeadData.MAP:{
                int len = readInt(0);
                for (int i = 0; i < len; i++) {
                    skipField();
                    skipField();
                }
            }
            case HeadData.STRUCT_BEGIN:{
                HeadData headData = null;
                do {
                    headData = packet.readHead();
                    skipField(headData);
                }while (headData.type != HeadData.STRUCT_END);
            }
            case HeadData.STRUCT_END, HeadData.ZERO_TAG:{
                break;
            }
            case HeadData.LIST:{
                int size = readInt(0);
                for (int i = 0; i < size; i++) {
                    HeadData headData = packet.readHead();
                    skipField(headData);
                }
            }
        }
    }


}
