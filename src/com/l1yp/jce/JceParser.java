package com.l1yp.jce;

import com.l1yp.util.HexUtil;
import com.l1yp.util.Packet;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class JceParser {

    public ObjElem parse(byte[] bytes, int pos, int len){
        Packet packet = new Packet(bytes, pos, len);
        ObjElem root = new ObjElem();
        while (packet.remaining() > 0){
            BaseElem elem = readObj(packet);
            root.children.add(elem);
        }
        return root;
    }

    public RootElem parse(byte[] bytes){
        Packet packet = new Packet(bytes);
        RootElem root = new RootElem();
        while (packet.remaining() > 0){
            BaseElem elem = readObj(packet);
            root.children.add(elem);
        }
        return root;
    }

    private BaseElem readObj(Packet packet){
        HeadData head = packet.peekHead();
        if (head == null){
            throw new IllegalArgumentException("readObj by readHead return null");
        }
        switch (head.type){
            case HeadData.BYTE:
            case HeadData.SHORT:
            case HeadData.INT:
            case HeadData.LONG:
            case HeadData.FLOAT:
            case HeadData.DOUBLE:
            case HeadData.STRING1:
            case HeadData.STRING4:
            case HeadData.ZERO_TAG:{
                BaseElem elem = BaseElem.getElem(head.type);
                elem.read(packet);
                return elem;
            }

            case HeadData.MAP:{
                MapElem elem = new MapElem();
                elem.head = packet.readHead();
                int size = packet.readJceInt();
                for (int i = 0; i < size; i++) {
                    BaseElem keyElem = readObj(packet);
                    BaseElem valElem = readObj(packet);
                    elem.put(keyElem, valElem);
                }
                return elem;
            }
            case HeadData.LIST:{
                ListElem elem = new ListElem();
                elem.head = packet.readHead();
                int size = packet.readJceInt();
                for (int i = 0; i < size; i++) {
                    BaseElem item = readObj(packet);
                    elem.add(item);
                }
                return elem;

            }
            case HeadData.SIMPLE_LIST:{
                SimpleListElem elem = new SimpleListElem();
                elem.head = packet.readHead();
                byte b = packet.read();
                if (b != 0){
                    throw new IllegalArgumentException("read simple list expect head is 0, but " + b);
                }
                int size = packet.readJceInt();
                int oldSize = size;
                byte[] buf = packet.getBuf();
                int readerPos = packet.getReaderPos();
                try{
                    if (buf[readerPos] == 120){
                        byte[] result = HexUtil.uncompress(buf, readerPos, size);
                        if (result.length > 0){
                            buf = result;
                            readerPos = 0;
                            size = buf.length;
                        }
                    }
                    ObjElem rootElem = parse(buf, readerPos, size);
                    packet.skip(oldSize);
                    return rootElem;
                }catch (Exception e){
                    elem.bytes = packet.readBytes(oldSize);
                    return elem;
                }
            }
            case HeadData.STRUCT_BEGIN:{
                ObjElem elem = new ObjElem();
                elem.head = packet.readHead();
                HeadData headData = null;
                do {
                    headData = packet.peekHead();
                    BaseElem e = readObj(packet);
                    if (headData.type != HeadData.STRUCT_END){
                        elem.children.add(e);
                    }
                }while (headData.type != HeadData.STRUCT_END);
                return elem;
            }
            case HeadData.STRUCT_END: {
                packet.skipHead();
                return null;
            }
            default: throw new IllegalArgumentException("type mismatch with " + head.type);
        }

    }


}
