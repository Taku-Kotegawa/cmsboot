package jp.co.stnet.cms.base.infrastructure.datasource.message;

import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailSendHistoryMapper {

    List<MailSendHistory> selectNewest();

}
