package jp.co.stnet.cms.common.controlleradvice;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;


/**
 * 全コントローラクラスに適用される共通処理
 */
@ControllerAdvice(annotations = Controller.class)
public class GlobalInitBinderControllerAdvice {

    /**
     * フォームからポストされたデータが空文字列の場合、nullに置換してバインディングする
     *
     * @param binder バインダー
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // bind empty strings as null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
