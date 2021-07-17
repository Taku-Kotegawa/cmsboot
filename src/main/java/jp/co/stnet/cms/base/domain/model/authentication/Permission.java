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
    ADMIN_PERMISSION("パーミッションの管理", "ADMIN"),
    ADMIN_MENU("管理者メニューを開く", "ADMIN"),
    ADMIN_VARIABLE("バリアルブルの管理", "ADMIN"),
    DOC_MAN_CREATE("新規登録", "DOC"),
    DOC_MAN_UPDATE("編集", "DOC"),
    DOC_MAN_SAVE_DRAFT("下書き保存/下書取消", "DOC"),
    DOC_MAN_SAVE("保存", "DOC"),
    DOC_MAN_INVALID("無効化/無効解除", "DOC"),
    DOC_MAN_DELETE("削除", "DOC"),
    DOC_MAN_UPLOAD("アップロード", "DOC"),
    DOC_MAN_LIST("管理一覧を表示", "DOC"),
    DOC_LIST("検索一覧を表示", "DOC"),
    DOC_SEARCH("全文検索一覧を表示", "DOC"),
    DOC_VIEW_ALL("参照(社員)", "DOC"),
    DOC_VIEW_DISPATCHED_LABOR("参照(派遣)", "DOC"),
    DOC_VIEW_OUTSOURCING("参照(外部委託)", "DOC"),
    JOBLOG_ADMIN("全てのジョブログを閲覧", "DOC"),
    JOBLOG_VIEW_SELF("自分のジョブログを閲覧", "DOC");


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
