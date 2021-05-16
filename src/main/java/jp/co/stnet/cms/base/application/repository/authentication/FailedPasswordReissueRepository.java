package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.FailedPasswordReissue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FailedPasswordReissueのリポジトリ.
 */
@Repository
public interface FailedPasswordReissueRepository extends JpaRepository<FailedPasswordReissue, String> {

    /**
     * 指定したトークンに合致するデータを取得。
     *
     * @param token トークン
     * @return FailedPasswordReissueのリスト
     */
    List<FailedPasswordReissue> findByToken(String token);

    /**
     * トークンで件数を数える。
     *
     * @param token トークン
     * @return 件数
     */
    long countByToken(String token);

    /**
     * トークンで削除する。
     *
     * @param token トークン
     * @return 削除した件数
     */
    long deleteByToken(String token);

    /**
     * 指定した日付以前のデータを削除する。
     *
     * @param attemptDate 日付
     * @return 削除した件数
     */
    long deleteByAttemptDateLessThan(LocalDateTime attemptDate);

}
