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

    ADMIN("システム管理者"),
    USER("一般ユーザ"),
    MANAGER("社員(職員)・管理者"),
    MEMBER("社員(職員)・メンバー"),
    DISPATCHED_LABOR("派遣社員"),
    OUTSOURCING("外部委託");

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
