package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * パスワード履歴エンティティ
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PasswordHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザID
     */
    private String username;

    /**
     * 利用開始日時
     */
    @CreatedDate
    private LocalDateTime useFrom;

    /**
     * パスワード
     */
    private String password;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

}
