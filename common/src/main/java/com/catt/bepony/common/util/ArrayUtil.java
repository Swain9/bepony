package com.catt.bepony.common.util;

import java.util.Arrays;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-24 16:31
 */
public class ArrayUtil {

    /**
     * 合并两个数组
     *
     * @param t1  数组1
     * @param t2  数组2
     * @param <T> 泛型类型
     * @return 泛型类型数组
     */
    public static <T> T[] merge(T[] t1, T... t2) {
        //建立t3数组，并将t1添加进去
        T[] t3 = Arrays.copyOf(t1, t1.length + t2.length);
        //将t2数组添加到已经含有t1数组的t3数组中去
        System.arraycopy(t2, 0, t3, t1.length, t2.length);
        return t3;
    }

}
