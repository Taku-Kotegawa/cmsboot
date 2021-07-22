package jp.co.stnet.cms.example.domain.model.simpleentity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 明細データエンティティ
 */
@Embeddable
@Data
public class LineItem implements Serializable {

    /**
     * 明細番号
     */
    private Long itemNo;

    /**
     * 品名
     */
    private String itemName;

    /**
     * 単価
     */
    private Integer unitPrise;

    /**
     * 数量
     */
    private Integer itemNumber;

}
