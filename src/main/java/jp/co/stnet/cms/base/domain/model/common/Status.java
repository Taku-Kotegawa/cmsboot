package jp.co.stnet.cms.base.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * ステータス.
 */
@AllArgsConstructor
@Getter
public enum Status implements EnumCodeList.CodeListItem {

    DRAFT("0", "下書き"),
    VALID("1", "有効"),
    INVALID("2", "無効");

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
    public static Status getByValue(String value) {
        for (Status status : Status.values()) {
            if (status.getCodeValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

}
