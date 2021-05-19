package jp.co.stnet.cms.base.application.repository.workflow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmpStepStatus {

    private Long employeeId;

    private Integer stepStatus;

}
