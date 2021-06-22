package jp.co.stnet.cms.example.domain.model.person;


import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * パーソンエンティティ. (Indexed)
 */
@SuppressWarnings({"LombokDataInspection", "LombokEqualsAndHashCodeInspection"})
@Entity
@Indexed
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PERSON")
public class Person extends AbstractEntity<Long> implements Serializable, StatusInterface {

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
    @FullTextField(analyzer = "japanese")
    private String name;

    /**
     * 年齢
     */
    @GenericField(aggregable = Aggregable.YES)
    private Integer age;

    /**
     * コード
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String code;

    /**
     * 添付ファイルの内容
     */
    @FullTextField(analyzer = "japanese")
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * ファイル(FileManaged)
     */
    private String attachedFile01Uuid;

    /**
     * ファイル(FileManaged). Uuidから自動的にFileManagedの値をセットする。
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "attachedFile01Uuid", referencedColumnName = "uuid", unique=true, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
    private FileManaged attachedFile01Managed;

    @Override
    public boolean isNew() {
        return id == null;
    }

}
