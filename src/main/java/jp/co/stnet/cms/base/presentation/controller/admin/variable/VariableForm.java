package jp.co.stnet.cms.base.presentation.controller.admin.variable;

import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.validation.variable.ExistInVariable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 変数管理の編集画面のBean
 *
 * @author Automatically generated
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExistInVariable(groups = VariableForm.Create.class)
public class VariableForm implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 2601462768185636865L;

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
    private String status;


    /**
     * タイプ
     */
    @NotNull
    @ExistInCodeList(codeListId = "CL_VARIABLETYPE")
    private String type;

    /**
     * コード
     */
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]*$")
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
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date1;

    /**
     * 日付2
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date2;

    /**
     * 日付3
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date3;

    /**
     * 日付4
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate date4;

    /**
     * 日付5
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
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


    public interface Create {
    }

    public interface Update {
    }

}