package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * パスワード変更の認証失敗の記録
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@IdClass(FailedPasswordReissuePK.class)
public class FailedPasswordReissue implements Serializable {

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

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;
}
