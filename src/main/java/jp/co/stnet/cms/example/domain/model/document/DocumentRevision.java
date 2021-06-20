package jp.co.stnet.cms.example.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.AbstractRevisionEntity;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
     * ファイル
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<File> files;

}
