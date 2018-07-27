package com.catt.bepony.client.start;

import com.catt.bepony.client.handler.HttpClientAioHandler;
import com.catt.bepony.client.listener.HttpClientAioListener;
import com.catt.bepony.common.constant.Const;
import lombok.extern.slf4j.Slf4j;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.TcpConst;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 21:36
 */
@Slf4j
public class HttpClientStarter {
    /**
     * 服务器节点
     */
    public static Map<Integer, Node> serverNode = new HashMap<>();

    /**
     * handler, 包括编码、解码、消息处理
     */
    public static ClientAioHandler tioClientHandler = new HttpClientAioHandler();

    /**
     * 事件监听器，可以为null
     */
    public static ClientAioListener aioListener = new HttpClientAioListener();

    /**
     * 断链后自动连接的，不想自动连接请设为null
     */
    //private static ReconnConf reconnConf = new ReconnConf(5000L);
    private static ReconnConf reconnConf = null;

    /**
     * 一组连接共用的上下文对象
     */
    public static ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);

    public static TioClient tioClient = null;
    public static Map<Integer, ClientChannelContext> clientChannelContext = null;

    /**
     * 启动程序入口
     */
    public static void start(Set<Node> nodes) throws Exception {
        clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        clientGroupContext.setShortConnection(true);
        clientGroupContext.setReadBufferSize(TcpConst.MAX_DATA_LENGTH);
        clientGroupContext.setUseQueueSend(false);
        clientGroupContext.setUseQueueDecode(false);
        tioClient = new TioClient(clientGroupContext);
        nodes.forEach(node -> {
            serverNode.put(node.getPort(), node);
        });
    }
}
