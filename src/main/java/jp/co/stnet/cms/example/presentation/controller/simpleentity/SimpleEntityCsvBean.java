package jp.co.stnet.cms.example.presentation.controller.simpleentity;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@CsvEntity
public class SimpleEntityCsvBean implements Serializable {

    @CsvColumn(name = "ID")
    private Long id;

    /**
     * ステータス
     */
    @CsvColumn(name = "ステータス")
    private String statusLabel;

    /**
     * テキストフィールド
     */
    @CsvColumn(name = "テキスト")
    private String text01;

    /**
     * テキストフィールド(数値・整数)
     */
    @CsvColumn(name = "テキスト(数値・整数)")
    private Integer text02;

    /**
     * テキストフィールド(数値・小数あり)
     */
    @CsvColumn(name = "テキスト(数値・小数あり)")
    private Float text03;

    /**
     * テキストフィールド(真偽値)
     */
    @CsvColumn(name = "真偽値")
    private Boolean text04;

    /**
     * コレクション(文字列)
     */
    @CsvColumn(name = "複数の値")
    private String text05;

    /**
     * ラジオボタン(真偽値)
     */
    @CsvColumn(name = "ラジオボタン(真偽値)")
    private String radio01Label;

    /**
     * ラジオボタン(文字列)
     */
    @CsvColumn(name = "ラジオボタン(文字列)")
    private String radio02;

    /**
     * チェックボックス(文字列)
     */
    @CsvColumn(name = "チェックボックス(文字列)")
    private String checkbox01Label;

    /**
     * チェックボックス(複数の値)
     */
    @CsvColumn(name = "チェックボックス(複数の値)")
    private String checkbox02Label;

    /**
     * テキストエリア
     */
    @CsvColumn(name = "テキストエリア")
    private String textarea01;

    /**
     * 日付
     */
    @CsvColumn(name = "日付", format="yyyy/MM/dd")
    private Date date01;

    /**
     * 日付時刻
     */
    @CsvColumn(name = "日付時刻", format="yyyy/MM/dd HH:mm:ss")
    private Date datetime01;

    /**
     * セレクト(単一の値)
     */
    @CsvColumn(name = "セレクト(単一の値)")
    private String select01Label;

    /**
     * セレクト(複数の値)
     */
    @CsvColumn(name = "セレクト(複数の値)")
    private String select02Label;

    /**
     * セレクト(単一の値, select2)
     */
    @CsvColumn(name = "セレクト(単一の値, select2)")
    private String select03Label;

    /**
     * セレクト(複数の値, select2)
     */
    @CsvColumn(name = "セレクト(複数の値, select2)")
    private String select04Label;

    /**
     * コンボボックス(単一の値, Bootstrap)
     */
    @CsvColumn(name = "コンボボックス(単一の値, Bootstrap)")
    private String combobox01;

    /**
     * コンボボックス(単一の値, Select2)
     */
    @CsvColumn(name = "コンボボックス(単一の値, Select2)")
    private String combobox02Label;

    /**
     * コンボボックス(複数の値, Select2)
     */
    @CsvColumn(name = "コンボボックス(複数の値, Select2)")
    private String combobox03Label;

    /**
     * 添付ファイル(FileManaged UUID)
     */
    @CsvColumn(name = "添付ファイル")
    private String attachedFile01FileName;

}
