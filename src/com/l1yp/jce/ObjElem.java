package com.l1yp.jce;

import java.util.ArrayList;

/**
 * @Author Lyp
 * @Date 2020/9/13
 * @Email l1yp@qq.com
 */
public class ObjElem extends BaseElem {

    public ObjElem(){
        children = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Object";
    }
}
