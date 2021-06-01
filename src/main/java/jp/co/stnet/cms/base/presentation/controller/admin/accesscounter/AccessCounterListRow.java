package jp.co.stnet.cms.base.presentation.controller.admin.accesscounter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * アクセスカウンター管理の一覧の行のBean
 *
 * @author Automatically generated
 */
@Data
public class AccessCounterListRow implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1396585187753711980L;

    /**
     * 操作
     */
    private String operations;

    /**
     * DataTables RowID
     */
    private String DT_RowId;

    /**
     * DataTables RowClass
     */
    private String DT_RowClass;

    /**
     * DataTables RT_RowAttr
     */
    private Map<String, String> DT_RowAttr;

    /**
     * 最終更新日
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;
    /**
     * 内部ID
     */
    private Long id;
    /**
     * ステータス
     */
    private String statusLabel;
    /**
     * URL
     */
    private String url;
    /**
     * アクセス数
     */
    private Long count;

    @JsonProperty("DT_RowId")
    public String getDT_RowId() {
        return id.toString();
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