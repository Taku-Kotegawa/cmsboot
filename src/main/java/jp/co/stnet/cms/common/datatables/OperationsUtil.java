package jp.co.stnet.cms.common.datatables;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * DataTables一覧画面の操作ボタンをレンダリングするためのユーティリティ
 */
@Data
public class OperationsUtil {

    private String baseUrl;

    private String BUTTON_CLASS = "btn btn-button";

    // 1
    private String LABEL_CREATE = "新規作成";
    // 2
    private String LABEL_VIEW = "参照";
    // 3
    private String LABEL_EDIT = "編集";
    // 4
    private String LABEL_DELETE = "削除";
    // 5
    private String LABEL_COPY = "複製";
    // 6
    private String LABEL_LIST = "一覧に戻る";
    // 7
    private String LABEL_UNLOCK = "ロック解除";
    // 8
    private String LABEL_SAVE_DRAFT = "下書き保存";
    // 9
    private String LABEL_CANCEL_DRAFT = "下書き取消";
    // 10
    private String LABEL_INVALID = "無効化";
    // 11
    private String LABEL_VALID = "無効解除";
    // 12
    private String LABEL_DOWNLOAD = "ダウンロード";
    // 13
    private String LABEL_SWITCH_USER = "スイッチ";
    // 14
    private String LABEL_SAVE = "保存・有効化";

    // 1
    private String URL_CREATE = "create?form";
    // 2
    private String URL_VIEW = "{id}";
    // 3
    private String URL_EDIT = "{id}/update?form";
    // 4
    private String URL_DELETE = "{id}/delete";
    // 5
    private String URL_COPY = "create?form&copy={id}";
    // 6
    private String URL_LIST = "list";
    // 7
    private String URL_UNLOCK = "{id}/unlock";
    // 8 該当URLなし
    // 9
    private String URL_CANCEL_DRAFT = "{id}/cancel_draft";
    // 10
    private String URL_INVALID = "{id}/invalid";
    // 11
    private String URL_VALID = "{id}/valid";
    // 12
    private String URL_DOWNLOAD = "{id}/download";
    // 13
    private String URL_SWITCH_USER = "/admin/impersonate?username={id}";
    // 14
    // なし

    // ------ コンストラクタ -----------------------------------------

    /**
     * 操作ボタンのリンク先の親パスをクラス変数に格納する。<br>
     * 引数で渡された値の前後に"/"の有無を確認し、無い場合は"/"を付加する。
     *
     * @param baseUrl 操作ボタンのリンク先の親パス
     */
    public OperationsUtil(String baseUrl) {
        if (baseUrl == null) {
            baseUrl = "";
        } else {
            if (!StringUtils.startsWith(baseUrl, "/")) {
                baseUrl = "/" + baseUrl;
            }
            if (!StringUtils.endsWith(baseUrl, "/")) {
                baseUrl = baseUrl + "/";
            }
        }
        this.baseUrl = baseUrl;
    }

    // ------ URL -------------------------------------------------

    /**
     * 「新規作成」ボタンのURLを返す。
     *
     * @return URL
     */
    public String getCreateUrl() {
        return baseUrl + URL_CREATE;
    }

    /**
     * 「参照」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getViewUrl(String id) {
        return baseUrl + convId(URL_VIEW, id);
    }

    /**
     * 「編集」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getEditUrl(String id) {
        return baseUrl + convId(URL_EDIT, id);
    }

    /**
     * 「削除」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getDeleteUrl(String id) {
        return baseUrl + convId(URL_DELETE, id);
    }

    /**
     * 「複製」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getCopyUrl(String id) {
        return baseUrl + convId(URL_COPY, id);
    }

    /**
     * 「一覧に戻る」ボタンのURLを返す。
     *
     * @return URL
     */
    public String getListUrl() {
        return baseUrl + URL_LIST;
    }

    /**
     * 「ロック解除」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getUnlockUrl(String id) {
        return baseUrl + convId(URL_UNLOCK, id);
    }

    // 下書き保存は不要なので作成しない

    /**
     * 「下書き取消」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getCancelDraftUrl(String id) {
        return baseUrl + convId(URL_CANCEL_DRAFT, id);
    }

    /**
     * 「無効化」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getInvalidUrl(String id) {
        return baseUrl + convId(URL_INVALID, id);
    }

    /**
     * 「無効解除」ボタンのURLを返す。
     *
     * @param id ID
     * @return URL
     */
    public String getValidUrl(String id) {
        return baseUrl + convId(URL_VALID, id);
    }

