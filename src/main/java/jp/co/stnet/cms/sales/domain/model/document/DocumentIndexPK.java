package jp.co.stnet.cms.sales.domain.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Long id;

    private Integer no;

}
