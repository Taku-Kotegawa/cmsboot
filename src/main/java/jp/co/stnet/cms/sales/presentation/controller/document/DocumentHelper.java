package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.util.StateMap;
import jp.co.stnet.cms.sales.application.service.document.DocumentHistoryService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.groups.Default;
import java.util.*;

@Component
public class DocumentHelper {

    //　管理画面
    static final String TEMPLATE_LIST = "sales/document/list";

    //　全文検索画面
    static final String TEMPLATE_SEARCH = "sales/document/search";

    // 一覧画面
    static final String TEMPLATE_SEARCH_LIST = "sales/document/searchlist";

    // 変更履歴
    static final String TEMPLATE_HISTORY= "sales/document/history";

    //セッションとして情報を格納するURLの配列
    private final String[] urlList = {TEMPLATE_LIST, TEMPLATE_SEARCH, TEMPLATE_SEARCH_LIST, TEMPLATE_HISTORY};

    @Autowired
    DocumentHistoryService documentHistoryService;

    @Autowired
    DocumentAuthority authority;

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

    private static final String DOC_CATEGORY2 = "DOC_CATEGORY2";

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

        LoggedInUser loggedInUser = (LoggedInUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
            if (authority.hasAuthorityWOException(operation, loggedInUser, record)) {
                buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
            }
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
        List<String> includeKeys = List.of("status", "lastModifiedBy", "lastModifiedDate");

        // 常設の隠しフィールドは状態管理しない
        StateMap fieldState = new StateMap(formClass, includeKeys, excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setInputFalse("status");
            fieldState.setInputFalse("saveRevision");
            fieldState.setInputFalse("lastModifiedBy");
            fieldState.setInputFalse("lastModifiedDate");

        }

        // 編集
        else if (Constants.OPERATION.UPDATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setViewTrue("status");
            fieldState.setInputFalse("lastModifiedBy");
            fieldState.setInputFalse("lastModifiedDate");

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

    /**
     * ユーザIDからユーザ名を返す
     * ユーザ名: 姓+名
     *
     * @param userId ユーザID
     * @return ユーザ名
     */
    String getUserName(String userId) {
        return documentHistoryService.nameSearch((userId)).getFirstName() + " " + documentHistoryService.nameSearch((userId)).getLastName();
    }

    /**
     * ユーザ情報から対応した公開区分を定義する
     * 99:社員 20:派遣 10:外部委託
     *
     * @param loggedInUser ユーザ情報
     * @return 公開区分
     */
    Set<String> getPublicScopeSet(LoggedInUser loggedInUser) {
        Set<String> setScope = new HashSet<>();
        if (loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("DOC_VIEW_ALL"))) {
            Collections.addAll(setScope, "10", "20", "99");
        } else if ((loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("DOC_VIEW_DISPATCHED_LABOR")))) {
            Collections.addAll(setScope, "10", "20");
        } else if ((loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("DOC_VIEW_OUTSOURCING")))) {
            Collections.addAll(setScope, "10");
        }

        return setScope;
    }

    /**
     * 指定したURLに該当するかチェックする
     * 該当した場合: TRUE
     * 該当しない場合: FALSE
     *
     * @param url 検索対象URL
     * @return TRUE or FALSE
     */
    Boolean isReferer(String url) {
        for (String arrayList : urlList) {
            if (url.indexOf(arrayList) >= 0) {
                return true;
            }
        }
        return false;
    }

    StateMap getButtonStateMap(@NonNull String operation, Document record, DocumentForm form, LoggedInUser loggedInUser) {
        StateMap buttonState = getButtonStateMap(operation, record, form);
        if (operation.equals(Constants.OPERATION.VIEW)) {
//            if (!authority.hasAuthorityNotException(operation, loggedInUser, record)) {
//                buttonState.setViewFalse(Constants.BUTTON.GOTOUPDATE);
//            }
        }


        return buttonState;
    }
}
