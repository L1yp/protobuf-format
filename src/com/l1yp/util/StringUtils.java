package com.l1yp.util;

public class StringUtils {


    public static void main(String[] args) {
        String str = "哈1啊哈12哈哈1哈牛1逼324";
        System.out.println(subString(str, "1","2", false));//啊哈1
        System.out.println(subString(str, "1","2", true));//逼3

        System.out.println(leftString(str, "1", false));//哈
        System.out.println(leftString(str, "1", true));//哈1啊哈12哈哈1哈牛

        System.out.println(rightString(str, "1", false));//逼324
        System.out.println(rightString(str, "1", true));//啊哈12哈哈1哈牛1逼324
/*
        啊哈1
        逼3
        哈
        哈1啊哈12哈哈1哈牛
        逼324
        啊哈12哈哈1哈牛1逼324
                */
    }

    public static String subString(String src, String left, String right){
        return subString(src, left, right, false);
    }

    public static String subString(String src, String left, String right, boolean startForRight){
        if(startForRight){
            int leftIndex = src.lastIndexOf(left);
            if(leftIndex == -1) return "";
            int rightIndex = src.indexOf(right, leftIndex + left.length());
            if(rightIndex == -1) return "";
            return src.substring(leftIndex + left.length(), rightIndex);
        }else{
            int leftIndex = src.indexOf(left);
            if(leftIndex == -1) return "";
            int rightIndex = src.indexOf(right, leftIndex + left.length());
            if(rightIndex == -1) return "";
            return src.substring(leftIndex + left.length(), rightIndex);
        }
    }

    public static String leftString(String src, String left){
        return leftString(src, left, false);
    }

    public static String leftString(String src, String left, boolean startForRight){
        if(startForRight){
            int lastIndex = src.lastIndexOf(left);
            if(lastIndex == -1) return "";
            return src.substring(0, lastIndex);
        }else{
            int index = src.indexOf(left);
            if(index == -1) return "";
            return src.substring(0, index);
        }
    }

    public static String rightString(String src, String right){
        return rightString(src, right, false);
    }


    public static String rightString(String src, String right, boolean startForLeft){
        if(startForLeft){
            int index = src.indexOf(right);
            if(index == -1) return "";
            return src.substring(index + right.length(), src.length());
        }else{
            int lastIndex = src.lastIndexOf(right);
            if(lastIndex == -1) return "";
            return src.substring(lastIndex + right.length(), src.length());
        }
    }

}
