package jp.co.stnet.cms.base.application.repository.authentication;

import jp.co.stnet.cms.base.domain.model.authentication.FailedAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * FailedAuthenticationのリポジトリ.
 */
@Repository
public interface FailedAuthenticationRepository extends JpaRepository<FailedAuthentication, String> {

    /**
     * 指定したユーザ名のデータを削除する。
     *
     * @param username ユーザ名
     * @return 削除した件数
     */
    long deleteByUsername(String username);
}
