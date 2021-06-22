package jp.co.stnet.cms.example.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.AbstractRevisionEntity;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    private String body;

    /**
     * 公開区分
     */
    private String publicScope;

    /**
     * 管理部門
     */
    private String chargeDepartment;

    /**
     * 管理担当者
     */
    private String chargePerson;

    /**
     * ドキュメント管理番号
     */
    private String documentNumber;

    /**
     * 制定日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate enactmentDate;

    /**
     * 最終改定日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate lastRevisedDate;

    /**
     * 実施日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate implementationDate;

    /**
     * 制定箇所
     */
    private String enactmentDepartment;

    /**
     * 変更理由
     */
    private String reasonForChange;

    /**
     * 利用シーン
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> useStage;

    /**
     * 区分
     */
    private Long docCategory;

    /**
     * 区分(Variable)
     */
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
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "docService", referencedColumnName = "id", unique = false, insertable = false, updatable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Variable docServiceVariable;

    /**
     * ファイル
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<File> files;

}
