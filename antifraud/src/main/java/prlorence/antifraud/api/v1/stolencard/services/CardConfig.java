package prlorence.antifraud.api.v1.stolencard.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardConfig {

    @Bean
    public CardControl cardControl() {
        return new CardControl();
    }

    @Bean
    public ValidateCard validateCard() {
        return new ValidateCard();
    }
}
