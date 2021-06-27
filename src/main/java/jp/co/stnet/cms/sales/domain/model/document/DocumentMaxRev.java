package jp.co.stnet.cms.sales.domain.model.document;

import jp.co.stnet.cms.base.domain.model.AbstractMaxRevEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentMaxRev extends AbstractMaxRevEntity<Long> implements Serializable {
}
