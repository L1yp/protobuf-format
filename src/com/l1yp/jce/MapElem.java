package com.l1yp.jce;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class MapElem extends BaseElem {

    public Map<BaseElem, BaseElem> map = new LinkedHashMap<>();

    public void put(BaseElem k, BaseElem v){
        map.put(k, v);
    }

    @Override
    public boolean isLeaf(){
        return map.isEmpty();
    }

    @Override
    public String toString() {
        return "Map[" + head.tag + "]";
    }
}
