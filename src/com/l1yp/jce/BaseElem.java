package com.l1yp.jce;

import com.l1yp.util.Packet;

import java.util.List;

/**
 * @Author Lyp6
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public abstract class BaseElem {
    public HeadData head;

    public BaseElem parent;

    public List<BaseElem> children;

    public void read(Packet packet){
        head = packet.readHead();
    }

    public boolean isLeaf(){
        return children == null || children.isEmpty();
    }

    public static BaseElem getElem(int type){
        return switch (type){
            case HeadData.BYTE -> new ByteElem();
            case HeadData.SHORT -> new ShortElem();
            case HeadData.INT -> new IntElem();
            case HeadData.LONG -> new LongElem();
            case HeadData.FLOAT -> new FloatElem();
            case HeadData.DOUBLE -> new DoubleElem();
            case HeadData.STRING4, HeadData.STRING1 -> new StringElem();
            case HeadData.ZERO_TAG -> new ZeroTagElem();

            case HeadData.MAP -> new MapElem();
            case HeadData.LIST -> new ListElem();
            case HeadData.SIMPLE_LIST -> new SimpleListElem();
            default -> null;
        };
    }

}
