package jp.co.stnet.cms.base.domain.model.common;

import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * アクセスカウンタエンティティ.
 */
@SuppressWarnings({"LombokDataInspection", "LombokEqualsAndHashCodeInspection"})
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(columnList = "URL", unique = true)})
public class AccessCounter extends AbstractEntity<Long> implements Serializable, StatusInterface {

    /**
     * 内部ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ステータス
     */
    @Column(nullable = false)
    private String status;

    /**
     * url
     */
    @Column(length = 1000)
    private String url;

    /**
     * アクセス数
     */
    private Long count;

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }

}