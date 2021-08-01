package jp.co.stnet.cms.sales.domain.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

@AllArgsConstructor
@Getter
public enum KeywordSearchTarget implements EnumCodeList.CodeListItem {

    CONTENT("content", "ファイル内"),
    BODY("bodyPlain", "本文"),
    TITLE("title", "タイトル"),
    FILENAME("fileName", "ファイル名");

    private final String value;
    private final String label;

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return name();
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
