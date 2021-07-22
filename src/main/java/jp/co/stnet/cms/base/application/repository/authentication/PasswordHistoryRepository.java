package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PasswordHistoryリポジトリ.
 */
@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, String> {

    /**
     * ユーザ名で検索する。
     *
     * @param username ユーザ名
     * @return パスワード履歴のリスト
     */
    List<PasswordHistory> findByUsername(String username);

    /**
     * 指定したユーザ名と一致し、利用開始日より新しいデータを検索する。
     *
     * @param username ユーザ名
     * @param useFrom  利用開始日
     * @return ヒットしたパスワード履歴のリスト
     */
    List<PasswordHistory> findByUsernameAndUseFromAfter(String username, LocalDateTime useFrom);

}
