package com.catt.bepony.server.start;

import com.catt.bepony.common.constant.Const;
import com.catt.bepony.server.handler.HttpServerAioHandler;
import com.catt.bepony.server.listener.HttpServerAioListener;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.TcpConst;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 11:51
 */
@Slf4j
public class HttpServerStarter {
    /**
     * handler, 包括编码、解码、消息处理
     */
    public static ServerAioHandler aioHandler = new HttpServerAioHandler();

    /**
     * 事件监听器，可以为null
     */
    public static ServerAioListener aioListener = new HttpServerAioListener();

    /**
     * 一组连接共用的上下文对象
     */
    public static Map<Integer, ServerGroupContext> serverGroupContext = new HashMap<>();

    /**
     * tioServer对象
     */
    public static Map<Integer, TioServer> tioServer = new HashMap<>();

    /**
     * 有时候需要绑定ip，不需要则null
     */
    public static String serverIp = null;

    public static void start(Set<Integer> ports) {


        ports.forEach(port -> {
            ServerGroupContext context = new ServerGroupContext("tio-http-server-" + port, aioHandler, aioListener);
            context.setHeartbeatTimeout(Const.TIMEOUT);
            context.setShortConnection(true);
            context.setReadBufferSize(TcpConst.MAX_DATA_LENGTH);
            context.setUseQueueSend(false);
            context.setUseQueueDecode(false);
            serverGroupContext.put(port, context);

            tioServer.put(port, new TioServer(context));
            log.info("服务器HTTP服务已经初始化完毕，端口：{}。", port);
        });

        tioServer.forEach((port, server) -> {
            try {
                server.start(serverIp, port);
                log.info("服务器HTTP服务已经开始监听，端口：{}。", port);
            } catch (IOException e) {
                log.error("端口：{}，监听失败", port);
                throw new RuntimeException(e);
            }
        });

    }
}
