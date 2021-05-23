package jp.co.stnet.cms.example.presentation.controller.simpleentity;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.example.domain.model.simpleentity.LineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEntityForm implements Serializable {

    private Long id;
    private Long version;
    private String status;

    /**
     * テキストフィールド
     */
    @NotNull
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
    private List<String> text05;
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
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date01;
    /**
     * 日付時刻
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
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
     * コンボボックス(複数の値, Select2)
     */
    private List<String> combobox03;
    /**
     * 添付ファイル(FileManaged UUID)
     */
    private String attachedFile01Uuid;
    /**
     * 添付ファイル(FileManaged)
     */
    private FileManaged attachedFile01Managed;

    private List<LineItem> lineItems;

    public interface Create {
    }

    public interface Update {
    }


}
