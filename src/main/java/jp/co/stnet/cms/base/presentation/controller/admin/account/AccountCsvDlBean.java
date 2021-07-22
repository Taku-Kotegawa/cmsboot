package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import lombok.Data;

@Data
@CsvEntity
public class AccountCsvDlBean {

    /**
     * ユーザID
     */
    @CsvColumn(name = "username", position = 0)
    private String username;

    /**
     * 名
     */
    @CsvColumn(name = "firstName", position = 1)
    private String firstName;

    /**
     * 姓
     */
    @CsvColumn(name = "lastName", position = 2)
    private String lastName;

    /**
     * 所属
     */
    @CsvColumn(name = "department", position = 3)
    private String department;

    /**
     * メールアドレス
     */
    @CsvColumn(name = "email", position = 4)
    private String email;

    /**
     * URL
     */
    @CsvColumn(name = "url", position = 5)
    private String url;

    /**
     * プロフィール
     */
    @CsvColumn(name = "profile", position = 6)
    private String profile;

    /**
     * ロール
     */
    @CsvColumn(name = "roles", position = 7)
    private String roles;

    /**
     * ステータス
     */
    @CsvColumn(name = "status", position = 8)
    private String status;

    /**
     * ステータス(ラベル)
     */
    @CsvColumn(name = "statusLabel", position = 9)
    private String statusLabel;

    /**
     * 画像UUID
     */
    @CsvColumn(name = "imageUuid", position = 10)
    private String imageUuid;

    /**
     * API KEY
     */
    @CsvColumn(name = "apiKey", position = 11)
    private String apiKey;

    /**
     * ログイン許可IPアドレス
     */
    @CsvColumn(name = "allowedIp", position = 12)
    private String allowedIp;
}
