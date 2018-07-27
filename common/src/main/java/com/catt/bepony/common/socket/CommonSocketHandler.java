package com.catt.bepony.common.socket;

import cn.hutool.core.util.RandomUtil;
import com.catt.bepony.common.constant.HttpPacket;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-27 09:09
 */
@Slf4j
public class CommonSocketHandler {
    public static ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        HttpPacket helloPacket = (HttpPacket) packet;
        byte[] body = helloPacket.getBody();
        int port = 0;
        int bodyLen = 0;
        if (body != null) {
            bodyLen = body.length;
            port = helloPacket.getPort();
        }

        //bytebuffer的总长度是 = 消息头的长度 + 端口占用的长度 +uuid长度+ 消息体的长度
        int allLen = HttpPacket.HEADER_LENGHT + HttpPacket.PORT_LENGTH + HttpPacket.UUID_LENGTH + bodyLen;
        //创建一个新的bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(groupContext.getByteOrder());

        //写入消息头----消息头的内容就是消息体的长度
        buffer.putInt(bodyLen);
        //写入端口号
        buffer.putInt(port);
        //写入uuid
        String uuid = helloPacket.getUuid();
        if (uuid == null) {
            uuid = RandomUtil.simpleUUID();
        }
        try {
            buffer.put(uuid.getBytes(HttpPacket.CHARSET));
        } catch (UnsupportedEncodingException e) {
            log.error("编码错误", e);
            throw new RuntimeException(e);
        }

        //写入消息体
        if (body != null) {
            buffer.put(body);
        }
        return buffer;
    }

    public static Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        //提醒：buffer的开始位置并不一定是0，应用需要从buffer.position()开始读取数据
        //收到的数据组不了业务包，则返回null以告诉框架数据不够
        if (readableLength < HttpPacket.HEADER_LENGHT + HttpPacket.PORT_LENGTH + HttpPacket.UUID_LENGTH) {
            return null;
        }

        //读取消息体的长度
        int bodyLength = buffer.getInt();

        //数据不正确，则抛出AioDecodeException异常
        if (bodyLength < 0) {
            throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
        }

        //计算本次需要的数据长度
        int neededLength = HttpPacket.HEADER_LENGHT + HttpPacket.PORT_LENGTH + HttpPacket.UUID_LENGTH + bodyLength;
        //收到的数据是否足够组包
        int isDataEnough = readableLength - neededLength;
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (isDataEnough < 0) {
            return null;
        } else //组包成功
        {
            HttpPacket imPacket = new HttpPacket();
            int port = buffer.getInt();
            imPacket.setPort(port);
            byte[] uuid = new byte[HttpPacket.UUID_LENGTH];
            buffer.get(uuid);
            try {
                imPacket.setUuid(new String(uuid, HttpPacket.CHARSET));
            } catch (UnsupportedEncodingException e) {
                log.error("错误的编码格式", e);
                throw new RuntimeException(e);
            }
            if (bodyLength > 0) {
                byte[] dst = new byte[bodyLength];
                buffer.get(dst);
                imPacket.setBody(dst);
            }
            return imPacket;
        }
    }
}
