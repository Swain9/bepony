package com.maolin.learn;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-26 17:37
 */
public class uuid {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String replace = UUID.randomUUID().toString().replace("-", "");
        System.out.println(replace.getBytes("UTF-8").length);

    }
}
