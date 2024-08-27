package groupbee.employee.config;

import feign.Logger;
import feign.RequestInterceptor;
import groupbee.employee.interceptor.MailInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {
    @Bean
    public RequestInterceptor MailInterceptor() {
        return new MailInterceptor();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            System.out.println("Request: " + template.url());
            template.headers().forEach((key, value) ->
                    System.out.println(key + ": " + value));
        };
    }
//    @Bean
//    public RequestInterceptor sessionInterceptor() {
//        return redisSessionInterceptor;
//    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
