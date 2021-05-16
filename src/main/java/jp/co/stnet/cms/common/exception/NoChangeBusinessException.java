package jp.co.stnet.cms.common.exception;

import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 保存ボタン押下時に変更が無いことを表すビジネス例外
 */
public class NoChangeBusinessException extends BusinessException {
    public NoChangeBusinessException(ResultMessages messages) {
        super(messages);
    }
}
