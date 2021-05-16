package jp.co.stnet.cms.base.application.repository.authentication;


import jp.co.stnet.cms.base.domain.model.authentication.PasswordReissueInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PasswordReissueInfoリポジトリ.
 */
@Repository
public interface PasswordReissueInfoRepository extends JpaRepository<PasswordReissueInfo, String> {

    /**
     * 指定した有効期限以前のデータを検索する。
     *
     * @param date 有効期限
     * @return ヒットしたデータのリスト
     */
    List<PasswordReissueInfo> findByExpiryDateLessThan(LocalDateTime date);

    /**
     * 指定した有効期限以前のデータを削除する。
     *
     * @param date 有効期限
     * @return 削除した件数
     */
    long deleteByExpiryDateLessThan(LocalDateTime date);

}
