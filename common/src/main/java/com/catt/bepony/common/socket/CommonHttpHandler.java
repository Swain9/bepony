package com.catt.bepony.common.socket;

import com.catt.bepony.common.constant.HttpPacket;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-27 09:29
 */
public class CommonHttpHandler {
    private CommonHttpHandler() {

    }

    public static Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) {
        HttpPacket imPacket = new HttpPacket();
        byte[] dst = new byte[readableLength];
        buffer.get(dst);
        imPacket.setBody(dst);
        return imPacket;
    }

    public static ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        HttpPacket helloPacket = (HttpPacket) packet;
        byte[] body = helloPacket.getBody();
        int bodyLen = 0;
        if (body != null) {
            bodyLen = body.length;
        }
        int allLen = bodyLen;
        //创建一个新的bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(groupContext.getByteOrder());
        //写入消息体
        if (body != null) {
            buffer.put(body);
        }
        return buffer;
    }
}
