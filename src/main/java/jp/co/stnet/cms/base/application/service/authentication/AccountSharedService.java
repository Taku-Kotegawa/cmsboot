/*
 * Copyright(c) 2013 NTT DATA Corporation. Copyright(c) 2013 NTT Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.stnet.cms.base.application.service.authentication;



import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;

import java.time.LocalDateTime;

/**
 * AccountSharedService
 */
public interface AccountSharedService {

    /**
     * キーでアカウントエンティティを取得する。
     *
     * @param username ユーザ名
     * @return アカウント・ロールエンティティ
     */
    Account findOne(String username);

    /**
     * 最終ログイン日時を取得する。
     *
     * @param username ユーザ名
     * @return 最終ログイン日時
     */
    LocalDateTime getLastLoginDate(String username);

    /**
     * 新規アカウントを登録する。(事前にアカウント画像の登録が必要)
     * 初期パスワードを設定し、返す。
     *
     * @param account アカウント・ロールエンティティ
     * @param imageId 画像ファイルを示す一時ファイルの内部番号
     * @return 初期パスワード
     */
    String create(Account account, String imageId);

    /**
     * ユーザアカウントの存在チェック
     *
     * @param username ユーザ名
     * @return true:存在する, false:存在しない
     */
    boolean exists(String username);

    /**
     * ユーザのロックを確認する。
     *
     * @param username ユーザ名
     * @return true:ロックしている, false:ロックしていない
     */
    boolean isLocked(String username);

    /**
     * パスワードが初期状態かどうか。
     *
     * @param username ユーザ名
     * @return true:初期状態, false:変更しれている
     */
    boolean isInitialPassword(String username);

    /**
     * パスワードの有効期限が切れているかどうか。
     *
     * @param username ユーザ名
     * @return true:有効期限が切れている, false:切れていない
     */
    boolean isCurrentPasswordExpired(String username);

    /**
     * パスワードを設定する。
     *
     * @param username    ユーザ名
     * @param rawPassword パスワード(ハッシュ化前)
     * @return true:パスワードが変更できた, false:変更できなかった
     */
    boolean updatePassword(String username, String rawPassword);

    /**
     * メールアドレスを設定する。
     *
     * @param username ユーザ名
     * @param email    メールアドレス
     * @return true:メールアドレスが変更できた, false:変更できなかった
     */
    boolean updateEmail(String username, String email);

    /**
     * キャッシュをクリアする。
     *
     * @param username
     */
    void clearPasswordValidationCache(String username);

    /**
     * 画像データを取得する。
     *
     * @param username ユーザ名
     * @return 画像データ
     */
    FileManaged getImage(String username);

}
