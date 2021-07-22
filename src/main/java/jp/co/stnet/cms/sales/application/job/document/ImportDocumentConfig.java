package jp.co.stnet.cms.sales.application.job.document;

import jp.co.stnet.cms.common.constant.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;

@EnableBatchProcessing
@Configuration
public class ImportDocumentConfig {

    private static final String JOB_ID = Constants.JOBID.IMPORT_DOCUMENT;

    private static final String TASKLET_NAME = JOB_ID + "Tasklet";

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Named(TASKLET_NAME)
    Tasklet tasklet;

    /**
     * メソッド名でBean定義される点に注意
     *
     * @return
     */
    @Bean(name = JOB_ID)
    public Job importDocument() {
        return jobBuilderFactory.get(JOB_ID)
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet)
                .build();
    }
}
