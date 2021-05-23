package jp.co.stnet.cms.base.domain.model.batch;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class BatchStepExecution {
    private Long stepExecutionId = null;

    private String stepName = null;

    private String status = null;

    private List<String> failureExceptions = new ArrayList<>();
}
