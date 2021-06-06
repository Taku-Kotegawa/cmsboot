package jp.co.stnet.cms.config;

import jp.co.stnet.cms.base.presentation.interceptor.BlockHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class IntercepterConfig {

    @Bean
    public BlockHandlerInterceptor blockHandlerInterceptor() {
        return new BlockHandlerInterceptor();
    }

    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/**"}, blockHandlerInterceptor());
    }

}
