package jp.co.stnet.cms.base.application.service.permission;

import jp.co.stnet.cms.base.domain.model.authentication.PermissionRole;

import java.util.List;
import java.util.Map;

/**
 * PermissionService
 */
public interface PermissionService {

    /**
     *　全件取得する。
     *
     * @return マップ
     */
    Map<String, Map<String, Boolean>> findAllMap();

    /**
     * 全件削除する。
     */
    void deleteAll();

    /**
     * 全件保存する。
     *
     * @param map マップ
     * @return
     */
    List<PermissionRole> saveAll(Map<String, Map<String, Boolean>> map);

}
