package jp.co.stnet.cms.common.exception;

import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 悲観的排他制御の異常を表すビジネス例外
 */
public class PessimisticLockingFailureBusinessException extends BusinessException {
    public PessimisticLockingFailureBusinessException(ResultMessages messages) {
        super(messages);
    }
}
