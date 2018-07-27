package com.catt.bepony.client.start;

import com.catt.bepony.client.handler.SocketClientAioHandler;
import com.catt.bepony.common.constant.Const;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 17:49
 */
public class SocketClientStarter {
    /**
     * 服务器节点
     */
    public static Node serverNode = new Node(Const.SERVER, Const.PORT);

    /**
     * handler, 包括编码、解码、消息处理
     */
    public static ClientAioHandler tioClientHandler = new SocketClientAioHandler();

    /**
     * 事件监听器，可以为null
     */
    public static ClientAioListener aioListener = null;

    /**
     * 断链后自动连接的，不想自动连接请设为null
     */
    private static ReconnConf reconnConf = new ReconnConf(5000L);

    /**
     * 一组连接共用的上下文对象
     */
    public static ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);

    public static TioClient tioClient = null;
    public static ClientChannelContext clientChannelContext = null;

    /**
     * 启动程序入口
     */
    public static void start() throws Exception {
        clientGroupContext.setHeartbeatTimeout(Const.HEARD_TIME_OUT);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
    }

}
