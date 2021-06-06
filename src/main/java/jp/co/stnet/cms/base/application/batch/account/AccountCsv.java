package jp.co.stnet.cms.base.application.batch.account;

import jp.co.stnet.cms.common.validation.Parseable;
import lombok.Data;

import javax.validation.constraints.Pattern;

import static jp.co.stnet.cms.common.validation.ParseableType.TO_INT;

@Data
public class AccountCsv {

    /**
     * ユーザID
     */
    private String username;

    /**
     * パスワード
     */
    private String password;

    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 所属
     */
    private String department;

    /**
     * メールアドレス
     */
    private String email;

    /**
     * URL
     */
    private String url;

    /**
     * プロフィール
     */
    private String profile;

    /**
     * ロール
     */
    private String roles;

    /**
     * ステータス
     */
    @Parseable(value = TO_INT)
    @Pattern(regexp = "^[0129]$")
    private String status;

    private String statusLabel;

    /**
     * 画像UUID
     */
    private String imageUuid;

    /**
     * API KEY
     */
    private String apiKey;

    /**
     * ログイン許可IPアドレス
     */
    private String allowedIp;
}
