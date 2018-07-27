package com.catt.bepony.server.handler;

import com.catt.bepony.common.constant.HttpPacket;
import com.catt.bepony.common.http.HttpChannelContextMap;
import com.catt.bepony.common.socket.CommonSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-24 23:37
 */
@Slf4j
public class SocketServerAioHandler implements ServerAioHandler {
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
        return CommonSocketHandler.decode(buffer, limit, position, readableLength, channelContext);
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
        return CommonSocketHandler.encode(packet, groupContext, channelContext);
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

            Integer port = helloPacket.getPort();
            if (port == null || port == 0) {
                return;
            }
            String uuid = helloPacket.getUuid();

            ChannelContext context = HttpChannelContextMap.getByUUID(uuid);

            Tio.send(context, helloPacket);
        }
    }
}
