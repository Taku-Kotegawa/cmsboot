package jp.co.stnet.cms.example.presentation.controller.simpleentity;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Component;

@Component
public class SimpleEntityAuthority {

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
        return true;
    }


}
