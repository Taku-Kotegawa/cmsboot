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
 * ログイン認証成功の記録
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SuccessfulAuthentication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザID
     */
    private String username;

    /**
     * 成功日時
     */
    @CreatedDate
    private LocalDateTime authenticationTimestamp;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

}
