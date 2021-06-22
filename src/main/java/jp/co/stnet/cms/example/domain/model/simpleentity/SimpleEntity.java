package jp.co.stnet.cms.example.domain.model.simpleentity;


import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

/**
 * サンプルエンティティ
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
public class SimpleEntity extends AbstractEntity<Long> implements Serializable, StatusInterface {

    // SimpleEntityRevisionとフィールドを一致させること

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
     * テキストフィールド
     */
    private String text01;

    /**
     * テキストフィールド(数値・整数)
     */
    private Integer text02;

    /**
     * テキストフィールド(数値・小数あり)
     */
    private Float text03;

    /**
     * テキストフィールド(真偽値)
     */
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(columnDefinition="smallint")
    private Boolean text04;

    /**
     * テキストフィールド(複数の値)
     */
    @ElementCollection
    private List<String> text05;

    /**
     * ラジオボタン(真偽値)
     */
    private Boolean radio01;

    /**
     * ラジオボタン(文字列)
     */
    private String radio02;

    /**
     * チェックボックス(文字列)
     */
    private String checkbox01;

    /**
     * チェックボックス(複数の値)
     */
    @ElementCollection
    private List<String> checkbox02;

    /**
     * テキストエリア
     */
    private String textarea01;

    /**
     * 日付
     */
    private LocalDate date01;

    /**
     * 日付時刻
     */
    private LocalDateTime datetime01;

    /**
     * セレクト(単一の値)
     */
    private String select01;

    /**
     * セレクト(複数の値)
     */
    @ElementCollection
    private List<String> select02;

    /**
     * セレクト(単一の値, select2)
     */
    private String select03;

    /**
     * セレクト(複数の値, select2)
     */
    @ElementCollection
    private List<String> select04;

    /**
     * コンボボックス(単一の値, Bootstrap)
     */
    private String combobox01;

    /**
     * コンボボックス(単一の値, Select2)
     */
    private String combobox02;

    /**
     * コンボボックス(複数の値, Select2)
     */
    @ElementCollection
    private List<String> combobox03;

    /**
     * 添付ファイル(FileManaged)
     */
    private String attachedFile01Uuid;

    /**
     * 添付ファイル(FileManaged)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "attachedFile01Uuid", referencedColumnName = "uuid", unique = true, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FileManaged attachedFile01Managed;

    /**
     * 明細行
     */
    @ElementCollection
    private Collection<LineItem> lineItems;

}
