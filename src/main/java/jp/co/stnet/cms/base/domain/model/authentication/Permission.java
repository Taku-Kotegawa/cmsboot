package jp.co.stnet.cms.base.domain.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

/**
 * パーミッション Enum
 */
@AllArgsConstructor
@Getter
public enum Permission implements EnumCodeList.CodeListItem {

    VIEW_ALL_NODE("全コンテンツの参照", "NODE"),
    VIEW_OWN_NODE("自分のコンテンツの参照", "NODE"),
    ADMIN_USER("ユーザの管理", "ADMIN"),
    ADMIN_ROLE("ロールの管理", "ADMIN"),
    ADMIN_PERMISSION("パーミッションの管理", "ADMIN");

    /**
     * ラベル(日本語)
     */
    private final String label;

    /**
     * カテゴリ
     */
    private final String category;

    @Override
    public String getCodeLabel() {
        return label;
    }

    @Override
    public String getCodeValue() {
        return name();
    }

}
