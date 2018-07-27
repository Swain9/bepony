package com.catt.bepony.common.constant;

import org.tio.core.intf.Packet;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 08:59
 */
public class HttpPacket extends Packet {
    private static final long serialVersionUID = -172060606924066412L;
    //消息头的长度
    public static final int HEADER_LENGHT = 4;
    //端口的长度
    public static final int PORT_LENGTH = 4;
    //唯一表示的长度
    public static final int UUID_LENGTH = 32;
    public static final String CHARSET = "utf-8";
    private byte[] body;
    private Integer port;
    private String uuid;

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
