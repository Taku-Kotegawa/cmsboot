package jp.co.stnet.cms.example.domain.model.select;


import jp.co.stnet.cms.base.domain.model.AbstractRevisionEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * SelectRevisionエンティティ.(複数の値をフィールドをStringに格納するサンプル)
 */
@SuppressWarnings({"LombokDataInspection", "LombokEqualsAndHashCodeInspection"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class SelectRevision extends AbstractRevisionEntity implements Serializable {

    /**
     * 内部ID
     */
    @Column(nullable = false)
    private Long id;

    /**
     * ステータス
     */
    @Column(nullable = false)
    private String status;

    /**
     * 文字列型
     */
    @NotNull
    private String select01;

    /**
     * 整数型
     */
    @NotNull
    private String select02;

}
