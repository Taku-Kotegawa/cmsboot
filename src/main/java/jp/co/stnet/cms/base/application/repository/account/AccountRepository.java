package jp.co.stnet.cms.base.application.repository.account;


import jp.co.stnet.cms.base.domain.model.authentication.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Accountのリポジトリ.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * APIキーでユーザアカウントを取得
     *
     * @param apiKey APIキー
     * @return Account
     */
    Account findByApiKey(String apiKey);

}
