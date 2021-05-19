package jp.co.stnet.cms.base.application.service.workflow;

import jp.co.stnet.cms.base.application.repository.workflow.EmpStepStatus;
import jp.co.stnet.cms.base.application.repository.workflow.StepInfo;
import jp.co.stnet.cms.base.application.repository.workflow.WorkflowRepository;
import jp.co.stnet.cms.base.domain.model.workflow.Workflow;
import jp.co.stnet.cms.common.exception.InvalidArgumentBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.terasoluna.gfw.common.message.ResultMessages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkflowSharedServiceImpl implements WorkflowSharedService {

    private final int STATUS_INIT = 0; // 未処理
    private final int STATUS_WORKING = 1; // 対応中
    private final int STATUS_COMPLETE = 2; // 完了
    private final int STATUS_REJECT = 3; // 差戻し
    private final int STATUS_PULLBACK = 4; // 引戻し

    private final String GO_NEXT = "goNext";
    private final String RETURN_PREV = "returnPrev";
    private final String PULL_BACK = "pullBack";
    private final String INITIALIZE = "initialize";


    private final int FIRST_STEP_NO = 1;
    private final int LAST_STEP_NO = 10;


    // ステータスの優先順位(2:完了->3:差戻->4:引戻->1:対応中->0:未対応)
    private Map<Integer, Integer> STATUS_ORDER = new LinkedHashMap<Integer, Integer>() {{
        put(STATUS_COMPLETE, 1);
        put(STATUS_REJECT, 2);
        put(STATUS_PULLBACK, 3);
        put(STATUS_WORKING, 4);
        put(STATUS_INIT, 5);
    }};

    private final List<String> OPERATIONS = new ArrayList<String>() {{
        add(GO_NEXT);
        add(RETURN_PREV);
        add(PULL_BACK);
        add(INITIALIZE);
    }};

    @Autowired
    WorkflowRepository workflowRepository;


    @Override
    public void init(String entityType, Long entityId) {

        // 保存されているデータの削除
        workflowRepository.deleteAll(
                workflowRepository.findAll(Example.of(Workflow.builder()
                        .entityType(entityType)
                        .entityId(entityId)
                        .build())));

        // 新規データの挿入
        List<Workflow> newRecords = new ArrayList<>();
        for (int i = FIRST_STEP_NO; i <= LAST_STEP_NO; i++) {
            newRecords.add(Workflow.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .stepNo(i)
                    .employeeId(getEmployeeIdByStep(entityType, entityId, i))
                    .stepStatus(STATUS_INIT)
                    .build());
        }
        workflowRepository.saveAll(newRecords);

    }

    @Override
    public void goNextStep(String entityType, Long entityId, Long employeeId) {

        if (!canGoNextStep(entityType, entityId, employeeId)) {
            throw new InvalidArgumentBusinessException(ResultMessages.error().add("操作権限がありません。"));
        }

        StepInfo stepInfo = getStepInfo(entityType, entityId);

        // 現在のステップのステータスを完了に変更
        List<Workflow> workflowList = workflowRepository.findAll(Example.of(Workflow.builder()
                .entityType(entityType)
                .entityId(entityId)
                .stepNo(stepInfo.getCurrentStepNo())
                .employeeId(employeeId)
                .build()));
        if (workflowList.size() > 0) {
            workflowList.get(0).setStepStatus(STATUS_COMPLETE);
        }

        // 次のステップが存在する場合
        if (stepInfo.getCurrentStepNo() < LAST_STEP_NO) {
            // 次のステップのステータスを対応中に変更
            List<Workflow> workflowList2 = workflowRepository.findAll(Example.of(Workflow.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .stepNo(stepInfo.getCurrentStepNo() + 1)
                    .build()));
            workflowList2.iterator().next().setStepStatus(STATUS_WORKING);
        }
    }

    @Override
    public boolean canGoNextStep(String entityType, Long entityId, Long employeeId) {
        return this.canOperations(GO_NEXT, entityType, entityId, employeeId);
    }

    @Override
    public void returnPrevStep(String entityType, Long entityId, Long employeeId) {

        if (!canReturnPrevStep(entityType, entityId, employeeId)) {
            throw new InvalidArgumentBusinessException(ResultMessages.error().add("操作権限がありません。"));
        }

        StepInfo stepInfo = getStepInfo(entityType, entityId);

        // 現在のステップのステータスを「完了」に変更
        List<Workflow> workflowList = workflowRepository.findAll(Example.of(Workflow.builder()
                .entityType(entityType)
                .entityId(entityId)
                .stepNo(stepInfo.getCurrentStepNo())
                .employeeId(employeeId)
                .build()));
        if (workflowList.size() > 0) {
            workflowList.get(0).setStepStatus(STATUS_WORKING);
        }

        // 前のステップのステータスを「差戻し」に変更
        List<Workflow> workflowList2 = workflowRepository.findAll(Example.of(Workflow.builder()
                .entityType(entityType)
                .entityId(entityId)
                .stepNo(stepInfo.getCurrentStepNo() - 1)
                .build()));
        workflowList2.iterator().next().setStepStatus(STATUS_REJECT);
    }

    @Override
    public boolean canReturnPrevStep(String entityType, Long entityId, Long employeeId) {
        return this.canOperations(RETURN_PREV, entityType, entityId, employeeId);
    }

    @Override
    public void pullbackStep(String entityType, Long entityId, Long employeeId) {

        if (!canPullbackStep(entityType, entityId, employeeId)) {
            throw new InvalidArgumentBusinessException(ResultMessages.error().add("操作権限がありません。"));
        }

        StepInfo stepInfo = getStepInfo(entityType, entityId);

        if (stepInfo.isCurrentIsLast() && stepInfo.getCurrentStepStatus() == STATUS_COMPLETE) {
            // 現在のステップのステータスを「引戻し」に変更
            List<Workflow> workflowList = workflowRepository.findAll(Example.of(Workflow.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .stepNo(stepInfo.getCurrentStepNo())
                    .employeeId(employeeId)
                    .build()));
            if (workflowList.size() > 0) {
                workflowList.get(0).setStepStatus(STATUS_PULLBACK);
            }

        } else {
            // 前のステップのステータスを「引戻し」に変更
            List<Workflow> workflowList = workflowRepository.findAll(Example.of(Workflow.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .stepNo(stepInfo.getCurrentStepNo() -1)
                    .employeeId(employeeId)
                    .build()));
            workflowList.iterator().next().setStepStatus(STATUS_PULLBACK); //一件しかないはず

            // 現在のステップのステータスを「未処理」に変更
            List<Workflow> workflowList2 = workflowRepository.findAll(Example.of(Workflow.builder()
                    .entityType(entityType)
                    .entityId(entityId)
                    .stepNo(stepInfo.getCurrentStepNo())
                    .build()));
            workflowList2.iterator().next().setStepStatus(STATUS_INIT);
        }
    }

    @Override
    public boolean canPullbackStep(String entityType, Long entityId, Long employeeId) {
        return this.canOperations(PULL_BACK, entityType, entityId, employeeId);
    }

    @Override
    public Workflow getCurrentStepInfo(String entityType, Long entityId) {







        return null;
    }

    @Override
    public List<Workflow> getAllStepInfo(String entityType, Long entityId) {
        return null;
    }

    @Override
    public StepInfo getStepInfo(String entityType, Long entityId) {
        return null;
    }

    /**
     * 授業員番号の初期値を求める
     * @param entityType エンティティタイプ
     * @param entityId エンティティID
     * @param step_no ステップ番号
     * @return
     */
    protected Long getEmployeeIdByStep(String entityType, Long entityId, int step_no) {
        // TODO
        return 1L;
    }


    /**
     * 操作権限を確認する。
     *
     * @param op
     * @param entityType
     * @param entityId
     * @param employeeId
     * @return
     */
    protected boolean canOperations(String op, String entityType, Long entityId, Long employeeId) {

        if (!OPERATIONS.contains(op)) {
            throw new IllegalArgumentException("op = " + op);
        }

        if (entityType == null) {
            throw new IllegalArgumentException("entityType is null ");
        }

        if (entityId == null) {
            throw new IllegalArgumentException("entityId is null");
        }

        if (employeeId == null) {
            throw new IllegalArgumentException("employeeId is null");
        }

        // TODO 引数の厳密なチェック


        // TODO エントリシート全体が確定(変更不能状態)の場合、常にfalseを返す。


        // 現在のステップ情報を取得.
        StepInfo stepInfo = this.getStepInfo(entityType, entityId);
        if (stepInfo == null) {
            return false;
        }

        if (op.equals(GO_NEXT)) {
            // 現ステップの担当者に対象者が含まれ、そのステータスが完了以外の場合.
            for (EmpStepStatus s : stepInfo.getCurrentStepEmployees()) {
                if (s.getEmployeeId().equals(employeeId) && s.getStepStatus() != STATUS_COMPLETE) {
                    return true;
                }
            }

        } else if (op.equals(RETURN_PREV)) {
            // 現ステップが１でない
            if (stepInfo.getCurrentStepNo() == 1) {
                return false;
            }

            // 現ステップの担当者に対象者が含まれ、そのステータスが完了以外の場合.
            for (EmpStepStatus s : stepInfo.getCurrentStepEmployees()) {
                if (s.getEmployeeId().equals(employeeId) && s.getStepStatus() != STATUS_COMPLETE) {
                    return true;
                }
            }

        } else if (op.equals(PULL_BACK)) {
            // 最終ステップが完了の場合
            if (stepInfo.isCurrentIsLast() && stepInfo.getCurrentStepStatus() == STATUS_COMPLETE) {
                return true;
            }
            // 現ステップ−１の担当者に対象者が含まれ、現ステップのステータスが未処理.
            for (EmpStepStatus s : stepInfo.getPrevStepEmployees()) {
                if (s.getEmployeeId().equals(employeeId) && stepInfo.getCurrentStepStatus() == STATUS_INIT) {
                    return true;
                }
            }
        }

        return false;
    }


}
