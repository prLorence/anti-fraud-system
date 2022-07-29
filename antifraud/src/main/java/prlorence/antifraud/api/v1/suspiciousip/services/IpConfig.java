package prlorence.antifraud.api.v1.suspiciousip.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpConfig {
    @Bean
    public ValidateIp validateIp() {
        return new ValidateIp();
    }
    @Bean
    public IpControl ipControl() {
        return new IpControl();
    }
}
