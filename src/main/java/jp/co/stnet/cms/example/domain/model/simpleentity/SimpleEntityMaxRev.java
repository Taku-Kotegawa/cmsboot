package jp.co.stnet.cms.example.domain.model.simpleentity;


import jp.co.stnet.cms.base.domain.model.AbstractMaxRevEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * サンプルエンティティ(MaxRev)
 */
@SuppressWarnings({"LombokDataInspection", "LombokEqualsAndHashCodeInspection"})
@Entity
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SimpleEntityMaxRev extends AbstractMaxRevEntity<Long> implements Serializable {

}
