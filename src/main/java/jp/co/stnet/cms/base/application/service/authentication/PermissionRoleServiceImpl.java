package jp.co.stnet.cms.base.application.service.authentication;


import jp.co.stnet.cms.base.application.repository.authentication.PermissionRoleRepository;
import jp.co.stnet.cms.base.domain.model.authentication.PermissionRole;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PermissionRoleServiceImpl implements PermissionRoleService {

    @Autowired
    PermissionRoleRepository permissionRoleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PermissionRole> findAllByRole(Collection<String> roleIds) {

        Collection<Role> roleList = new ArrayList<>();
        for (String roleId : roleIds) {
            roleList.add(Role.valueOf(roleId));
        }

        return permissionRoleRepository.findByRoleIn(roleList);
    }

}
