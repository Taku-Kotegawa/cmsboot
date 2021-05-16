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


import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.application.service.filemanage.FileUploadSharedService;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * アップロードされたファイルでステータスが"一時保存"のものを一定期間経過後に削除するクラス。
 * <br>
 * security.tempFileCleanupSecondsに設定した時間経過したファイルを削除する。
 * <br><br>
 * (前提条件)
 * <ul>
 *     <li>スケジューラーから定期的に呼び出される</li>
 *     <li>プロパティ「security.tempFileCleanupSeconds」が設定されている。</li>
 * </ul>
 * <br>
 * (スケジュールの設定例: applicationContext.xmlに指定)
 * <pre>{@code
 *     <bean id="tempFileCleaner"
 *         class="jp.co.stnet.cms.domain.common.scheduled.TempFileCleaner" />
 *     <bean id="tempFileCleanTrigger" class="org.springframework.scheduling.support.PeriodicTrigger">
 *         <constructor-arg name="period" value="${security.tempFileCleanupSeconds}" />
 *         <constructor-arg name="timeUnit" value="SECONDS" />
 *     </bean>
 *     <task:scheduler id="tempFileTaskScheduler" />
 *     <task:scheduled-tasks scheduler="tempFileTaskScheduler">
 *         <task:scheduled ref="tempFileCleaner" method="cleanup" trigger="tempFileCleanTrigger" />
 *     </task:scheduled-tasks>
 * }</pre>
 * <br>
 */
public class TempFileCleaner {

    @Autowired
    CustomDateFactory dateFactory;

    @Value("${security.tempFileCleanupSeconds}")
    int cleanupInterval;

    @Autowired
    FileUploadSharedService fileUploadSharedService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    public void cleanup() {
//        fileUploadSharedService.cleanUp(LocalDateTime.now().minusSeconds(cleanupInterval));
//        fileManagedSharedService.cleanup(LocalDateTime.now().minusSeconds(cleanupInterval));
        fileUploadSharedService.cleanUp(dateFactory.newLocalDateTime().minusSeconds(cleanupInterval));
        fileManagedSharedService.cleanup(dateFactory.newLocalDateTime().minusSeconds(cleanupInterval));
    }
}
