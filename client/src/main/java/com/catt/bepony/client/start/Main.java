package com.catt.bepony.client.start;

import com.catt.bepony.common.http.HttpChannelContextMap;
import com.catt.bepony.common.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangmaolin
 * @version 0.0.1
 * @since 2018-07-25 17:46
 */
@Slf4j
public class Main {


    public static void main(String[] args) throws Exception {
        //String[] newArgs = ArrayUtil.merge(args, "127.0.0.1:8180");
        //init(newArgs);
        init(args);
    }

    private static void init(String[] args) throws Exception {
        int length = args.length;
        Set<Node> nodes = new HashSet<>();

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (CheckUtil.checkIpAndPort(args[i])) {
                    String[] split = args[i].split(":");
                    nodes.add(new Node(split[0], Integer.valueOf(split[1])));
                    continue;
                }
                log.error("请输入正确的IP地址和端口号");
                return;
            }
        } else {
            nodes.add(new Node("127.0.0.1", 8080));
        }

        HttpChannelContextMap.getInstance();
        SocketClientStarter.start();
        HttpClientStarter.start(nodes);
    }

}
