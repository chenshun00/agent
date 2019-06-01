package top.huzhurong.boot.collect.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author chenshun00@gmail.com
 * @since 2019/5/24
 */
@Configuration
@Slf4j
public class Config {
    @Value("${elasticsearch.host:127.0.0.1}")
    public String host;
    @Value("${elasticsearch.port:9300}")
    public int port;

    @Bean
    public Client client() {
        TransportClient client = null;
        try {
            Settings aaa = Settings.builder().put("cluster.name", "aaa").build();
            log.info("host:[{}] ,port:[{}]", host, port);
            client = new PreBuiltTransportClient(aaa)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

}
