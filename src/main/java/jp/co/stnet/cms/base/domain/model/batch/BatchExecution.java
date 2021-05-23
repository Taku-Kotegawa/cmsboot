package jp.co.stnet.cms.base.domain.model.batch;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BatchExecution {
    private Long jobExecutionId = null;

    private String jobName = null;

    private Long stepExecutionId = null;

    private String stepName = null;

    private List<BatchStepExecution> batchStepExecutions = new ArrayList<>();

    private String status = null;

    private String exitStatus = null;

    private String errorMessage;

    private List<String> failureExceptions = new ArrayList<>();
}
