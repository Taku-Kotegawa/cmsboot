package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.util.StateMap;
import jp.co.stnet.cms.common.util.StringUtils;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AdminAccountHelper {

    // 許可されたOperation
    private static final Set<String> allowedOperation = Set.of(
            Constants.OPERATION.CREATE,
            Constants.OPERATION.UPDATE,
            Constants.OPERATION.VIEW
    );

    // 表示するボタン
    private static final Set<String> buttons = Set.of(
            Constants.BUTTON.GOTOLIST,
            Constants.BUTTON.GOTOUPDATE,
            Constants.BUTTON.VIEW,
            Constants.BUTTON.SAVE,
            Constants.BUTTON.VALID,
            Constants.BUTTON.INVALID,
            Constants.BUTTON.DELETE,
            Constants.BUTTON.UNLOCK,
            Constants.BUTTON.SET_APIKEY,
            Constants.BUTTON.UNSET_APIKEY,
            Constants.BUTTON.SWITCH_USER
    );

    /**
     * 許可されたOperationか
     *
     * @param operation 操作を表す定数
     */
    private void validate(String operation) {
        if (!allowedOperation.contains(operation)) {
            throw new IllegalArgumentException("Operation not allowed.");
        }
    }

    /**
     * 画面に応じたボタンの状態を定義
     *
     * @param operation 操作
     * @param record DBから取り出したデータ
     * @param form 画面から入力されたデータ
     * @return StateMap
     */
    StateMap getButtonStateMap(@NonNull String operation, Account record, AccountForm form) {

        // 入力チェック
        validate(operation);

        if (record == null) {
            record = new Account();
        }

        // StateMap初期化
        List<String> includeKeys = new ArrayList<>();
        includeKeys.addAll(buttons);
        StateMap buttonState = new StateMap(Default.class, includeKeys, new ArrayList<>());

        // 常に表示
        buttonState.setViewTrue(Constants.BUTTON.GOTOLIST);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            buttonState.setViewTrue(Constants.BUTTON.SAVE);
        }

        // 編集
        else if (Constants.OPERATION.UPDATE.equals(operation)) {

            // ステータス有効
            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
                buttonState.setViewTrue(Constants.BUTTON.SWITCH_USER);
                buttonState.setViewTrue(Constants.BUTTON.UNLOCK);

                // API KEY 設定有無
                if (StringUtils.isEmpty(form.getApiKey())) {
                    buttonState.setViewTrue(Constants.BUTTON.SET_APIKEY);
                } else {
                    buttonState.setViewTrue(Constants.BUTTON.UNSET_APIKEY);
                }
            }

            // ステータス無効
            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
            }

        }

        // 参照
        else if (Constants.OPERATION.VIEW.equals(operation)) {

            // スタータス有効
            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
                buttonState.setViewTrue(Constants.BUTTON.SWITCH_USER);
            } else {
                // ステータス無効
                buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
            }
        }

        return buttonState;
    }

    /**
     * 画面に応じたフィールドの状態を定義
     *
     * @param operation 操作
     * @param record DBから取り出したデータ
     * @param form 画面から入力されたデータ
     * @return StateMap
     */
    StateMap getFiledStateMap(String operation, Account record, AccountForm form) {
        List<String> excludeKeys = new ArrayList<>();

        // 常設の隠しフィールドは状態管理しない
        StateMap fieldState = new StateMap(AccountForm.class, new ArrayList<>(), excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setReadOnlyTrue("apiKey");
        }

        // 編集
        else if (Constants.OPERATION.UPDATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setDisabledTrue("username");
            fieldState.setReadOnlyTrue("apiKey");

            // スタータスが無効
            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                // fieldState.setReadOnlyTrueAll();
                fieldState.setDisabledTrueAll();
            }
        }

        // 参照
        else if (Constants.OPERATION.VIEW.equals(operation)) {
            fieldState.setViewTrueAll();
            fieldState.setViewFalse("password");
        }

        return fieldState;
    }

}