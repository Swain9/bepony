package com.catt.bepony.server.start;

import com.catt.bepony.common.http.HttpChannelContextMap;
import com.catt.bepony.common.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * main方法
 *
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-24 15:48
 */
@Slf4j
public class Main {


    public static void main(String[] args) throws IOException {
        //String[] newArgs = ArrayUtil.merge(args, "8180");
        init(args);
    }


    private static void init(String[] args) throws IOException {

        int length = args.length;
        Set<Integer> ports = new HashSet<>();

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (CheckUtil.checkPort(args[i])) {
                    ports.add(Integer.valueOf(args[i]));
                    continue;
                }
                log.error("请输入正确的端口号");
                return;
            }
        } else {
            ports.add(8080);
        }

        HttpChannelContextMap.getInstance();
        HttpServerStarter.start(ports);
        SocketServerStarter.start();
    }
}
