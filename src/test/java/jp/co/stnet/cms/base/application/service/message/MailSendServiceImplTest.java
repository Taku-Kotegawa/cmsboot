package jp.co.stnet.cms.base.application.service.message;

import jp.co.stnet.cms.base.domain.model.message.Receiver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailSendServiceImplTest {

    @Autowired
    MailSendService target;

    @Test
    void sendMail() throws MessagingException {

        // 準備
        List<Receiver> receivers = new ArrayList<>();
        receivers.add(new Receiver("aaa", "aaa@stnet.co.jp"));
        receivers.add(new Receiver("bbb", "bbb@stnet.co.jp"));
        receivers.add(new Receiver("ccc", "ccc@stnet.co.jp"));

        String code = "WELCOME";

        Map<String,String> params = new LinkedHashMap<>();


        target.sendMail(receivers, code,params);


    }
}