package com.catt.bepony.client.handler;

import com.catt.bepony.client.start.HttpClientStarter;
import com.catt.bepony.common.constant.HttpPacket;
import com.catt.bepony.common.http.HttpChannelContextMap;
import com.catt.bepony.common.socket.CommonSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 17:51
 */
@Slf4j
public class SocketClientAioHandler implements ClientAioHandler {
    /**
     * 心跳包
     */
    private static HttpPacket heartbeatPacket = new HttpPacket();

    /**
     * 创建心跳包
     * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
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
     * <p>
     * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
     * 总的消息结构：消息头 + 消息体
     * 消息头结构：    4个字节，存储消息体的长度
     * 消息体结构：   对象的json串的byte[]
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
        Integer port = helloPacket.getPort();
        if (body != null) {

            ChannelContext connect = HttpChannelContextMap.getByUUID(helloPacket.getUuid());
            if (connect == null) {
                connect = HttpClientStarter.tioClient.connect(HttpClientStarter.serverNode.get(port));
                HttpChannelContextMap.put(connect, helloPacket.getUuid());
            }

            Tio.send(connect, helloPacket);
            log.info("本次请求的ID：{}，端口：{}", helloPacket.getUuid(), helloPacket.getPort());
        }
    }
}
