package jp.co.stnet.cms.base.application.service.message;

import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import jp.co.stnet.cms.base.domain.model.message.Receiver;

import javax.mail.MessagingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MailSendService {

    void sendMail(Collection<Receiver> receivers, String messageTemplateCode, Map<String, String> params) throws MessagingException;

    List<MailSendHistory> getNewest();

}