    /**
     * 「ダウンロード」ボタンのURLを返す。
     *
     * @param uuid UUID
     * @return URL
     */
    public String getDownloadUrl(String uuid) {
        return baseUrl + convId(URL_DOWNLOAD, uuid);
    }


    // ------ Link<A> -----------------------------------------------

    /**
     * 「新規作成」ボタンのリンクのHTMLを返す。
     *
     * @return リンクのHTML
     */
    public String getCreateLink() {
        return link(getCreateUrl(), LABEL_CREATE);
    }

    /**
     * 「参照」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getViewLink(String id) {
        return link(getViewUrl(id), LABEL_VIEW);
    }

    /**
     * 「編集」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getEditLink(String id) {
        return link(getEditUrl(id), LABEL_EDIT);
    }

    /**
     * 「削除」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getDeleteLink(String id) {
        return link(getDeleteUrl(id), LABEL_DELETE);
    }

    /**
     * 「複製」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getCopyLink(String id) {
        return link(getCopyUrl(id), LABEL_COPY);
    }

    /**
     * 「一覧に戻る」ボタンのリンクのHTMLを返す。
     *
     * @return リンクのHTML
     */
    public String getListLink() {
        return link(getListUrl(), LABEL_LIST);
    }

    /**
     * 「ロック解除」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getUnlockLink(String id) {
        return link(getUnlockUrl(id), LABEL_UNLOCK);
    }

    // 「下書き保存」は省略

    /**
     * 「下書き取消」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getCancelDraftLink(String id) {
        return link(getCancelDraftUrl(id), LABEL_CANCEL_DRAFT);
    }

    /**
     * 「無効化」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getInvalidLink(String id) {
        return link(getInvalidUrl(id), LABEL_INVALID);
    }

    /**
     * 「無効解除」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getValidLink(String id) {
        return link(getValidUrl(id), LABEL_VALID);
    }

    /**
     * 「ダウンロード」ボタンのリンクのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return リンクのHTML
     */
    public String getDownloadLink(String id) {
        return link(getDownloadUrl(id), LABEL_DOWNLOAD);
    }


    // ------ Button Link<A> -----------------------------------------

    /**
     * 「新規作成」ボタンのHTMLを返す。
     *
     * @return ボタンのHTML
     */
    public String getCreateButton() {
        return link(getCreateUrl(), LABEL_CREATE, BUTTON_CLASS);
    }

    /**
     * 「参照」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getViewButton(String id) {
        return link(getViewUrl(id), LABEL_VIEW, BUTTON_CLASS);
    }

    /**
     * 「編集」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getEditButton(String id) {
        return link(getEditUrl(id), LABEL_EDIT, BUTTON_CLASS);
    }

    /**
     * 「削除」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getDeleteButton(String id) {
        return link(getDeleteUrl(id), LABEL_DELETE, BUTTON_CLASS);
    }

    /**
     * 「一覧に戻る」ボタンのHTMLを返す。
     *
     * @return ボタンのHTML
     */
    public String getListButton() {
        return link(getListUrl(), LABEL_LIST, BUTTON_CLASS);
    }

    /**
     * 「複製」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getCopyButton(String id) {
        return link(getCopyUrl(id), LABEL_COPY, BUTTON_CLASS);
    }

    /**
     * 「ロック解除」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getUnlockButton(String id) {
        return link(getUnlockUrl(id), LABEL_UNLOCK, BUTTON_CLASS);
    }

    // 「下書き保存」は省略

    /**
     * 「下書き取消」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getCancelDraftButton(String id) {
        return link(getCancelDraftUrl(id), LABEL_CANCEL_DRAFT, BUTTON_CLASS);
    }

    /**
     * 「無効化」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getInvalidButton(String id) {
        return link(getInvalidUrl(id), LABEL_INVALID, BUTTON_CLASS);
    }

    /**
     * 「無効解除」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getValidButton(String id) {
        return link(getValidUrl(id), LABEL_VALID, BUTTON_CLASS);
    }

    /**
     * 「ダウンロード」ボタンのHTMLを返す。
     *
     * @param id データを一位に特定する内部ID番号
     * @return ボタンのHTML
     */
    public String getDownloadButton(String id) {
        return link(getDownloadUrl(id), LABEL_DOWNLOAD, BUTTON_CLASS);
    }


    // ------ private function --------------------------------------
    private String convId(String template, String id) {
        return template.replace("{id}", id);
    }

    private String link(String url, String label) {
        return link(url, label, "");
    }

    private String link(String url, String label, String classVal) {
        return "<A HREF=\"" + url + "\" class=\"" + classVal + "\" >" + label + "</A>";
    }

}
