package jp.co.stnet.cms.sales.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.AbstractRevisionEntity;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DocumentRevision extends AbstractRevisionEntity implements Serializable {

    // Documentエンティティとフィールドを一致させること
    // @id, @GeneratedValue は外すこと

    /**
     * 内部ID
     */
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
    private String title;

    /**
     * 本文
     */
    @Column(columnDefinition = "TEXT")
    private String body;

    /**
     * 公開区分
     */
    private String publicScope;

    /**
     * ドキュメント管理番号
     */
    private String documentNumber;

    /**
     * 版数
     */
    private String versionNumber;

    /**
     * 作成部門
     */
    private String departmentForCreation;

    /**
     * 作成担当者
     */
    private String chargePersonForCreation;

    /**
     * 発行担当者
     */
    private String chargePersonForPublish;

    /**
     * 発行部門
     */
    private String departmentForPublish;

    /**
     * 発行責任者
     */
    private String responsiblePersonForPublish;

    /**
     * 発行日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate publishedDate;

    /**
     * 改定日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate lastRevisedDate;

    /**
     * 廃止日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate invalidationDate;

    /**
     * 周知日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate announceDate;

    /**
     * 変更理由
     */
    private String reasonForChange;

    /**
     * 活用シーン
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> useStage;

//    /**
//     * 区分
//     */
//    private Long docCategory;

//    /**
//     * 区分(Variable)
//     */
//    @ManyToOne
//    @NotFound(action = NotFoundAction.IGNORE)
//    @JoinColumnsOrFormulas({
//            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_CATEGORY'", referencedColumnName = "type")),
//            @JoinColumnOrFormula(column = @JoinColumn(name = "docCategory", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
//    })
//    private Variable docCategoryVariable;

//    /**
//     * サービス
//     */
//    private Long docService;

//    /**
//     * サービス(Variable)
//     */
//    @ManyToOne
//    @NotFound(action = NotFoundAction.IGNORE)
//    @JoinColumnsOrFormulas({
//            @JoinColumnOrFormula(formula = @JoinFormula(value = "'DOC_SERVICE'", referencedColumnName = "type")),
//            @JoinColumnOrFormula(column = @JoinColumn(name = "docService", referencedColumnName = "code", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)))
//    })
//    private Variable docServiceVariable;

    /**
     * 区分1
     */
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

    /**
     * ファイル
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<File> files;

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 備考
     */
    private String remark;

    /**
     * 顧客公開区分
     */
    private String customerPublic;

}
