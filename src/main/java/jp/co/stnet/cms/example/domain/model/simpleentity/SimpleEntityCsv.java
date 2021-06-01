package jp.co.stnet.cms.example.domain.model.simpleentity;

import lombok.Data;

@Data
public class SimpleEntityCsv {

    /**
     * 内部ID
     */
    private String id;

    /**
     * ステータス
     */
    private String status;

    /**
     * テキストフィールド
     */
    private String text01;

    /**
     * テキストフィールド(数値・整数)
     */
    private String text02;
    /**
     * テキストフィールド(数値・小数あり)
     */
    private String text03;
    /**
     * テキストフィールド(真偽値)
     */
    private String text04;
    /**
     * テキストフィールド(複数の値)
     */
    private String text05;
    /**
     * ラジオボタン(真偽値)
     */
    private String radio01;
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
    private String checkbox02;
    /**
     * テキストエリア
     */
    private String textarea01;
    /**
     * 日付
     */
    private String date01;
    /**
     * 日付時刻
     */
    private String datetime01;
    /**
     * セレクト(単一の値)
     */
    private String select01;
    /**
     * セレクト(複数の値)
     */
    private String select02;
    /**
     * セレクト(単一の値, select2)
     */
    private String select03;
    /**
     * セレクト(複数の値, select2)
     */
    private String select04;
    /**
     * コンボボックス(単一の値, Bootstrap)
     */
    private String combobox01;
    /**
     * コンボボックス(単一の値, Select2)
     */
    private String combobox02;
    /**
     * コンボボックス(複数の値, Select2)
     */
    private String combobox03;
    /**
     * 添付ファイル(FileManaged)
     */
    private String attachedFile01Uuid;

}
