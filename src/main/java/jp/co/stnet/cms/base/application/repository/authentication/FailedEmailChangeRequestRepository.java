package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.FailedEmailChangeRequest;
import jp.co.stnet.cms.base.domain.model.authentication.FailedEmailChangeRequestPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * FailedEmailChangeRequestのリポジトリ.
 */
@Repository
public interface FailedEmailChangeRequestRepository extends JpaRepository<FailedEmailChangeRequest, FailedEmailChangeRequestPK> {

    /**
     * トークンで件数を数える。
     *
     * @param token トークン
     * @return ヒットした件数
     */
    long countByToken(String token);

    /**
     * 指定した日付以前のデータを削除する。
     *
     * @param attemptDate 日付
     * @return 削除した件数
     */
    long deleteByAttemptDateLessThan(LocalDateTime attemptDate);

    /**
     * トークンで削除する。
     *
     * @param token トークン
     * @return 削除した件数
     */
    long deleteByToken(String token);

}
