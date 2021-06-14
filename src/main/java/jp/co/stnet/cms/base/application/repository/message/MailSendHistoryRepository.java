package jp.co.stnet.cms.base.application.repository.message;

import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSendHistoryRepository extends JpaRepository<MailSendHistory, Long> {
}
