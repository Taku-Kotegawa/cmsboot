package jp.co.stnet.cms.base.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * リビジョン管理エンティティの抽象エンティティ.
 *
 * @param <ID> 主キーのクラス
 */
@SuppressWarnings("LombokDataInspection")
@Data
@MappedSuperclass
public abstract class AbstractMaxRevEntity<ID> {

    /**
     * 内部ID
     */
    @Id
    private ID id;

    /**
     * リビジョンID
     */
    @Column(nullable = false)
    private Long rid;
}
