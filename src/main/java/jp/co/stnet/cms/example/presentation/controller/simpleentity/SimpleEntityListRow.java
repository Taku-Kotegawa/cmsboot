package jp.co.stnet.cms.example.presentation.controller.simpleentity;

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
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEntityListRow implements Serializable {

    private String operations;

    private String DT_RowId;

    private String DT_RowClass;

    private Map<String, String> DT_RowAttr;

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

    // ------------------------------------------------

    private Long id;

    /**
     * ステータス
     */
    private String statusLabel;

    /**
     * テキストフィールド
     */
    private String text01;

    /**
     * テキストフィールド(数値・整数)
     */
    private Integer text02;

    /**
     * テキストフィールド(数値・小数あり)
     */
    private Float text03;

    /**
     * テキストフィールド(真偽値)
     */
    private Boolean text04;

    /**
     * テキストフィールド(複数の値)
     */
    private Collection<String> text05;

    /**
     * ラジオボタン(真偽値)
     */
    private Boolean radio01;
    /**
     * ラジオボタン(文字列)
     */
    private String radio02;
    /**
     * チェックボックス(文字列)
     */
    private String checkbox01;
    /**
     * チェックボックス(複数の値)
     */
    private List<String> checkbox02;
    /**
     * テキストエリア
     */
    private String textarea01;
    /**
     * 日付
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate date01;
    /**
     * 日付時刻
     */
    @JsonFormat(pattern = "yyyy/MM/dd hh:mm")
    private LocalDateTime datetime01;
    /**
     * セレクト(単一の値)
     */
    private String select01;
    /**
     * セレクト(複数の値)
     */
    private List<String> select02;
    /**
     * セレクト(単一の値, select2)
     */
    private String select03;
    /**
     * セレクト(複数の値, select2)
     */
    private List<String> select04;
    /**
     * コンボボックス(単一の値, Bootstrap)
     */
    private String combobox01;
    /**
     * コンボボックス(単一の値, Select2)
     */
    private String combobox02;
    /**
     * ラジオボタン(真偽値)ラベル
     */
    private String radio01Label;

    /**
     * チェックボックス(文字列)のラベル
     */
    private String checkbox01Label;

    /**
     * チェックボックス(複数の値)ラベル
     */
    private String checkbox02Label;

    /**
     * セレクト(単一の値)ラベル
     */
    private String select01Label;

    /**
     * セレクト(複数の値)
     */
    private String select02Label;

    /**
     * セレクト(単一の値, select2)
     */
    private String select03Label;

    /**
     * セレクト(複数の値, select2)
     */
    private String select04Label;

    /**
     * コンボボックス(単一の値, Bootstrap)
     */
    private String combobox01Label;

    /**
     * コンボボックス(単一の値, Select2)
     */
    private String combobox02Label;

    /**
     * コンボボックス(複数の値, Select2)
     */
    private String combobox03Label;

    /**
     * 添付ファイル名
     */
    private String attachedFile01FileName;


    private FileManaged attachedFile01Managed;
}
