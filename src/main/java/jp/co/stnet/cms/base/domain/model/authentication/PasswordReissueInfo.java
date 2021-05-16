package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * パスワード変更要求
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PasswordReissueInfo implements Serializable {
    /**
     * トークン
     */
    @Id
    private String token;

    /**
     * ユーザID
     */
    private String username;

    /**
     * 秘密情報
     */
    private String secret;

    /**
     * 有効期限
     */
    private LocalDateTime expiryDate;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;
}
