package jp.co.stnet.cms.base.presentation.controller.job;


import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JobStarter {

    @Autowired
    JobOperator jobOperator;

    @Autowired
    JobRegistry jobRegistry;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("job03")
    Job job;

    private JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

    public Long start(String jobName, String parameters) throws JobParametersInvalidException, JobInstanceAlreadyExistsException, NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        String FromClassName = Thread.currentThread().getStackTrace()[2].getClassName();

        LoggedInUser loggedInUser = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "unknown";
        if (loggedInUser != null) {
            username = loggedInUser.getUsername();
        }

        parameters += ",executedBy=" + username;
        parameters += ",executedFromClass=" + FromClassName;


        JobParameters jobParameters = this.jobParametersConverter.getJobParameters(PropertiesConverter.stringToProperties(parameters));

        System.out.println(jobRegistry.getJobNames());


        return jobLauncher.run(job, jobParameters).getJobId();


//        return jobOperator.start(jobName, jobParameters);
    }


}
