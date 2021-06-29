package jp.co.stnet.cms.base.application.batch.variable;


import jp.co.stnet.cms.common.validation.IsDate;
import jp.co.stnet.cms.common.validation.Parseable;
import lombok.Data;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static jp.co.stnet.cms.common.validation.ParseableType.TO_INT;


@Data
public class VariableCsv {

    private String id;

    private String version;

    @Parseable(value = TO_INT)
    @Pattern(regexp = "^[0129]$")
    private String status;

    private String statusLabel;

    @ExistInCodeList(codeListId = "CL_VARIABLETYPE")
    private String type;

    @Size(max = 20)
    private String code;

    @Size(max = 255)
    private String value1;

    @Size(max = 255)
    private String value2;

    @Size(max = 255)
    private String value3;

    @Size(max = 255)
    private String value4;

    @Size(max = 255)
    private String value5;

    @Size(max = 255)
    private String value6;

    @Size(max = 255)
    private String value7;

    @Size(max = 255)
    private String value8;

    @Size(max = 255)
    private String value9;

    @Size(max = 255)
    private String value10;

    @IsDate("yyyy/MM/dd")
    private String date1;

    @IsDate("yyyy/MM/dd")
    private String date2;

    @IsDate("yyyy/MM/dd")
    private String date3;

    @IsDate("yyyy/MM/dd")
    private String date4;

    @IsDate("yyyy/MM/dd")
    private String date5;

    @Parseable(value = TO_INT)
    private String valint1;

    @Parseable(value = TO_INT)
    private String valint2;

    @Parseable(value = TO_INT)
    private String valint3;

    @Parseable(value = TO_INT)
    private String valint4;

    @Parseable(value = TO_INT)
    private String valint5;

    private String textarea;

    private String file1Uuid;

    @Size(max = 255)
    private String remark;
}
