package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * メールアドレス変更要求
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EmailChangeRequest {

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
     * 暗証番号
     */
    private String secret;

    /**
     * 新メールアドレス
     */
    private String newMail;

    /**
     * 有効期限
     */
    private LocalDateTime expiryDate;
}
