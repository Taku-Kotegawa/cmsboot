package jp.co.stnet.cms.base.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * リビジョン管理用の抽象エンティティ.
 */
@SuppressWarnings("LombokDataInspection")
@Data
@MappedSuperclass
public abstract class AbstractRevisionEntity {

    /**
     * リビジョンID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    /**
     * リビジョンタイプ(1:新規,2:更新)
     */
    @Column(nullable = false)
    private Integer revType;

    /**
     * バージョン(コピー)
     */
    @Column(nullable = false)
    private Long version;

    /**
     * 作成者(コピー)
     */
    private String createdBy;

    /**
     * 最終更新者(コピー)
     */
    private String lastModifiedBy;

    /**
     * 作成日時(コピー)
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 最終更新日時(コピー)
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

}