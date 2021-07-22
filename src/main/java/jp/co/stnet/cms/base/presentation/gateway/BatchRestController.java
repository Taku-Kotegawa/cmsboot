package jp.co.stnet.cms.base.presentation.gateway;


import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.batch.BatchExecution;
import jp.co.stnet.cms.base.domain.model.batch.BatchOperation;
import jp.co.stnet.cms.base.domain.model.batch.BatchStepExecution;
import jp.co.stnet.cms.base.presentation.controller.job.JobStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/batch")
public class BatchRestController {

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobStarter jobStarter;

    @PostMapping(value = "{jobName}")
    public ResponseEntity<BatchOperation> launch(@PathVariable("jobName") String jobName,
                                                 @RequestBody BatchOperation requestResource) {

        BatchOperation responseResource = new BatchOperation();
        responseResource.setJobName(jobName);
        try {

//            Long jobExecutionId = jobOperator.start(jobName, requestResource.getJobParams());
            Long jobExecutionId = jobStarter.start(jobName, requestResource.getJobParams());
            responseResource.setJobExecutionId(jobExecutionId);
            requestResource.setJobParams(requestResource.getJobParams());
            return ResponseEntity.ok().body(responseResource);
        } catch (JobParametersInvalidException | ValidationException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
            responseResource.setError(e);
            return ResponseEntity.badRequest().body(responseResource);
        }
    }

    @GetMapping(value = "{jobExecutionId}")
    @ResponseStatus(HttpStatus.OK)
    public BatchExecution getJob(@PathVariable("jobExecutionId") Long jobExecutionId) {

        BatchExecution responseResource = new BatchExecution();
        responseResource.setJobExecutionId(jobExecutionId);

        JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);

        if (jobExecution == null) {
            responseResource.setErrorMessage("Job execution not found.");
        } else {
            mappingExecutionInfo(jobExecution, responseResource);
        }

        return responseResource;
    }

    @GetMapping("test")
    public String test(@AuthenticationPrincipal LoggedInUser loggedInUser) {

        log.info(loggedInUser.toString());

        if (false) {
            throw new OptimisticLockingFailureException("testtest");
        }

        return "test";
    }

    private void mappingExecutionInfo(JobExecution src, BatchExecution dest) {
        dest.setJobName(src.getJobInstance().getJobName());
        for (StepExecution se : src.getStepExecutions()) {
            BatchStepExecution ser = new BatchStepExecution();
            ser.setStepExecutionId(se.getId());
            ser.setStepName(se.getStepName());
            ser.setStatus(se.getStatus().toString());
            for (Throwable th : se.getFailureExceptions()) {
                ser.getFailureExceptions().add(th.toString());
            }
            dest.getBatchStepExecutions().add(ser);
        }
        dest.setStatus(src.getStatus().toString());
        dest.setExitStatus(src.getExitStatus().toString());
    }
}
