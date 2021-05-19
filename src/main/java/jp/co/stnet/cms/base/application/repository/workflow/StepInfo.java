package jp.co.stnet.cms.base.application.repository.workflow;

import lombok.Data;

import java.util.List;

@Data
public class StepInfo {

    /**
     * 処理中のステップ番号
     */
    private int currentStepNo;

    /**
     * 処理中のステップのステータス
     */
    private int currentStepStatus;

    /**
     * 処理中のステップが最終ステップかどうか(true:最終ステップ, false:最終でない)
     */
    private boolean currentIsLast;

    /**
     * 処理中のステップの従業員別のステータス
     */
    private List<EmpStepStatus> currentStepEmployees;

    /**
     * 一つ前のステップの従業員別のステータス
     */
    private List<EmpStepStatus> prevStepEmployees;

}
