package prlorence.antifraud.api.v1.auth.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilityConfig {
    @Bean
    public ResolveUser resolveUser() {
        return new ResolveUser();
    }

    @Bean
    public UserControl userControl() {
        return new UserControl();
    }

    @Bean
    public Admin admin() {
        return new Admin();
    }
}
