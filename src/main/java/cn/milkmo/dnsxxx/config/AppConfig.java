package cn.milkmo.dnsxxx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {
    private EcsConfig ecsConfig;
    private List<String> domainSuffixes = Arrays.asList(".cn", ".me", ".com", ".net", ".top", ".com.cn");
}
