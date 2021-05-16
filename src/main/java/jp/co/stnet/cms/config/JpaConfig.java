package jp.co.stnet.cms.config;

import jp.co.stnet.cms.common.auditing.DateTimeProviderImpl;
import jp.co.stnet.cms.common.auditing.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware", dateTimeProviderRef = "dateTimeProviderImpl")
@Configuration
public class JpaConfig {

    @Bean(name = "springSecurityAuditorAware")
    public SpringSecurityAuditorAware springSecurityAuditorAware() {
        return new SpringSecurityAuditorAware();
    }

    @Bean
    public DateTimeProviderImpl dateTimeProviderImpl() {
        return new DateTimeProviderImpl();
    }

}
