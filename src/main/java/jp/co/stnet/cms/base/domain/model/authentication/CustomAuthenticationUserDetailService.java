package jp.co.stnet.cms.base.domain.model.authentication;


import jp.co.stnet.cms.base.application.service.account.AccountService;
import jp.co.stnet.cms.base.application.service.authentication.AccountSharedService;
import jp.co.stnet.cms.base.application.service.authentication.PermissionRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationUserDetailService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    PermissionRoleService permissionRoleService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {

        // フィルタで取得したAuthorizationヘッダの値
        String credential = token.getCredentials().toString();

        credential = credential.replace("Bearer ", "");

        // 空の場合は認証エラーとする
        if (credential.isEmpty()) {
            throw new UsernameNotFoundException("Authorization header must not be empty.");
        }

        Account account = accountService.findByApiKey(credential);

        if (account == null) {
            throw new UsernameNotFoundException("Invalid authorization header.");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> roleIds = new ArrayList<>();

        for (String roleLabel : account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleLabel));
            roleIds.add(roleLabel);
        }

        for (PermissionRole permissionRole : permissionRoleService.findAllByRole(roleIds)) {
            authorities.add(new SimpleGrantedAuthority(permissionRole.getPermission().name()));
        }


        return new LoggedInUser(account,
                accountSharedService.isLocked(account.getUsername()),
                accountSharedService.getLastLoginDate(account.getUsername()),
                authorities);

    }
}