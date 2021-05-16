package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

/**
 * メールアドレス変更の認証失敗の記録
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@IdClass(FailedEmailChangeRequestPK.class)
public class FailedEmailChangeRequest {

    /**
     * トークン
     */
    @Id
    private String token;

    /**
     * 試行日時
     */
    @Id
    private LocalDateTime attemptDate;

}
