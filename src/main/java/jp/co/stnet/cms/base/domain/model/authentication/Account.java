package jp.co.stnet.cms.base.domain.model.authentication;

import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * ユーザアカウントエンティティ
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(columnList = "apiKey", unique = true)})
public class Account extends AbstractEntity<String> implements Serializable, StatusInterface {

    /**
     * ユーザID
     */
    @Id
    private String username;

    /**
     * パスワード
     */
    private String password;

    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 所属
     */
    private String department;

    /**
     * メールアドレス
     */
    private String email;

    /**
     * URL
     */
    private String url;

    /**
     * プロフィール
     */
    @Column(length = 1000)
    private String profile;

    /**
     * ロール
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    /**
     * ステータス
     */
    private String status;

    /**
     * 画像UUID
     */
    private String imageUuid;

    /**
     * 画像(FileManaged)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "imageUuid", referencedColumnName = "uuid", unique = true, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FileManaged imageManaged;

    /**
     * API KEY
     */
    @Column(unique = true)
    private String apiKey;

    /**
     * ログイン許可IPアドレス
     */
    private String allowedIp;

    @Override
    public String getId() {
        return username;
    }

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }


}
