package jp.co.stnet.cms.base.presentation.controller.admin.variable;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 変数管理のCSVファイルのBean
 */
@Data
@CsvEntity
public class VariableCsvBean implements Serializable {

    /**
     * 内部ID
     */
    @CsvColumn(name = "id", position = 0)
    private Long id;

    /**
     * バージョン
     */
    @CsvColumn(name = "version", position = 1)
    private Long version;

    /**
     * ステータス
     */
    @CsvColumn(name = "status", position = 2)
    private String status;

    /**
     * ステータス
     */
    @CsvColumn(name = "statusLabel", position = 3)
    private String statusLabel;

    /**
     * タイプ
     */
    @CsvColumn(name = "type", position = 4)
    @NotNull
    private String type;

    /**
     * コード
     */
    @CsvColumn(name = "code", position = 5)
    @NotNull
    private String code;

    /**
     * 値1
     */
    @CsvColumn(name = "value1", position = 6)
    private String value1;

    /**
     * 値2
     */
    @CsvColumn(name = "value2", position = 7)
    private String value2;

    /**
     * 値3
     */
    @CsvColumn(name = "value3", position = 8)
    private String value3;

    /**
     * 値4
     */
    @CsvColumn(name = "value4", position = 9)
    private String value4;

    /**
     * 値5
     */
    @CsvColumn(name = "value5", position = 10)
    private String value5;

    /**
     * 値6
     */
    @CsvColumn(name = "value6", position = 11)
    private String value6;

    /**
     * 値7
     */
    @CsvColumn(name = "value7", position = 12)
    private String value7;

    /**
     * 値8
     */
    @CsvColumn(name = "value8", position = 13)
    private String value8;

    /**
     * 値9
     */
    @CsvColumn(name = "value9", position = 14)
    private String value9;

    /**
     * 値10
     */
    @CsvColumn(name = "value10", position = 15)
    private String value10;

    /**
     * 日付1
     */
    @CsvColumn(name = "date1", format = "yyyy/MM/dd", position = 16)
    private Date date1;

    /**
     * 日付2
     */
    @CsvColumn(name = "date2", format = "yyyy/MM/dd", position = 17)
    private Date date2;

    /**
     * 日付3
     */
    @CsvColumn(name = "date3", format = "yyyy/MM/dd", position = 18)
    private Date date3;

    /**
     * 日付4
     */
    @CsvColumn(name = "date4", format = "yyyy/MM/dd", position = 19)
    private Date date4;

    /**
     * 日付5
     */
    @CsvColumn(name = "date5", format = "yyyy/MM/dd", position = 20)
    private Date date5;

    /**
     * 整数1
     */
    @CsvColumn(name = "valint1", position = 21)
    private Integer valint1;

    /**
     * 整数2
     */
    @CsvColumn(name = "valint2", position = 22)
    private Integer valint2;

    /**
     * 整数3
     */
    @CsvColumn(name = "valint3", position = 23)
    private Integer valint3;

    /**
     * 整数4
     */
    @CsvColumn(name = "valint4", position = 24)
    private Integer valint4;

    /**
     * 整数5
     */
    @CsvColumn(name = "valint5", position = 25)
    private Integer valint5;

    /**
     * テキストエリア
     */
    @CsvColumn(name = "textarea", position = 26)
    private String textarea;

    /**
     * ファイル1
     */
    @CsvColumn(name = "file1Uuid", position = 27)
    private String file1Uuid;

    /**
     * 備考
     */
    @CsvColumn(name = "remark", position = 28)
    private String remark;

}