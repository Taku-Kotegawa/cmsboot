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


import jp.co.stnet.cms.base.domain.model.authentication.PasswordReissueInfo;

import java.time.LocalDateTime;

/**
 * PasswordReissueService
 */
public interface PasswordReissueService {

    /**
     * 新しいパスワード再発行メールを送信する。
     *
     * @param username ユーザ名
     * @return
     */
    String createAndSendReissueInfo(String username);

    /**
     * トークンで検索する。
     *
     * @param token 　トークン
     * @return
     */
    PasswordReissueInfo findOne(String token);

    /**
     * パスワードを変更する。
     *
     * @param username    ユーザ名
     * @param token       トークン
     * @param secret      シークレット
     * @param rawPassword 新しいパスワード(ハッシュなし)
     * @return true:成功, false:失敗
     */
    boolean resetPassword(String username, String token, String secret,
                          String rawPassword);

    /**
     * 指定した日付より古いパスワード再発行要求(passwordReissueInfo)と認証失敗の記録(failedPasswordReissue)を削除する。
     *
     * @param date 削除する日付(これ以前)
     * @return true:成功, false:失敗
     */
    boolean removeExpired(LocalDateTime date);
}
