package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.authentication.Permission;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.sales.domain.model.document.DocPublicScope;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class DocumentAuthority {

    // 許可されたOperation
    private static final Set<String> allowedOperation = Set.of(
            Constants.OPERATION.CREATE,
            Constants.OPERATION.UPDATE,
            Constants.OPERATION.DELETE,
            Constants.OPERATION.SAVE,
            Constants.OPERATION.SAVE_DRAFT,
            Constants.OPERATION.CANCEL_DRAFT,
            Constants.OPERATION.INVALID,
            Constants.OPERATION.VALID,
            Constants.OPERATION.UPLOAD,
            Constants.OPERATION.VIEW,
            Constants.OPERATION.LIST,
            "SEARCH_LIST",
            "SEARCH_FULLTEXT"
    );

    /**
     * 権限チェックを行う。
     *
     * @param operation    操作の種類(Constants.OPERATIONに登録された値)
     * @param loggedInUser ログインユーザ情報
     * @return true=操作する権限を持つ, false=操作する権限なし
     * @throws AccessDeniedException @PostAuthorizeを用いてfalse時にスロー
     */
    @PostAuthorize("returnObject == true")
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return hasAuthority(operation, loggedInUser, null);
    }

    /**
     * 権限チェックを行う。
     *
     * @param operation    操作の種類(Constants.OPERATIONに登録された値)
     * @param loggedInUser ログインユーザ情報
     * @param document     ドキュメントエンティティ
     * @return true=操作する権限を持つ, false=操作する権限なし
     * @throws AccessDeniedException @PostAuthorizeを用いてfalse時にスロー
     */
    @PostAuthorize("returnObject == true")
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser, Document document) {
        // 入力チェック
        validate(operation);

        Collection<GrantedAuthority> authorities = loggedInUser.getAuthorities();

        // 新規登録
        if (Constants.OPERATION.CREATE.equals(operation)) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_CREATE.name()));
        }

        // 編集画面を開く
        else if (Constants.OPERATION.UPDATE.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_UPDATE.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // 保存
        else if (Constants.OPERATION.SAVE.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_SAVE.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // 下書き保存
        else if (Constants.OPERATION.SAVE_DRAFT.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_SAVE_DRAFT.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // 下書き取消
        else if (Constants.OPERATION.CANCEL_DRAFT.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_SAVE_DRAFT.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // 無効
        else if (Constants.OPERATION.INVALID.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_INVALID.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // 無効解除
        else if (Constants.OPERATION.VALID.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_INVALID.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // 削除
        else if (Constants.OPERATION.DELETE.equals(operation)) {
            if (authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_DELETE.name()))) {
                return checkPublicScope(authorities, document);
            } else {
                return false;
            }
        }

        // アップロード
        else if (Constants.OPERATION.UPLOAD.equals(operation)) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_UPLOAD.name()));
        }

        // 管理一覧を開く
        else if (Constants.OPERATION.LIST.equals(operation)) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_MAN_LIST.name()));
        }

        // 検索一覧を開く
        else if ("SEARCH_LIST".equals(operation)) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_LIST.name()));
        }

        // 全文検索画面を開く
        else if ("SEARCH_FULLTEXT".equals(operation)) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_SEARCH.name()));
        }

        // 参照
        else if (Constants.OPERATION.VIEW.equals(operation)) {
            return checkPublicScope(authorities, document);
        }

        return false;
    }

    /**
     * 公開区分によるアクセス権をチェック
     *
     * @param authorities ログインユーザが保持する権限
     * @param document    ドキュメントエンティティ
     * @return 権限の有無(true = 権限あり, false = 権限なし)
     */
    private boolean checkPublicScope(Collection<GrantedAuthority> authorities, Document document) {

        if (document == null) {
            return false;
        }

        // 公開区分が全公開
        else if (DocPublicScope.ALL.getValue().equals(document.getPublicScope())) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_VIEW_ALL.name()))
                    || authorities.contains(new SimpleGrantedAuthority(Permission.DOC_VIEW_DISPATCHED_LABOR.name()))
                    || authorities.contains(new SimpleGrantedAuthority(Permission.DOC_VIEW_OUTSOURCING.name()));
        }

        // 公開区分が派遣社員まで
        else if (DocPublicScope.DISPATCHED_LABOR.getValue().equals(document.getPublicScope())) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_VIEW_ALL.name()))
                    || authorities.contains(new SimpleGrantedAuthority(Permission.DOC_VIEW_DISPATCHED_LABOR.name()));
        }

        // 公開区分が社員のみ
        else if (DocPublicScope.EMPLOYEE.getValue().equals(document.getPublicScope())) {
            return authorities.contains(new SimpleGrantedAuthority(Permission.DOC_VIEW_ALL.name()));
        }

        return false;
    }


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

}
