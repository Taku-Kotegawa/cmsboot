package jp.co.stnet.cms.sales.domain.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class DocumentHistoryBean implements Serializable {

    //datatablesのお作法
    private String DT_RowId;

    private String DT_RowClass;

    private Map<String, String> DT_RowAttr;
    /**
     * リビジョンID
     */
    private Long rid;
    /**
     * リンク用Ver
     */
    private String verLabel;
    /**
     * ドキュメントVer
     */
    private Long version;
    /**
     * 変更理由
     */
    private String reasonForChange;
    /**
     * 最終更新者ID
     */
    private String lastModifiedBy;
    /**
     * 最終更新日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime lastModifiedDate;
    /**
     * 最終更新者名
     */
    private String lastModifiedByLabel;

    @JsonProperty("DT_RowId")
    public String getDT_RowId() {
        return DT_RowId;
    }

    @JsonProperty("DT_RowClass")
    public String getDT_RowClass() {
        return DT_RowClass;
    }

    @JsonProperty("DT_RowAttr")
    public Map<String, String> getDT_RowAttr() {
        return DT_RowAttr;
    }
}
