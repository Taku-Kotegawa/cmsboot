package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * ロール Enum
 */
@AllArgsConstructor
@Getter
public enum Role implements EnumCodeList.CodeListItem {

    ADMIN("管理者"),
    USER("一般ユーザ");

    private final String label;

    @Override
    public String getCodeLabel() {
        return label + "(" + name() + ")";
    }

    @Override
    public String getCodeValue() {
        return name();
    }
}
