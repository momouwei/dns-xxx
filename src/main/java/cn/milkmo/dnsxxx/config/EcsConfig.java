package cn.milkmo.dnsxxx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app-config.ecs-config")
public class EcsConfig {
    private String provider;
    private String regionId;
    private String accessKeyId;
    private String accessKeySecret;
}
