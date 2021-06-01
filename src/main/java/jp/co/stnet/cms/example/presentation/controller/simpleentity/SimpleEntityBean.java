package jp.co.stnet.cms.example.presentation.controller.simpleentity;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import lombok.Data;

@Data
public class SimpleEntityBean extends SimpleEntity {

    /**
     * テキストフィールド(複数の値)のラベル
     */
    private String text05Label;

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
