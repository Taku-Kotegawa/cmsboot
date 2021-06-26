package jp.co.stnet.cms.base.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * Yes/No
 */
@AllArgsConstructor
@Getter
public enum YesNo implements EnumCodeList.CodeListItem {

    YES("1", "はい"),
    NO("0", "いいえ");

    private final String value;
    private final String label;

    /**
     * valueでEnumを取得
     *
     * @param value 検索したいvalue
     * @return Enum, 指定したvalueが存在しない場合はnull.
     */
    public static YesNo getByValue(String value) {
        for (YesNo e : YesNo.values()) {
            if (e.getCodeValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return value;
    }

}
