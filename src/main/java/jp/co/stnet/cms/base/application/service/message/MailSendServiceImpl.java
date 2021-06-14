package jp.co.stnet.cms.base.application.service.message;

import jp.co.stnet.cms.base.application.repository.message.MailSendHistoryRepository;
import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import jp.co.stnet.cms.base.domain.model.message.Receiver;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.base.infrastructure.datasource.message.MailSendHistoryMapper;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class MailSendServiceImpl implements MailSendService {

    private static final String type = VariableType.MESSAGE_TEMPLATE.name();
    @Autowired
    VariableService variableService;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailSendHistoryRepository mailSendHistoryRepository;

    @Autowired
    MailSendHistoryMapper mailSendHistoryMapper;

    @Autowired
    CustomDateFactory dateFactory;

    @Value("${mail.from}")
    private String from;

    @Override
    public void sendMail(Collection<Receiver> receivers, String messageTemplateCode, Map<String, String> params) throws MessagingException {

        String subject = "";
        String body = "";

        List<Variable> variables = variableService.findAllByTypeAndCode(type, messageTemplateCode);
        if (!variables.isEmpty()) {
            subject = variables.get(0).getValue1();
            body = variables.get(0).getTextarea();

        }

        mailSender.send(getMimePreparators(receivers, subject, body, params));

    }

    @Override
    @Transactional(readOnly = true)
    public List<MailSendHistory> getNewest() {
        return mailSendHistoryMapper.selectNewest();
    }


    private MimeMessagePreparator[] getMimePreparators(Collection<Receiver> receivers, String subject, String body, Map<String, String> params) throws MessagingException {
        List<MimeMessagePreparator> list = new ArrayList<>();
        for (Receiver receiver : receivers) {
            list.add(getOneMimeMessagePreparator(receiver, subject, body, params));
            mailSendHistoryRepository.save(new MailSendHistory(null, dateFactory.newLocalDateTime(), subject, body, receiver, null));
        }
        return list.toArray(new MimeMessagePreparator[list.size()]);
    }


    private MimeMessagePreparator getOneMimeMessagePreparator(
            Receiver receiver, String subject, String body, Map<String, String> params) throws MessagingException {

        return new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
                helper.setFrom(from);
                helper.setTo(receiver.getEmail());
                helper.setSubject(subject);

                String text = body;

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }

                helper.setText(text, true);
            }
        };

    }


}
