package com.catt.bepony.server.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 17:25
 */
@Slf4j
public class HttpResponseQueue {

    private final static Map<Integer, ArrayBlockingQueue<byte[]>> queueMap = new HashMap<>();

    //public final static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(50);

    public static void init(Set<Integer> ports) {
        ports.forEach(port -> {
            queueMap.put(port, new ArrayBlockingQueue<>(50));
            log.info("服务器HTTP的队列已经初始化完毕，端口：{}。", port);
        });
    }

    public static void put(Integer port, byte[] bytes) {
        try {
            queueMap.get(port).put(bytes);
        } catch (Exception e) {
            log.error("存放{}端口数据失败：", port, e);
        }
    }

    public static byte[] take(Integer port) {
        try {
            return queueMap.get(port).take();
        } catch (Exception e) {
            log.error("获取{}端口数据失败：", port, e);
            return null;
        }
    }

}
