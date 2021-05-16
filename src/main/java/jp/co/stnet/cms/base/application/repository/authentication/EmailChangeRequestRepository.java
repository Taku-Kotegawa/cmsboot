package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.EmailChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * EmailChangeRequestのリポジトリ.
 */
@Repository
public interface EmailChangeRequestRepository extends JpaRepository<EmailChangeRequest, String> {

    /**
     * 有効期限が指定された日時より古いのデータを削除する。
     *
     * @param date 日付
     * @return 削除した件数
     */
    long deleteByExpiryDateLessThan(LocalDateTime date);

}
