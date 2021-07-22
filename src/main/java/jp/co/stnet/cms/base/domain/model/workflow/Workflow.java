package jp.co.stnet.cms.base.domain.model.workflow;


import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {@Index(columnList = "entityType, entityId, stepNo, employeeId", unique = true)})
public class Workflow extends AbstractEntity<Long> implements Serializable {
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }

    /**
     * 内部ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * エンティティ名
     */
    @Column(nullable = false)
    private String entityType;

    /**
     * エンティティID
     */
    @Column(nullable = false)
    private Long entityId;

    /**
     * ステップNo
     */
    @Column(nullable = false)
    private int stepNo;

    /**
     * 従業員ID
     */
    @Column(nullable = false)
    private Long employeeId;

    /**
     * ステップステータス(0:未対応, 1:対応中, 2:完了, 3:差戻, 4:引戻)
     */
    @Column(nullable = false)
    private int stepStatus;

    /**
     * 並び順(同一ステップに従業員が複数割り当てられた場合)
     */
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int weight;

}
