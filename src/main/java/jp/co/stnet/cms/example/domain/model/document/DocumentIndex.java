package jp.co.stnet.cms.example.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Indexed
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DocumentIndex implements Serializable, StatusInterface {

    /**
     * バージョン(排他制御用)
     */
    @Column(nullable = false)
    private Long version;

    /**
     * 作成者
     */
    @Column(nullable = false)
    private String createdBy;

    /**
     * 最終更新者
     */
    @Column(nullable = false)
    private String lastModifiedBy;

    /**
     * 作成日時
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createdDate;

    /**
     * 最終更新日時
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    /**
     * 内部ID
     */
    @Id
    private Long id;

    /**
     * ステータス
     */
    private String status;

    /**
     * 変更履歴を残す(コピー)
     */
    private boolean saveRevision;

    /**
     * タイトル
     */
    @FullTextField(analyzer = "japanese")
    private String title;

    /**
     * 本文
     */
    @FullTextField(analyzer = "japanese")
    @Column(columnDefinition = "TEXT")
    private String body;

    /**
     * 公開区分
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String publicScope;

    /**
     * 管理部門
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String chargeDepartment;

    /**
     * 管理担当者
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String chargePerson;

    /**
     * ドキュメント管理番号
     */
    @KeywordField
    private String documentNumber;

    /**
     * 制定日
     */
    @GenericField(aggregable = Aggregable.YES)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate enactmentDate;

    /**
     * 最終改定日
     */
    @GenericField(aggregable = Aggregable.YES)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate lastRevisedDate;

    /**
     * 実施日
     */
    @GenericField(aggregable = Aggregable.YES)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate implementationDate;

    /**
     * 制定箇所
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String enactmentDepartment;

    /**
     * 変更理由
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String reasonForChange;

    /**
     * 利用シーン
     */
    @GenericField(aggregable = Aggregable.YES)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> useStage;

    /**
     * 区分
     */
    @GenericField(aggregable = Aggregable.YES)
    private Long docCategory;

    /**
     * 区分(Variable)
     */
    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "docCategory", referencedColumnName = "id", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Variable docCategoryVariable;

    /**
     * サービス
     */
    private Long docService;

    /**
     * サービス(Variable)
     */
    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "docService", referencedColumnName = "id", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Variable docServiceVariable;

    /**
     * ファイル
     */
    @IndexedEmbedded
    @ElementCollection(fetch = FetchType.EAGER)
    private List<File> files;
}
