package jp.co.stnet.cms.common.datatables;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DataTablesInputHistory extends DataTablesInput {
    /**
     * 全件表示を含むフラグ
     */
    private Boolean history;
}
