package jp.co.stnet.cms.example.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Document extends AbstractEntity<Long> implements Serializable, StatusInterface {

    // DocumentRevisionエンティティとフィールドを一致させること

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
    @Column(nullable = false)
    private String publicScope = DocPublicScope.ALL.getValue();;

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
    private String docCategory;

    /**
     * ファイル
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<File> files;

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 概要
     */
    private String summary;

}
