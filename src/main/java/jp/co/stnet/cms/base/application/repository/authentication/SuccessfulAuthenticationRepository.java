package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.SuccessfulAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SuccessfulAuthenticationリポジトリ.
 */
@Repository
public interface SuccessfulAuthenticationRepository extends JpaRepository<SuccessfulAuthentication, String> {
}
