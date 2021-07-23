package jp.co.stnet.cms.base.application.service.authentication;

import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.authentication.PermissionRole;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import jp.co.stnet.cms.base.domain.model.common.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    PermissionRoleService permissionRoleService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Account account = accountSharedService.findOne(username);

            if (account == null || account.getStatus().equals(Status.INVALID.getValue())) {
                throw new UsernameNotFoundException("user not found");
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            List<String> roleIds = new ArrayList<>();

            for (String roleLabel : account.getRoles()) {

                if (!roleLabel.equals(Role.ADMIN.name())) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleLabel));
                    roleIds.add(roleLabel);
                }
            }

            for (PermissionRole permissionRole : permissionRoleService.findAllByRole(roleIds)) {
                authorities.add(new SimpleGrantedAuthority(permissionRole.getPermission().name()));
            }

            return new LoggedInUser(account,
                    accountSharedService.isLocked(username),
                    accountSharedService.getLastLoginDate(username),
                    authorities);

        } catch (ResourceNotFoundException e) {
            throw new UsernameNotFoundException("user not found", e);
        }
    }

}
