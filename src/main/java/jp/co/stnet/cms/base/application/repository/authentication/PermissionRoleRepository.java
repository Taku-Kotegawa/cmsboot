package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.PermissionRole;
import jp.co.stnet.cms.base.domain.model.authentication.PermissionRolePK;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * PermissionRoleリポジトリ.
 */
@Repository
public interface PermissionRoleRepository extends JpaRepository<PermissionRole, PermissionRolePK> {

//    @Query(nativeQuery = true, value = "SELECT DISTINCT PERMISSIONS_PERMISSION FROM ROLE_PERMISSION WHERE ROLE_ROLE IN (:roles)")
//    List<String> findPermissions(@Param("roles") List<String> roles);

//    List<PermissionRole> findAllByPermissionAndRole(Permission permission, Role role);

    /**
     * 指定したロールのコレクションに合致するデータを検索する。
     *
     * @param roles ロールのコレクション
     * @return ヒットしたデータのリスト
     */
    List<PermissionRole> findByRoleIn(Collection<Role> roles);

}
