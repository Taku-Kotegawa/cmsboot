package jp.co.stnet.cms.common.exception;

import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 状態違反を表すビジネス例外
 */
public class IllegalStateBusinessException extends BusinessException {
    public IllegalStateBusinessException(ResultMessages messages) {
        super(messages);
    }
}
