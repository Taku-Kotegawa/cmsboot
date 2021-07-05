package jp.co.stnet.cms.sales.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.authentication.FailedPasswordReissuePK;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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
@IdClass(DocumentIndexPK.class)
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
    @DocumentId
    private Long id;

    @Id
    @DocumentId
    private Integer no;

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
     * 活用シーン
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
//    @JoinColumn(name = "docCategory", referencedColumnName = "id", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_CATEGORY'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docCategory", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
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
//    @JoinColumn(name = "docService", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_SERVICE'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docService", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
    private Variable docServiceVariable;

//    /**
//     * ファイル
//     */
//    @IndexedEmbedded
//    @ElementCollection(fetch = FetchType.EAGER)
//    private List<File> files;


    private String type;

    private String fileUuid;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "fileUuid", referencedColumnName = "uuid", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
    private FileManaged fileManaged;

    private String pdfUuid;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "pdfUuid", referencedColumnName = "uuid", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FileManaged pdfManaged;

    @FullTextField(analyzer = "japanese")
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 概要
     */
    private String summary;

    /**
     * 顧客公開区分
     */
    private String customerPublic;

}
