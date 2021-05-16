package jp.co.stnet.cms.base.application.service.authentication;

import jp.co.stnet.cms.base.application.repository.authentication.EmailChangeRequestRepository;
import jp.co.stnet.cms.base.application.repository.authentication.FailedEmailChangeRequestRepository;
import jp.co.stnet.cms.base.domain.model.authentication.EmailChangeRequest;
import jp.co.stnet.cms.base.domain.model.authentication.FailedEmailChangeRequest;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jp.co.stnet.cms.common.message.MessageKeys.*;

@Service
@Transactional
public class EmailChangeServiceImpl implements EmailChangeService {

    @Autowired
    PasswordGenerator passwordGenerator;

    @Resource(name = "passwordGenerationRules")
    List<CharacterRule> passwordGenerationRules;

    @Value("${security.tokenLifeTimeSeconds}")
    int tokenLifeTimeSeconds;

    @Value("${mail.from}")
    String mailFrom;

    @Value("${security.tokenValidityThreshold}")
    int tokenValidityThreshold;

    @Autowired
    EmailChangeRequestRepository emailChangeRequestRepository;

    @Autowired
    FailedEmailChangeRequestRepository failedEmailChangeRequestRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    CustomDateFactory dateFactory;

    @Override
    public String createAndSendMailChangeRequest(String username, String mail) {

        // 暗証番号を生成
        String rowSecret = passwordGenerator.generatePassword(10, passwordGenerationRules);

        // トークンを生成
        String token = UUID.randomUUID().toString();

        // 暗証番号の有効期限
        LocalDateTime expiryDate = dateFactory.newLocalDateTime().plusSeconds(tokenLifeTimeSeconds);

        // 新しいメールアドレスと暗証番号を記録
        EmailChangeRequest emailChangeRequest = emailChangeRequestRepository.save(
                EmailChangeRequest.builder()
                        .token(token)
                        .username(username)
                        .secret(rowSecret)
                        .newMail(mail)
                        .expiryDate(expiryDate)
                        .build()
        );

        // 暗証番号を新しいメールアドレスに送信
        sendMail(mail, rowSecret);

        // トークンを返す
        return token;

    }

    private void sendMail(String to, String secret) {

        mailSender.send(new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                        StandardCharsets.UTF_8.name()); // (3)
                helper.setFrom(mailFrom); // (4)
                helper.setTo(to); // (5)
                helper.setSubject("メールアドレス変更の確認"); // (6)
                String text = "暗証番号: {secret}";
                text = text.replace("{secret}", secret);
                helper.setText(text, false); // (7)
            }
        });

    }

    @Override
    @Transactional(readOnly = true)
    public EmailChangeRequest findOne(String token) {

        // トークンの存在チェック
        EmailChangeRequest emailChangeRequest = emailChangeRequestRepository.findById(token)
                .orElseThrow(() -> new ResourceNotFoundException(ResultMessages.error().add(E_SL_MC_5002, token)));

        // 有効期限チェック
        if (dateFactory.newLocalDateTime().isAfter(emailChangeRequest.getExpiryDate())) {
            throw new BusinessException(ResultMessages.error().add(E_SL_MC_5004, token));
        }

        // 失敗回数チェック
        long count = failedEmailChangeRequestRepository.countByToken(token);
        if (count >= tokenValidityThreshold) {
            throw new BusinessException(ResultMessages.error().add(E_SL_PR_5004));
        }

        return emailChangeRequest;

    }

    @Override
    public void changeEmail(String token, String secret) {

        EmailChangeRequest emailChangeRequest = findOne(token);

        if (!Objects.equals(emailChangeRequest.getSecret(), secret)) {
            fail(token);
            throw new BusinessException(ResultMessages.error().add(E_SL_MC_5003));
        }
        failedEmailChangeRequestRepository.deleteByToken(token);
        emailChangeRequestRepository.deleteById(token);
        accountSharedService.updateEmail(emailChangeRequest.getUsername(), emailChangeRequest.getNewMail());
    }

    @Override
    public void removeExpired(LocalDateTime date) {

        // DELETE FROM failedPasswordReissue WHERE expiry_date < #{date}
        failedEmailChangeRequestRepository.deleteByAttemptDateLessThan(date);

        // DELETE FROM passwordReissueInfo WHERE expiry_date < #{date}
        emailChangeRequestRepository.deleteByExpiryDateLessThan(date);

    }

    @Override
    public FailedEmailChangeRequest fail(String token) {
        return failedEmailChangeRequestRepository.save(
                FailedEmailChangeRequest.builder()
                        .token(token)
                        .attemptDate(dateFactory.newLocalDateTime())
                        .build()
        );
    }
}
