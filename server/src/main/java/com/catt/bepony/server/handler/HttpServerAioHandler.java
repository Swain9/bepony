package com.catt.bepony.server.handler;

import cn.hutool.core.util.RandomUtil;
import com.catt.bepony.common.constant.HttpPacket;
import com.catt.bepony.common.http.HttpChannelContextMap;
import com.catt.bepony.common.socket.CommonHttpHandler;
import com.catt.bepony.server.start.SocketServerStarter;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-24 23:37
 */
@Slf4j
public class HttpServerAioHandler implements ServerAioHandler {
    /**
     * 根据ByteBuffer解码成业务需要的Packet对象.
     * 如果收到的数据不全，导致解码失败，请返回null，在下次消息来时框架层会自动续上前面的收到的数据
     *
     * @param buffer         参与本次希望解码的ByteBuffer
     * @param limit          ByteBuffer的limit
     * @param position       ByteBuffer的position，不一定是0哦
     * @param readableLength ByteBuffer参与本次解码的有效数据（= limit - position）
     * @param channelContext
     * @return
     * @throws AioDecodeException
     */
    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return CommonHttpHandler.decode(buffer, limit, position, readableLength, channelContext);
    }

    /**
     * 编码：把业务消息包编码为可以发送的ByteBuffer
     * 总的消息结构：消息头 + 消息体
     * 消息头结构：    4个字节，存储消息体的长度
     * 消息体结构：   对象的json串的byte[]
     *
     * @param packet
     * @param groupContext
     * @param channelContext
     * @return
     * @author zhangmaolin
     */
    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return CommonHttpHandler.encode(packet, groupContext, channelContext);
    }

    /**
     * 处理消息包
     *
     * @param packet
     * @param channelContext
     * @throws Exception
     * @author zhangmaolin
     */
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        HttpPacket helloPacket = (HttpPacket) packet;
        byte[] body = helloPacket.getBody();
        if (body != null) {

            int size = SocketServerStarter.serverGroupContext.connections.size();
            if (size > 0) {

                Node serverNode = channelContext.getServerNode();
                int port = serverNode.getPort();

                helloPacket.setPort(port);
                String uuid = HttpChannelContextMap.get(channelContext);
                if (uuid == null) {
                    uuid = RandomUtil.simpleUUID();
                    HttpChannelContextMap.put(channelContext, uuid);
                }
                helloPacket.setUuid(uuid);

                Tio.sendToAll(SocketServerStarter.serverGroupContext, helloPacket);

            } else {
                HttpPacket resppacket = new HttpPacket();
                resppacket.setBody(post().getBytes());
                Tio.send(channelContext, resppacket);
            }


        }
    }

    private String post() {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK\r\n")
                .append("Content-Type:text/html;charset=utf-8\r\n")
                .append("Content-Length:38\r\n")
                .append("Server:gybs\r\n")
                .append("Date:")
                .append(new Date())
                .append("\r\n")
                .append("\r\n")
                .append("<h1>客户端没有打开！</h1>");
        return builder.toString();
    }
}
