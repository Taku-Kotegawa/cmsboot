package jp.co.stnet.cms.base.application.service.authentication;


import jp.co.stnet.cms.base.application.repository.authentication.FailedPasswordReissueRepository;
import jp.co.stnet.cms.base.application.repository.authentication.PasswordReissueInfoRepository;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.PasswordReissueInfo;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jp.co.stnet.cms.common.message.MessageKeys.*;

@Slf4j
@Service
@Transactional
public class PasswordReissueServiceImpl implements PasswordReissueService {

    @Autowired
    PasswordReissueFailureSharedService passwordReissueFailureSharedService;

    @Autowired
    PasswordReissueMailSharedService mailSharedService;

    @Autowired
    PasswordReissueInfoRepository passwordReissueInfoRepository;

    @Autowired
    FailedPasswordReissueRepository failedPasswordReissueRepository;

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordGenerator passwordGenerator;

    @Autowired
    CustomDateFactory dateFactory;

    @Resource(name = "passwordGenerationRules")
    List<CharacterRule> passwordGenerationRules;

    @Value("${security.tokenLifeTimeSeconds}")
    int tokenLifeTimeSeconds;

    @Value("${app.host}")
    String host;

    @Value("${app.port}")
    String port;

    @Value("${app.contextPath}")
    String contextPath;

    @Value("${app.passwordReissueProtocol}")
    String protocol;

    @Value("${security.tokenValidityThreshold}")
    int tokenValidityThreshold;


    @Override
    public String createAndSendReissueInfo(String username) {

        String rowSecret = passwordGenerator.generatePassword(10, passwordGenerationRules);

        if (!accountSharedService.exists(username)) {
            return rowSecret;
        }

        Account account = accountSharedService.findOne(username);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = dateFactory.newLocalDateTime().plusSeconds(tokenLifeTimeSeconds);

        PasswordReissueInfo info = passwordReissueInfoRepository.save(
                PasswordReissueInfo.builder()
                        .username(username)
                        .token(token)
                        .secret(passwordEncoder.encode(rowSecret))
                        .expiryDate(expiryDate)
                        .build()
        );

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme(protocol).host(host).port(port).path(contextPath)
                .pathSegment("reissue").pathSegment("resetpassword")
                .queryParam("form").queryParam("token", info.getToken());
        String passwordResetUrl = uriBuilder.build().toString();

        mailSharedService.send(account.getEmail(), passwordResetUrl);
        return rowSecret;
    }

    @Override
    @Transactional(readOnly = true)
    public PasswordReissueInfo findOne(String token) {
        PasswordReissueInfo info = passwordReissueInfoRepository.findById(token)
                .orElseThrow(() -> new ResourceNotFoundException(ResultMessages.error().add(E_SL_PR_5002, token)));

        if (dateFactory.newLocalDateTime().isAfter(info.getExpiryDate())) {
            throw new BusinessException(ResultMessages.error().add(E_SL_PR_2001, token));
        }

        long count = failedPasswordReissueRepository.countByToken(token);
        if (count >= tokenValidityThreshold) {
            throw new BusinessException(ResultMessages.error().add(E_SL_PR_5004));
        }

        return info;
    }

    @Override
    public boolean resetPassword(String username, String token, String secret, String rawPassword) {
        PasswordReissueInfo info = this.findOne(token);
        if (!passwordEncoder.matches(secret, info.getSecret())) {
            passwordReissueFailureSharedService.resetFailure(username, token);
            throw new BusinessException(ResultMessages.error().add(E_SL_PR_5003));
        }
        failedPasswordReissueRepository.deleteByToken(token);
        passwordReissueInfoRepository.deleteById(token);
        return accountSharedService.updatePassword(username, rawPassword);
    }

    @Override
    public boolean removeExpired(LocalDateTime date) {

        // DELETE FROM failedPasswordReissue WHERE expiry_date < #{date}
        failedPasswordReissueRepository.deleteByAttemptDateLessThan(date);

        // DELETE FROM passwordReissueInfo WHERE expiry_date < #{date}
        passwordReissueInfoRepository.deleteByExpiryDateLessThan(date);

        return true;
    }

}
