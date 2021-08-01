package jp.co.stnet.cms.config;

import jp.co.stnet.cms.common.dialect.PageInfoDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafDialectConfig {

    @Bean
    public PageInfoDialect pageInfoDialect() {
        return new PageInfoDialect();
    }
}