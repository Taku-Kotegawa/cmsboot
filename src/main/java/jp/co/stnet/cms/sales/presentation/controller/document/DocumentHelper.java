package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.util.StateMap;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import lombok.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DocumentHelper {

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
            Constants.BUTTON.SAVE_DRAFT,
            Constants.BUTTON.CANCEL_DRAFT,
            Constants.BUTTON.COPY,
            Constants.BUTTON.ADD_ITEM
    );

    // 対象のフォーム
    private static final Class formClass = DocumentForm.class;

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
     * @param record    DBから取り出したデータ
     * @param form      画面から入力されたデータ
     * @return StateMap
     */
    StateMap getButtonStateMap(@NonNull String operation, Document record, DocumentForm form) {

        // 入力チェック
        validate(operation);

        if (record == null) {
            record = new Document();
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
            buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
            buttonState.setViewTrue(Constants.BUTTON.ADD_ITEM);
        }

        // 編集
        else if (Constants.OPERATION.UPDATE.equals(operation)) {

            // ステータスが下書き
            if (Status.DRAFT.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.CANCEL_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.COPY);
                buttonState.setViewTrue(Constants.BUTTON.ADD_ITEM);
            }

            // ステータス有効
            else if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
                buttonState.setViewTrue(Constants.BUTTON.COPY);
                buttonState.setViewTrue(Constants.BUTTON.ADD_ITEM);
            }

            // ステータス無効
            else if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
                buttonState.setViewTrue(Constants.BUTTON.COPY);
            }

        }

        // 参照
        else if (Constants.OPERATION.VIEW.equals(operation)) {
            buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
        }

        return buttonState;
    }

    /**
     * 画面に応じたフィールドの状態を定義
     *
     * @param operation 操作
     * @param record    DBから取り出したデータ
     * @param form      画面から入力されたデータ
     * @return StateMap
     */
    StateMap getFiledStateMap(String operation, Document record, DocumentForm form) {
        List<String> excludeKeys = new ArrayList<>();
        List<String> includeKeys = List.of("status");

        // 常設の隠しフィールドは状態管理しない
        StateMap fieldState = new StateMap(formClass, includeKeys, excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setInputFalse("status");
            fieldState.setInputFalse("saveRevision");

        }

        // 編集
        else if (Constants.OPERATION.UPDATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setViewTrue("status");

            // スタータスが無効
            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                fieldState.setDisabledTrueAll();
            }
        }

        // 参照
        else if (Constants.OPERATION.VIEW.equals(operation)) {
            fieldState.setViewTrueAll();
            fieldState.setViewFalse("saveRevision");
            fieldState.setViewFalse("reasonForChange");
        }

        return fieldState;
    }

}
