package com.catt.bepony.client.handler;

import com.catt.bepony.client.start.SocketClientStarter;
import com.catt.bepony.common.constant.HttpPacket;
import com.catt.bepony.common.http.HttpChannelContextMap;
import com.catt.bepony.common.socket.CommonHttpHandler;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * https://code.joejag.com/2012/how-to-send-a-raw-http-request-via-java.html
 *
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 18:22
 */
public class HttpClientAioHandler implements ClientAioHandler {
    /**
     * 创建心跳包
     *
     * @return
     * @author zhangmaolin
     */
    @Override
    public Packet heartbeatPacket() {
        return null;
    }

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
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) {
        return CommonHttpHandler.decode(buffer, limit, position, readableLength, channelContext);
    }

    /**
     * 编码，客户端发送http请求，不需要编码端口
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
            int port = channelContext.getServerNode().getPort();
            helloPacket.setPort(port);

            String uuid = HttpChannelContextMap.get(channelContext);
            if (uuid != null) {

                helloPacket.setUuid(uuid);

                Tio.send(SocketClientStarter.clientChannelContext, helloPacket);
            }
        }
    }
}
