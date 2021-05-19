package jp.co.stnet.cms.base.application.service.workflow;



import jp.co.stnet.cms.base.application.repository.workflow.StepInfo;
import jp.co.stnet.cms.base.domain.model.workflow.Workflow;

import java.util.List;

public interface WorkflowSharedService {

    /**
     * ワークフローステップを初期化する.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     */
    void init(String entityType, Long entityId);

    /**
     * 次のステップに進める.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @param employeeId 従業員ID
     */
    void goNextStep(String entityType, Long entityId, Long employeeId);

    /**
     * 「次のステップに進める」操作の権限チェック.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @param employeeId 従業員ID
     * @return TRUE: 操作可能, FALSE: 不可
     */
    boolean canGoNextStep(String entityType, Long entityId, Long employeeId);

    /**
     * 前のステップに戻す.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @param employeeId 従業員ID
     */
    void returnPrevStep(String entityType, Long entityId, Long employeeId);

    /**
     * 「前のステップに戻す」操作の権限チェック.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @param employeeId 従業員ID
     * @return TRUE: 操作可能, FALSE: 不可
     */
    boolean canReturnPrevStep(String entityType, Long entityId, Long employeeId);

    /**
     * ステップを引き戻す.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @param employeeId 従業員ID
     */
    void pullbackStep(String entityType, Long entityId, Long employeeId);

    /**
     * 「ステップを引き戻す」操作の権限チェック.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @param employeeId 従業員ID
     * @return TRUE: 操作可能, FALSE: 不可
     */
    boolean canPullbackStep(String entityType, Long entityId, Long employeeId);

    /**
     * 現在のステップの情報を取得する.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @return 対象エンティティの現在のステップの情報
     */
    Workflow getCurrentStepInfo(String entityType, Long entityId);

    /**
     * 対象エンティティのすべてのステップの情報を取得する.
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @return 対象エンティティの全ステップ情報
     */
    List<Workflow> getAllStepInfo(String entityType, Long entityId);

    /**
     * 指定エンティティのステップに関する情報をMapで取得する.
     * <p>
     * current_step_no        int 現在ステップ番号
     * current_step_status    int 現在ステップステータス(再優先)
     * current_step_employee  array(employee_id, employee_status, employee_weight)
     * 現在ステップの担当者(従業員番号)とステータス
     * prev_step_employee     array(employee_id, employee_status, employee_weight)
     * １つ前のステップの担当者(従業員番号)とステータス
     * max_step_no            int 最大ステップ番号
     * current_is_max         bool 現在ステップが最大ステップか？
     * </p>
     *
     * @param entityType エンティティタイプ
     * @param entityId   エンティティの内部ID
     * @return ステップに関する情報を格納したMap
     */
    StepInfo getStepInfo(String entityType, Long entityId);

}
