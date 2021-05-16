package jp.co.stnet.cms.common.exception;

import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 引数の異常を表すビジネス例外
 */
public class InvalidArgumentBusinessException extends BusinessException {
    public InvalidArgumentBusinessException(ResultMessages messages) {
        super(messages);
    }
}

