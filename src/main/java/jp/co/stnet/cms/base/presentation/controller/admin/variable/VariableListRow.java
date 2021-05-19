package jp.co.stnet.cms.base.presentation.controller.admin.variable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 変数管理の一覧の行のBean
 * @author Automatically generated
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariableListRow implements Serializable {
 
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2601462768185636865L;

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

    /**
     * 内部ID
     */
    private Long id;

    /**
     * バージョン
     */
    private Long version;

    /**
     * ステータス
     */
    private String statusLabel;

    /**
     * タイプ
     */
    private String type;

    /**
     * コード
     */
    private String code;

    /**
     * 値1
     */
    private String value1;

    /**
     * 値2
     */
    private String value2;

    /**
     * 値3
     */
    private String value3;

    /**
     * 値4
     */
    private String value4;

    /**
     * 値5
     */
    private String value5;

    /**
     * 値6
     */
    private String value6;

    /**
     * 値7
     */
    private String value7;

    /**
     * 値8
     */
    private String value8;

    /**
     * 値9
     */
    private String value9;

    /**
     * 値10
     */
    private String value10;

    /**
     * 日付1
     */
    @JsonFormat(pattern="yyyy/MM/dd")
    private LocalDate date1;

    /**
     * 日付2
     */
    @JsonFormat(pattern="yyyy/MM/dd")
    private LocalDate date2;

    /**
     * 日付3
     */
    @JsonFormat(pattern="yyyy/MM/dd")
    private LocalDate date3;

    /**
     * 日付4
     */
    @JsonFormat(pattern="yyyy/MM/dd")
    private LocalDate date4;

    /**
     * 日付5
     */
    @JsonFormat(pattern="yyyy/MM/dd")
    private LocalDate date5;

    /**
     * 整数1
     */
    private Integer valint1;

    /**
     * 整数2
     */
    private Integer valint2;

    /**
     * 整数3
     */
    private Integer valint3;

    /**
     * 整数4
     */
    private Integer valint4;

    /**
     * 整数5
     */
    private Integer valint5;

    /**
     * テキストエリア
     */
    private String textarea;

    /**
     * ファイル1
     */
    private String file1Uuid;

    /**
     * ファイル1
     */
    private FileManaged file1Managed;

    /**
     * 備考
     */
    private String remark;

}