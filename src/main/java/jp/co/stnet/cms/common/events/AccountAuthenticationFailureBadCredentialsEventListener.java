/*
 * Copyright(c) 2013 NTT Corporation.
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
package jp.co.stnet.cms.common.events;

import jp.co.stnet.cms.base.application.service.authentication.AuthenticationEventSharedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * 認証失敗を記録するイベントリスナー
 */
@Component
public class AccountAuthenticationFailureBadCredentialsEventListener {

    @Autowired
    AuthenticationEventSharedService authenticationEventSharedService;

    @EventListener(AuthenticationFailureBadCredentialsEvent.class)
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        authenticationEventSharedService.authenticationFailure(username);
    }

}
