package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;


/**
 * ログインユーザエンティティ
 */
@Getter
public class LoggedInUser extends User {

    private final Account account;

    private final LocalDateTime lastLoginDate;

    public LoggedInUser(Account account, boolean isLocked,
                        LocalDateTime lastLoginDate,
//                        List<SimpleGrantedAuthority> authorities) {
                        Collection<? extends GrantedAuthority> authorities) {

        super(account.getUsername(), account.getPassword(),
                true, true, true,
                !isLocked, authorities);
        this.account = account;
        this.lastLoginDate = lastLoginDate;
    }

}
