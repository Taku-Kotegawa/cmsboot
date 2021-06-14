package jp.co.stnet.cms.base.infrastructure.datasource.message;

import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MailSendHistoryMapperTest {

    @Autowired
    MailSendHistoryMapper mailSendHistoryMapper;

    @Test
    void selectNewest() {

        List<MailSendHistory> mailSendHistories = mailSendHistoryMapper.selectNewest();
        assertThat(mailSendHistories.size()).isEqualTo(3);

    }
}