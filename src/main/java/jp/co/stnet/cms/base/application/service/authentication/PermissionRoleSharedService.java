package jp.co.stnet.cms.base.application.service.authentication;

import jp.co.stnet.cms.base.domain.model.authentication.PermissionRole;

import java.util.Collection;
import java.util.List;

/**
 * PermissionRoleSharedService
 */
public interface PermissionRoleSharedService {

    /**
     * ロールのコレクションで検索する。
     *
     * @param roleIds ロールのコレクション
     * @return ヒットしたデータのリスト
     */
    List<PermissionRole> findAllByRole(Collection<String> roleIds);

}
