package jp.co.stnet.cms.base.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * 抽象エンティティ(履歴管理なし).
 *
 * @param <ID> 主キーのクラス
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity<ID> implements Persistable<ID> {

    /**
     * バージョン(排他制御用)
     */
    @Version
    @Column(nullable = false)
    private Long version;

    /**
     * 作成者
     */
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    /**
     * 最終更新者
     */
    @LastModifiedBy
    @Column(nullable = false)
    private String lastModifiedBy;

    /**
     * 作成日時
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 最終更新日時
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @Override
    public boolean isNew() {
        return getVersion() == null;
    }

}