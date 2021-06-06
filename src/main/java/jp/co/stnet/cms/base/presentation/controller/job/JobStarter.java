package jp.co.stnet.cms.base.presentation.controller.job;


import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JobStarter {

    private final JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    ApplicationContext applicationContext;

    public Long start(String jobName, String parameters) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        String FromClassName = Thread.currentThread().getStackTrace()[2].getClassName();

        LoggedInUser loggedInUser = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "unknown";
        if (loggedInUser != null) {
            username = loggedInUser.getUsername();
        }

        parameters += ",executedBy=" + username;
        parameters += ",executedFromClass=" + FromClassName;

        JobParameters jobParameters = this.jobParametersConverter.getJobParameters(PropertiesConverter.stringToProperties(parameters));

        Job job = (Job) applicationContext.getBean(jobName);

        return jobLauncher.run(job, jobParameters).getJobId();

    }
}
