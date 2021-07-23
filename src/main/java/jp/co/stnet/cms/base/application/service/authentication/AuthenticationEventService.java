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


import jp.co.stnet.cms.base.domain.model.authentication.FailedAuthentication;
import jp.co.stnet.cms.base.domain.model.authentication.SuccessfulAuthentication;

import java.util.List;

/**
 * AuthenticationEventSharedService
 */
public interface AuthenticationEventService {

    /**
     * @param username
     * @param count
     * @return
     */
    List<SuccessfulAuthentication> findLatestSuccessEvents(String username, int count);

    /**
     * @param username
     * @param count
     * @return
     */
    List<FailedAuthentication> findLatestFailureEvents(String username, int count);

    /**
     * @param username
     */
    void authenticationSuccess(String username);

    /**
     * @param username
     */
    void authenticationFailure(String username);

    /**
     * @param username
     * @return
     */
    long deleteFailureEventByUsername(String username);
}
