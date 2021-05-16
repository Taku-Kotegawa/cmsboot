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




import jp.co.stnet.cms.base.domain.model.authentication.PasswordHistory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PasswordHistorySharedService
 */
public interface PasswordHistorySharedService {

    /**
     * 登録する。
     *
     * @param history エンティティ
     * @return 登録した件数
     */
    int insert(PasswordHistory history);

    /**
     * 指定したユーザ名と一致し、利用開始日より新しいデータを検索する。
     *
     * @param username ユーザ名
     * @param useFrom 利用開始日
     * @return ヒットしたパスワード履歴のリスト
     */
    List<PasswordHistory> findHistoriesByUseFrom(String username, LocalDateTime useFrom);

    /**
     * 指定したユーザ名と一致して、登録した新しい方から指定した件数を検索する。
     *
     * @param username ユーザ名
     * @param limit 取得件数
     * @return ヒットしたデータのリスト
     */
    List<PasswordHistory> findLatest(String username, int limit);

}
