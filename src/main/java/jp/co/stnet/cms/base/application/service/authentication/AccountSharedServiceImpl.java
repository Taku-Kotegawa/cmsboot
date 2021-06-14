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

import com.github.dozermapper.core.Mapper;

import jp.co.stnet.cms.base.application.repository.authentication.AccountRepository;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.application.service.filemanage.FileUploadSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.FailedAuthentication;
import jp.co.stnet.cms.base.domain.model.authentication.PasswordHistory;
import jp.co.stnet.cms.base.domain.model.authentication.SuccessfulAuthentication;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jp.co.stnet.cms.common.message.MessageKeys.*;

@Slf4j
@Service
@Transactional
public class AccountSharedServiceImpl implements AccountSharedService {

    @Autowired
    Mapper beanMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FileUploadSharedService fileUploadSharedService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    AuthenticationEventSharedService authenticationEventSharedService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordGenerator passwordGenerator;

    @Autowired
    PasswordHistorySharedService passwordHistorySharedService;

    @Autowired
    CustomDateFactory dateFactory;

    @Resource(name = "passwordGenerationRules")
    List<CharacterRule> passwordGenerationRules;

    @Value("${security.lockingDurationSeconds}")
    int lockingDurationSeconds;

    @Value("${security.lockingThreshold}")
    int lockingThreshold;

    @Value("${security.passwordLifeTimeSeconds}")
    int passwordLifeTimeSeconds;

    @Override
    public Account findOne(String username) {
        final Account account = accountRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResultMessages.error().add(E_SL_FW_5001, username)));
        if (account.getRoles() == null) {
            account.setRoles(new ArrayList<>());
        }
        return account;
    }

    @Override
    public LocalDateTime getLastLoginDate(String username) {

        List<SuccessfulAuthentication> events = authenticationEventSharedService
                .findLatestSuccessEvents(username, 1);

        if (!events.isEmpty()) {
            return events.get(0).getAuthenticationTimestamp();
        } else {
            return null;
        }
    }

    @Override
    public String create(Account account, String imageId) {
        if (account == null) {
            throw new IllegalArgumentException("account was null.");
        }

        if (exists(account.getUsername())) {
            throw new BusinessException(ResultMessages.error().add(E_SL_FW_5001));
        }
        String rawPassword = passwordGenerator.generatePassword(10, passwordGenerationRules);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setStatus(Status.INVALID.getCodeValue());
        accountRepository.save(account);

//        if (imageId != null) {
//            TempFile tempFile = fileUploadSharedService.findTempFile(imageId);
//            AccountImage image = AccountImage.builder()
//                    .username(account.getUsername())
//                    .extension(StringUtils.getFilenameExtension(tempFile.getOriginalName()))
//                    .body(tempFile.getBody()).build();
//            accountImageRepository.save(image);
//        }

        return rawPassword;
    }

    @Override
    public boolean exists(String username) {
        return accountRepository.existsById(username);
    }

    @Override
    public boolean isLocked(String username) {
        List<FailedAuthentication> failureEvents = authenticationEventSharedService
                .findLatestFailureEvents(username, lockingThreshold);

        if (failureEvents.size() < lockingThreshold) {
            return false;
        }

        return !failureEvents.get(lockingThreshold - 1)
                .getAuthenticationTimestamp()
                .isBefore(dateFactory.newLocalDateTime().minusSeconds(lockingDurationSeconds));
    }

    @Override
    @Cacheable("isInitialPassword")
    public boolean isInitialPassword(String username) {
        List<PasswordHistory> passwordHistories = passwordHistorySharedService
                .findLatest(username, 1);

        if (passwordHistories.isEmpty()) {
            return true;
        }

        // システム管理者がパスワードを変更した場合、初期パスワードと判定する
        String lastUpdatedBy = passwordHistories.get(0).getCreatedBy();
        if (username.equals(lastUpdatedBy) || "unknown".equals(lastUpdatedBy)) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    @Cacheable("isCurrentPasswordExpired")
    public boolean isCurrentPasswordExpired(String username) {
        List<PasswordHistory> passwordHistories = passwordHistorySharedService
                .findLatest(username, 1);

        if (passwordHistories.isEmpty()) {
            return true;
        }

        return passwordHistories
                .get(0)
                .getUseFrom()
                .isBefore(dateFactory.newLocalDateTime().minusSeconds(passwordLifeTimeSeconds));
    }

    @Override
    @CacheEvict(value = {"isInitialPassword", "isCurrentPasswordExpired"}, key = "#username")
    public boolean updatePassword(String username, String rawPassword) {
        String password = passwordEncoder.encode(rawPassword);

        Account account = findOne(username);
        account.setPassword(password);
        account = accountRepository.save(account);

        passwordHistorySharedService.insert(
                PasswordHistory.builder()
                .username(username)
                .password(password)
                .useFrom(dateFactory.newLocalDateTime()).build());

        return true;
    }

    @Override
    public boolean updateEmail(String username, String email) {

        Account account = findOne(username);
        account.setEmail(email);
        account = accountRepository.save(account);

        return true;
    }

    @Override
    @CacheEvict(value = {"isInitialPassword", "isCurrentPasswordExpired"}, key = "#username")
    public void clearPasswordValidationCache(String username) {
    }

    @Override
    public FileManaged getImage(String username) {
        Account account = accountRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException(ResultMessages.error().add(E_SL_FW_5001, username)));
        FileManaged fileManaged = fileManagedSharedService.findByUuid(account.getImageUuid());
        return fileManaged;
    }

}
