package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.authentication.Permission;
import jp.co.stnet.cms.common.constant.Constants;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class AdminAccountAuthority {

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
            Constants.OPERATION.LIST
    );

    /**
     * 権限チェックを行う。
     *
     * @param operation    操作の種類(Constants.OPERATIONに登録された値)
     * @param loggedInUser ログインユーザ情報
     * @return true=操作する権限を持つ, false=操作する権限なし
     * @throws AccessDeniedException    @PostAuthorizeを用いてfalse時にスロー
     * @throws IllegalArgumentException 不正なOperationが指定された場合
     * @throws NullPointerException     operation, loggedInUser がnullの場合
     */
    @PostAuthorize("returnObject == true")
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return hasAuthorityWOException(operation, loggedInUser);
    }

    private Boolean hasAuthorityWOException(String operation, LoggedInUser loggedInUser) {

        // 入力チェック
        validate(operation);

        Collection<GrantedAuthority> authorities = loggedInUser.getAuthorities();
        if (authorities == null) {
            return false;
        }

        return authorities.contains(new SimpleGrantedAuthority(Permission.ADMIN_USER.name()));

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
