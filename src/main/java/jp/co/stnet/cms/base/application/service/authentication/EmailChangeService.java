package jp.co.stnet.cms.base.application.service.authentication;

import jp.co.stnet.cms.base.domain.model.authentication.EmailChangeRequest;
import jp.co.stnet.cms.base.domain.model.authentication.FailedEmailChangeRequest;

import java.time.LocalDateTime;

/**
 * EmailChangeService
 */
public interface EmailChangeService {

    /**
     * メール変更を仮受付しトークンを発行し、エンティティを保存しつつ、メールを送る。
     *
     * @param username ユーザ名
     * @param mail     新メールアドレス
     * @return トークン
     */
    String createAndSendMailChangeRequest(String username, String mail);

    /**
     * 保存しているメール変更要求をトークンで取り出す
     *
     * @param token トークン
     * @return EmailChangeRequest
     */
    EmailChangeRequest findOne(String token);

    /**
     * ユーザアカウントのメールアドレスを変更する。(トークンチェックあり)
     *
     * @param token  トークン
     * @param secret シークレット
     * @Exception トークンが存在しない、トークンの有効期限が超過
     */
    void changeEmail(String token, String secret);

    /**
     * 有効期限を超えるメール変更要求を削除する。(スケジュール機能で定期的に実行される想定)
     *
     * @param date 現在の日付
     */
    void removeExpired(LocalDateTime date);


    /**
     * メール変更要求に対する暗証番号の認証に失敗した記録を保損する。
     *
     * @param token トークン
     * @return FailedEmailChangeRequest
     */
    FailedEmailChangeRequest fail(String token);

}
