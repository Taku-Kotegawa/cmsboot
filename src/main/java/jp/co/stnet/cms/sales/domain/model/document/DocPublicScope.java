package jp.co.stnet.cms.sales.domain.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * 公開範囲
 */
@AllArgsConstructor
@Getter
public enum DocPublicScope implements EnumCodeList.CodeListItem {

    ALL("99", "全員"),
    DISPATCHED_LABOR("20", "社員/職員＋派遣社員"),
    EMPLOYEE("10", "社員/職員限定");

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
     *
     * @param value 検索したいvalue
     * @return Enum, 指定したvalueが存在しない場合はnull.
     */
    public static DocPublicScope getByValue(String value) {
        for (DocPublicScope docPublicScope : DocPublicScope.values()) {
            if (docPublicScope.getCodeValue().equals(value)) {
                return docPublicScope;
            }
        }
        return null;
    }

}
