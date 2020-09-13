package com.l1yp.pb;

import com.l1yp.util.CharacterUtil;
import com.l1yp.util.Packet;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author Lyp
 * @Date   2020/5/8
 * @Email  l1yp@qq.com
 */
public class PbParser {

    public RootElem parse(byte[] src){
        if (src == null || src.length == 0){
            return null;
        }

        return parse(src, 0, src.length);
    }

    public RootElem parse(byte[] src, int off, int len){
        if (src == null || len == 0){
            return null;
        }
        RootElem root = new RootElem();
        root.setKey("root");
        root.setBuffer(src);
        root.setOff(off);
        root.setLen(len);

        parse0(root);

        return root;
    }


    private void parse0(BaseElem root){

        LinkedList<BaseElem> folder = new LinkedList<>();
        folder.push(root);
        while (!folder.isEmpty()){
            BaseElem father = folder.removeLast();
            Packet packet = new Packet(father.getBuffer(), father.getOff(), father.getLen());
            boolean succ = true;
            while (packet.remaining() > 0){
                int pos = packet.getReaderPos();
                int key = packet.readRawVarint32();
                if (key == 0){
                    break;
                }
                int type = WireFormatMicro.getTagWireType(key);

                TagElem tagElem = new TagElem();
                tagElem.setTag(WireFormatMicro.getTagFieldNumber(key));
                tagElem.setType(type);
                tagElem.setKey(key);

                tagElem.setBuffer(father.getBuffer());
                tagElem.setOff(pos);
                tagElem.setLen(packet.getReaderPos() - pos);
                tagElem.setParent(tagElem);

                try {

                    BaseElem valueElem = TypeHandler.handle(type, tagElem, packet);
                    if (valueElem == null){
                        succ = false;
                        break;
                    }

                    if (type == WireFormatMicro.WIRETYPE_LENGTH_DELIMITED
                        && valueElem.getLen() > 1
                        && !CharacterUtil.isPrintable(valueElem.getBuffer(),
                            valueElem.getOff(), valueElem.getLen(), StandardCharsets.UTF_8)){

                        Packet pack = new Packet(father.getBuffer(), valueElem.getOff(), valueElem.getLen());
                        if (pack.peekVarintSize() != valueElem.getLen()){
                            folder.addFirst(valueElem);
                        }

                    }
                    father.add(tagElem);
                } catch (InvalidProtocolBufferMicroException ignored){
                    succ = false;
                    break;
                }
            }
            if (!succ){
                father.clear();
            }
        }

    }

    private static final Map<Integer, TypeHandler> map = new HashMap<>();

    static {
        map.put(0, TypeStrategy.VARINT);
        map.put(1, TypeStrategy.FIXED64);
        map.put(2, TypeStrategy.LENGTH_DELIMITED);
        map.put(5, TypeStrategy.FIXED32);
    }

    private interface TypeHandler {

        BaseElem handle(BaseElem elem, Packet packet);

        static BaseElem handle(int type, BaseElem elem, Packet packet){
            if (!map.containsKey(type)){
                return null;
            }
            return map.get(type).handle(elem, packet);
        }

    }

    private enum TypeStrategy implements TypeHandler{

        VARINT(){
            @Override
            public BaseElem handle(BaseElem elem, Packet packet) {
                int size = packet.peekVarintSize();
                if (size > 10 || size == 0){
                    elem.clear();
                    throw new InvalidProtocolBufferMicroException(
                            String.format("varint size error：off: %d, len: %d",
                                    packet.getReaderPos(), packet.getLimit()));
                }

                int pos = packet.getReaderPos();
                long value = packet.readRawVarint64();

                VarintElem varintElem = new VarintElem();

                varintElem.setValue(value);
                varintElem.setBuffer(packet.getBuf());
                varintElem.setOff(pos);
                varintElem.setLen(packet.getReaderPos() - pos);

                varintElem.setParent(elem);

                elem.add(varintElem);

                return varintElem;
            }
        },

        FIXED64(){
            @Override
            public BaseElem handle(BaseElem elem, Packet packet) {
                if (packet.remaining() < 8){
                    elem.clear();
                    throw new InvalidProtocolBufferMicroException(
                            String.format("fixed64 size error：expect: 8, remaining: %d", packet.remaining()));
                }

                int pos = packet.getReaderPos();
                long value = packet.readLong();


                Fixed64Elem fixed64Elem = new Fixed64Elem();

                fixed64Elem.setValue(value);
                fixed64Elem.setBuffer(packet.getBuf());
                fixed64Elem.setOff(pos);
                fixed64Elem.setLen(8);

                fixed64Elem.setParent(elem);

                elem.add(fixed64Elem);

                return fixed64Elem;
            }
        },

        FIXED32(){
            @Override
            public BaseElem handle(BaseElem elem, Packet packet) {
                if (packet.remaining() < 4){
                    elem.clear();
                    throw new InvalidProtocolBufferMicroException(
                            String.format("fixed32 size error：expect: 4, remaining: %d", packet.remaining()));
                }

                int pos = packet.getReaderPos();
                int value = packet.readInt();

                Fixed32Elem fixed32Elem = new Fixed32Elem();

                fixed32Elem.setValue(value);
                fixed32Elem.setBuffer(packet.getBuf());
                fixed32Elem.setOff(pos);
                fixed32Elem.setLen(4);

                fixed32Elem.setParent(elem);

                elem.add(fixed32Elem);

                return fixed32Elem;
            }
        },

        LENGTH_DELIMITED(){
            @Override
            public BaseElem handle(BaseElem elem, Packet packet) {
                int varintSize = packet.peekVarintSize();
                if (varintSize > 6){
                    elem.clear();
                    throw new InvalidProtocolBufferMicroException(
                            String.format("LengthElem varint size error：expect: <= 6, act: %d", varintSize));
                }

                int pos = packet.getReaderPos();
                int length = packet.readRawVarint32();

                if (packet.remaining() < length){
                    elem.clear();
                    throw new InvalidProtocolBufferMicroException(
                            String.format("BytesElem size error：expect: <= %d, remaining: %d",
                                    length, packet.remaining()));
                }

                LengthElem lengthElem = new LengthElem();
                lengthElem.setLength(length);
                lengthElem.setBuffer(packet.getBuf());
                lengthElem.setOff(pos);
                lengthElem.setLen(packet.getReaderPos() - pos);

                lengthElem.setParent(elem);

                elem.add(lengthElem);

                if (length == 0){
                    return lengthElem;
                }


                pos = packet.getReaderPos();
                BytesElem bytesElem = new BytesElem();
                bytesElem.setBuffer(packet.getBuf());
                bytesElem.setOff(pos);
                bytesElem.setLen(length);

                bytesElem.setParent(elem);

                lengthElem.add(bytesElem);


                packet.setReaderPos(pos + length);

                return bytesElem;
            }
        },
        ;
    }

}
