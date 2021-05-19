package jp.co.stnet.cms.base.presentation.controller.job;


import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JobStarter {

    @Autowired
    JobOperator jobOperator;

    public Long start(String jobName, String jobParameters) throws JobParametersInvalidException, JobInstanceAlreadyExistsException, NoSuchJobException {

        String FromClassName = Thread.currentThread().getStackTrace()[2].getClassName();

        LoggedInUser loggedInUser = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "unknown";
        if (loggedInUser != null) {
            username = loggedInUser.getUsername();
        }

        jobParameters += ",executedBy=" + username;
        jobParameters += ",executedFromClass=" + FromClassName;

        return jobOperator.start(jobName, jobParameters);
    }


}
