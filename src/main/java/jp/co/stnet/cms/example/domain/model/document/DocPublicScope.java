package jp.co.stnet.cms.example.domain.model.document;

import jp.co.stnet.cms.base.domain.model.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * 公開範囲
 */
@AllArgsConstructor
@Getter
public enum DocPublicScope implements EnumCodeList.CodeListItem {

    ALL("99", "全員(外部委託含)"),
    EMPLOYEE("10", "社員(職員)"),
    DISPATCHED_LABOR("20", "社員(職員)・派遣社員");

    private final String value;
    private final String label;

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return value;
    }

    /**
     * valueでEnumを取得
     * @param value 検索したいvalue
     * @return Enum, 指定したvalueが存在しない場合はnull.
     */
    public static Status getByValue(String value) {
        for (Status status : Status.values()) {
            if (status.getCodeValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

}
