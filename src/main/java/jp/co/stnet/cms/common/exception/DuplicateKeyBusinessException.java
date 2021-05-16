package jp.co.stnet.cms.common.exception;

import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 一意制約違反を表すビジネス例外
 */
public class DuplicateKeyBusinessException extends BusinessException {
    public DuplicateKeyBusinessException(ResultMessages messages) {
        super(messages);
    }
}
