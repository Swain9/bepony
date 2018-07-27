package com.catt.bepony.common.constant;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 09:06
 */
public interface Const {
    /**
     * 服务器地址
     */
//    public static final String SERVER = "192.168.31.213";
    public static final String SERVER = "172.168.10.7";
//    public static final String SERVER = "192.168.1.104";

    /**
     * 监听端口
     */
    public static final int PORT = 6789;

    /**
     * 心跳超时时间
     */
    public static final int TIMEOUT = 1000 * 20;

    public static final int HEARD_TIME_OUT = 5000;
}
