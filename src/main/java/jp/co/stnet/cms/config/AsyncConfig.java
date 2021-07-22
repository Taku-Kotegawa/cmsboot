package jp.co.stnet.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

//    @Bean
//    public Executor taskExecutor1() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(3);
//        executor.setQueueCapacity(10);
//        executor.setThreadNamePrefix("Thread1--");
//        executor.initialize();
//        return executor;
//    }

}
