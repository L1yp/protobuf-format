package com.l1yp.jce;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class ListElem extends BaseElem {

    public List<BaseElem> list = new ArrayList<>();

    public void add(BaseElem e){
        list.add(e);
    }


    @Override
    public boolean isLeaf() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return "ListElem[" + head.tag + "] length: " + list.size();
    }
}
