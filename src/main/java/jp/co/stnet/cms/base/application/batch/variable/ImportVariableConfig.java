package jp.co.stnet.cms.base.application.batch.variable;

import jp.co.stnet.cms.common.constant.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@EnableBatchProcessing
@Configuration
public class ImportVariableConfig {

    private final String JOB_ID = Constants.JOBID.IMPORT_VARIABLE;

    private final String taskletName = JOB_ID + "Tasklet";

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier(taskletName)
    Tasklet tasklet;

    /**
     * メソッド名でBean定義される点に注意
     *
     * @return
     */
    @Bean(name = JOB_ID)
    public Job importVariable() {
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
