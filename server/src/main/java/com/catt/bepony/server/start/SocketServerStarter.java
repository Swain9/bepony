package com.catt.bepony.server.start;

import com.catt.bepony.common.constant.Const;
import com.catt.bepony.server.handler.SocketServerAioHandler;
import lombok.extern.slf4j.Slf4j;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 11:51
 */
@Slf4j
public class SocketServerStarter {
    /**
     * handler, 包括编码、解码、消息处理
     */
    public static ServerAioHandler aioHandler = new SocketServerAioHandler();

    /**
     * 事件监听器，可以为null
     */
    public static ServerAioListener aioListener = null;

    /**
     * 一组连接共用的上下文对象
     */
    public static ServerGroupContext serverGroupContext = new ServerGroupContext("tio-server", aioHandler, aioListener);

    /**
     * tioServer对象
     */
    public static TioServer tioServer = new TioServer(serverGroupContext);

    /**
     * 有时候需要绑定ip，不需要则null
     */
    public static String serverIp = null;

    /**
     * 监听的端口
     */
    public static int serverPort = Const.PORT;

    public static void start() throws IOException {
        serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        tioServer.start(serverIp, serverPort);
        log.info("服务器端服务已开启，端口：{}。", serverPort);
    }
}
