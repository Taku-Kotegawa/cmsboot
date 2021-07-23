package jp.co.stnet.cms.sales.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.IdentifierBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @GenericField(aggregable = Aggregable.YES, sortable = Sortable.YES)
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @EmbeddedId
    @DocumentId(
            identifierBridge = @IdentifierBridgeRef(type = DocumentIndexPKBridge.class)
    )
    private DocumentIndexPK pk;

    /**
     * 内部ID
     */
//    private Long id;

    /**
     *
     */
//    private Integer no;

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
    private String body;

    /**
     * 本文(HTMLタグ除去)
     */
    @FullTextField(analyzer = "japanese")
    @Column(columnDefinition = "TEXT")
    private String bodyPlain;

    /**
     * 公開区分
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String publicScope;

    /**
     * ドキュメント管理番号
     */
    @KeywordField
    private String documentNumber;

    /**
     * 版数
     */
    @KeywordField
    private String versionNumber;

    /**
     * 作成部門
     */
    @KeywordField
    private String departmentForCreation;

    /**
     * 作成担当者
     */
    @KeywordField
    private String chargePersonForCreation;

    /**
     * 発行担当者
     */
    @KeywordField
    private String chargePersonForPublish;

    /**
     * 発行部門
     */
    @KeywordField
    private String departmentForPublish;

    /**
     * 発行責任者
     */
    @KeywordField
    private String responsiblePersonForPublish;

    /**
     * 発行日
     */
    @GenericField
    private LocalDate publishedDate;

    /**
     * 改定日
     */
    @GenericField
    private LocalDate lastRevisedDate;

    /**
     * 廃止日
     */
    @GenericField
    private LocalDate invalidationDate;

    /**
     * 周知日
     */
    @GenericField
    private LocalDate announceDate;

    /**
     * 変更理由
     */
    @KeywordField
    private String reasonForChange;

    /**
     * 活用シーン
     */
    @GenericField(aggregable = Aggregable.YES)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> useStage;

    /**
     * 区分1
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String docCategory1;

    /**
     * 区分1(Variable)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_CATEGORY1'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docCategory1", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
    private Variable docCategoryVariable1;

    /**
     * 区分2
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String docCategory2;

    /**
     * 区分2(Variable)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_CATEGORY2'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docCategory2", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
    private Variable docCategoryVariable2;

    /**
     * サービス-事業領域
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String docService1;

    /**
     * サービス-事業領域(Variable)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_SERVICE1'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docService1", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
    private Variable docServiceVariable1;

    /**
     * サービス-サービス種別
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String docService2;

    /**
     * サービス-サービス種別(Variable)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_SERVICE1'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docService2", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
    private Variable docServiceVariable2;

    /**
     * サービス-サービス
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String docService3;

    /**
     * サービス-サービス(Variable)
     */
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_SERVICE3'", referencedColumnName = "type")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "docService3", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
    })
    private Variable docServiceVariable3;

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

    private String fileMemo;

    @FullTextField(analyzer = "japanese")
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    /**
     * 備考
     */
    @FullTextField(analyzer = "japanese")
    private String remark;

    /**
     * 顧客公開区分
     */
    @KeywordField(aggregable = Aggregable.YES)
    private String customerPublic;

}
