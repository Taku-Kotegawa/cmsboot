package jp.co.stnet.cms.base.domain.model.authentication;

import jp.co.stnet.cms.base.application.service.authentication.PermissionRoleSharedService;
import jp.co.stnet.cms.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.*;

public class CustomUsernamePasswordAuthenticationProvider extends DaoAuthenticationProvider {

//    @Autowired
//    AccountSharedService accountSharedService;

    @Autowired
    PermissionRoleSharedService permissionRoleSharedService;

    private boolean matches(String ip, String subnet) {
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            return true;
        }
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(subnet);
        return ipAddressMatcher.matches(ip);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

        LoggedInUser loggedInUser = (LoggedInUser) userDetails;
        Account account = loggedInUser.getAccount();

        if (StringUtils.isNotBlank(account.getAllowedIp())) {

            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            String userIp = details.getRemoteAddress();

            List<String> allowedIps = Arrays.asList(account.getAllowedIp().split(","));
            boolean userIpIsAllowed = false;

            for (String allowedIp : allowedIps) {
                if (matches(userIp, allowedIp)) {
                    userIpIsAllowed = true;
                }
            }

            if (!userIpIsAllowed) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad User IP Address"));
            }
        }


        CustomUsernamePasswordAuthenticationToken customUsernamePasswordAuthentication =
                (CustomUsernamePasswordAuthenticationToken) authentication;

        // ログイン画面の「Login As Administrator」にチェックが入っている場合、ADMINロールを保持している必要がある。
        if (customUsernamePasswordAuthentication.getLoginAsAdministrator()) {
            if (!account.getRoles().contains(Role.ADMIN.name())) {
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            }
        }

    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {

        CustomUsernamePasswordAuthenticationToken customUsernamePasswordAuthentication =
                (CustomUsernamePasswordAuthenticationToken) authentication;

        Boolean loginAsAdministrator = customUsernamePasswordAuthentication.getLoginAsAdministrator();

        LoggedInUser loggedInUser = (LoggedInUser) user;

        Account account = loggedInUser.getAccount();

        Collection<GrantedAuthority> authorities = new HashSet<>();

        List<String> roleIds = new ArrayList<>();

        for (String roleLabel : account.getRoles()) {

            // Administrator権限でログインしない場合、ADMINロールを除外
            // asAdmin=true, role=admin -> true
            // asAdmin=false, role=admin -> false
            // asAdmin=true, role=other -> true
            // asAdmin=false, role=other -> true
            if (!(!customUsernamePasswordAuthentication.getLoginAsAdministrator() && roleLabel.equals(Role.ADMIN.name()))) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleLabel));
                roleIds.add(roleLabel);
            }
        }

        for (PermissionRole permissionRole : permissionRoleSharedService.findAllByRole(roleIds)) {
            authorities.add(new SimpleGrantedAuthority(permissionRole.getPermission().name()));
        }

        principal = new LoggedInUser(account,
                !loggedInUser.isAccountNonLocked(),
                loggedInUser.getLastLoginDate(),
                authorities);

        return new CustomUsernamePasswordAuthenticationToken(principal,
                authentication.getCredentials(), loginAsAdministrator,
                authorities);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomUsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
