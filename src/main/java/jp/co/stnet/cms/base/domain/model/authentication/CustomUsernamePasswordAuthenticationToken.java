package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomUsernamePasswordAuthenticationToken extends
        UsernamePasswordAuthenticationToken {

    private final Boolean loginAsAdministrator;

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Boolean loginAsAdministrator) {
        super(principal, credentials);
        this.loginAsAdministrator = loginAsAdministrator;
    }

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Boolean loginAsAdministrator, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.loginAsAdministrator = loginAsAdministrator;
    }
}
