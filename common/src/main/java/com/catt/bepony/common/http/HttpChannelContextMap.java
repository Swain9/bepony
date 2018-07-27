package com.catt.bepony.common.http;

import org.tio.core.ChannelContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于每一次的HTTP请求，都声明一个UUID，将其绑定到ChannelContext上
 * 如果已经存在就获取，不存在就声明一个
 *
 * 该UUID 用来确保服务端和客户端的请求一致，避免数据串行。
 *
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-26 17:10
 */
public class HttpChannelContextMap {

    private HttpChannelContextMap() {

    }

    private static volatile Map<ChannelContext, String> map;

    public static Map<ChannelContext, String> getInstance() {
        if (map == null) {
            synchronized (HttpChannelContextMap.class) {
                if (map == null) {
                    map = new ConcurrentHashMap<>();
                }
            }
        }
        return map;
    }


    public static ChannelContext getByUUID(String uuid) {
        for (Map.Entry<ChannelContext, String> entry : map.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void put(ChannelContext channelContext, String uuid) {
        if (channelContext == null || uuid == null) {
            return;
        }
        map.put(channelContext, uuid);
    }

    public static String get(ChannelContext channelContext) {
        if (channelContext == null || channelContext.isWaitingClose || channelContext.isClosed) {
            return null;
        }
        return map.get(channelContext);
    }

    public static void remove(ChannelContext channelContext) {
        map.remove(channelContext);
    }
}
