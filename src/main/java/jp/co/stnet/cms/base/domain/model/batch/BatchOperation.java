package jp.co.stnet.cms.base.domain.model.batch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchOperation {

    private String jobName = null;

    private String jobParams = null;

    private Long jobExecutionId = null;

    private String errorMessage = null;

    private Exception error = null;

}
