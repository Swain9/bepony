package com.catt.bepony.common.util;

import cn.hutool.core.util.NetUtil;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-24 15:57
 */
public class CheckUtil {

    public static boolean checkNum(String num) {
        try {
            Integer.valueOf(num);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 检查端口的正确性
     *
     * @param num 端口
     * @return boolean
     */
    public static boolean checkPort(String num) {
        int n;
        try {
            n = Integer.valueOf(num);
        } catch (Exception e) {
            return false;
        }

        //1024-65535
        if (n > 1024 && n < 65535) {
            return true;
        }
        return false;
    }

    public static boolean checkIpAndPort(String ipAndPort) {
        String[] split = ipAndPort.split(":");
        if (split.length != 2) {
            return false;
        }
        return NetUtil.isInnerIP(split[0]) && checkPort(split[1]);
    }
}
