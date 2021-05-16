package jp.co.stnet.cms.common.datatables;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * DataTables(Server-Side)からのリクエストを格納するクラス(リクエスト全体+下書きフラグ)
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DataTablesInputDraft extends DataTablesInput {

    /**
     * 下書きを含むフラグ
     */
    private Boolean draft;

}
