package com.l1yp.jce;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class ListElem extends BaseElem {

    private List<BaseElem> list = new ArrayList<>();

    public void add(BaseElem e){
        list.add(e);
    }



}
