package jp.co.stnet.cms.base.domain.model.authentication;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * メールアドレス変更の認証失敗の記録(主キー)
 */
public class FailedEmailChangeRequestPK implements Serializable {

    /**
     * トークン
     */
    private String token;

    /**
     * 試行日時
     */
    private LocalDateTime attemptDate;
}
