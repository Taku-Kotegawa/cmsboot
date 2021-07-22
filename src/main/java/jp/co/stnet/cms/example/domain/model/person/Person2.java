package jp.co.stnet.cms.example.domain.model.person;


import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * パーソンエンティティ2. (物理テーブルをPersonを共有するサンプル)
 */
@SuppressWarnings({"LombokDataInspection", "LombokEqualsAndHashCodeInspection"})
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PERSON")
public class Person2 extends AbstractEntity<Long> implements Serializable, StatusInterface {

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
     * 名前
     */
    private String name;

    /**
     * 年齢
     */
    private Integer age;

    /**
     * メモ
     */
    private String memo;

    @Override
    public boolean isNew() {
        return id == null;
    }

}
