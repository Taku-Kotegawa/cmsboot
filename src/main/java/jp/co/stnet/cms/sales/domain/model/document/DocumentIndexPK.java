package jp.co.stnet.cms.sales.domain.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * DocumentIndexエンティティの主キー
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DocumentIndexPK implements Serializable {

    @GenericField(sortable = Sortable.YES)
    private Long id;

    private Integer no;

}
