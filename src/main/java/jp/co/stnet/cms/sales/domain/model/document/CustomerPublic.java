package jp.co.stnet.cms.sales.domain.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * 顧客公開
 */
@AllArgsConstructor
@Getter
public enum CustomerPublic implements EnumCodeList.CodeListItem {

    OPEN("1", "公開"),
    CLOSE("0", "非公開");

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
    public static CustomerPublic getByValue(String value) {
        for (CustomerPublic customerPublic : CustomerPublic.values()) {
            if (customerPublic.getCodeValue().equals(value)) {
                return customerPublic;
            }
        }
        return null;
    }


}
