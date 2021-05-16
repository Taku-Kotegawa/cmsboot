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
package jp.co.stnet.cms.base.application.scheduled;


import jp.co.stnet.cms.base.application.service.authentication.PasswordReissueService;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 不要になったパスワード再発行要求の削除処理クラス.
 * <br><br>
 * (前提条件)
 * <ul>
 *     <li>スケジューラーから定期的に呼び出される</li>
 * </ul>
 * <br>
 * (スケジュールの設定例: applicationContext.xmlに指定)
 * <pre>{@code
 *     <bean id="expiredReissueInfoCleaner"
 *         class="jp.co.stnet.cms.domain.common.scheduled.UnnecessaryReissueInfoCleaner" />
 *     <bean id="expiredReissueInfoCleanTrigger" class="org.springframework.scheduling.support.PeriodicTrigger">
 *         <constructor-arg name="period" value="${security.reissueInfoCleanupSeconds}" />
 *         <constructor-arg name="timeUnit" value="SECONDS" />
 *     </bean>
 *     <task:scheduler id="reissueInfoCleanupTaskScheduler" />
 *
 *     <task:scheduled-tasks scheduler="reissueInfoCleanupTaskScheduler">
 *         <task:scheduled ref="expiredReissueInfoCleaner" method="cleanup"
 *             trigger="expiredReissueInfoCleanTrigger" />
 *     </task:scheduled-tasks>
 * }</pre>
 * <br>
 */
public class UnnecessaryReissueInfoCleaner {

    @Autowired
    CustomDateFactory dateFactory;

    @Autowired
    PasswordReissueService passwordReissueService;

    /**
     * 不要なパスワード再発行要求の削除
     */
    public void cleanup() {
        passwordReissueService.removeExpired(dateFactory.newLocalDateTime());
    }

}
