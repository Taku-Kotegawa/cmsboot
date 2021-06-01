package jp.co.stnet.cms.base.application.service.permission;

import jp.co.stnet.cms.base.application.repository.authentication.PermissionRoleRepository;
import jp.co.stnet.cms.base.domain.model.authentication.Permission;
import jp.co.stnet.cms.base.domain.model.authentication.PermissionRole;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRoleRepository permissionRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Map<String, Boolean>> findAllMap() {

        Map<String, Map<String, Boolean>> map = new LinkedHashMap<>();
        List<PermissionRole> permissionRoleList = permissionRoleRepository.findAll();

        for (Permission permission : Permission.values()) {
            Map<String, Boolean> item = new LinkedHashMap<>();
            for (Role role : Role.values()) {
                item.put(role.name(), existPermissionRole(permissionRoleList, permission, role));
            }
            map.put(permission.name(), item);
        }

        return map;
    }

    @Override
    public void deleteAll() {
        permissionRoleRepository.deleteAll();
    }

    @Override
    public List<PermissionRole> saveAll(Map<String, Map<String, Boolean>> map) {
        List<PermissionRole> list = mapToList(map);
        return permissionRoleRepository.saveAll(list);
    }

    private List<PermissionRole> mapToList(Map<String, Map<String, Boolean>> map) {
        List<PermissionRole> list = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, Map<String, Boolean>> permissionMap : map.entrySet()) {
                for (Map.Entry<String, Boolean> roleMap : permissionMap.getValue().entrySet()) {
                    if (roleMap.getValue()) {
                        PermissionRole item = new PermissionRole();
                        item.setPermission(Permission.valueOf(permissionMap.getKey()));
                        item.setRole(Role.valueOf(roleMap.getKey()));
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    /**
     * パーミッション・ロールの組み合わせを検索する。
     *
     * @param permissionRoleList パーミッション・ロールの組み合わせ(リスト)
     * @param permission         パーミッションのコード
     * @param role               ロールのコード
     * @return true:存在する, false:存在しない
     */
    private boolean existPermissionRole(List<PermissionRole> permissionRoleList, Permission permission, Role role) {
        for (PermissionRole permissionRole : permissionRoleList) {
            if (Objects.equals(permissionRole.getPermission(), permission)
                    && Objects.equals(permissionRole.getRole(), role)) {
                return true;
            }
        }
        return false;
    }


    /**
     * category -> permission -> role
     */

}
