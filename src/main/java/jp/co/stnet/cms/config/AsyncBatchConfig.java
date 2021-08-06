package jp.co.stnet.cms.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@EnableBatchProcessing
@EnableAsync
@Configuration
public class AsyncBatchConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    JobRepository jobRepository;

    @Bean
    public BatchConfigurer batchConfigurer(DataSource dataSource) {
        return new DefaultBatchConfigurer(dataSource) {
            @Override
            protected JobLauncher createJobLauncher() throws Exception {
                SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
                jobLauncher.setJobRepository(jobRepository);
                SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("job-");
                taskExecutor.setConcurrencyLimit(3);
                jobLauncher.setTaskExecutor(taskExecutor);
                jobLauncher.afterPropertiesSet();
                return jobLauncher;
            }
        };
    }

}
