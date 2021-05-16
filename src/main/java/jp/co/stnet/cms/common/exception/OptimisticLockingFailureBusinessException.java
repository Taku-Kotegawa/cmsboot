package jp.co.stnet.cms.common.exception;

import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 楽観的排他制御の異常を表すビジネス例外
 */
public class OptimisticLockingFailureBusinessException extends BusinessException {
    public OptimisticLockingFailureBusinessException(ResultMessages messages) {
        super(messages);
    }
}
