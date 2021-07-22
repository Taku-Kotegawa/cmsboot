package jp.co.stnet.cms.sales.domain.validation;

import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.sales.presentation.controller.document.DocumentForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 事業領域を選択したら、サービス種別とサービス(選択肢がある場合)を必須とする。
 */
public class ValidServiceValidator implements ConstraintValidator<ValidService, Object> {

    @Autowired
    VariableService variableService;

    private ValidService validService;

    private String message;

    @Override
    public void initialize(ValidService constraintAnnotation) {
        this.validService = constraintAnnotation;
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        boolean result = true;

        DocumentForm documentForm = (DocumentForm) value;

        context.disableDefaultConstraintViolation();

        // 事業領域に値が設定された場合、サービス種別・サービスの必須チェックを行う。
        if (documentForm.getDocService1() != null) {

            // サービス種別の必須チェック
            if (documentForm.getDocService2() == null) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("docService2")
                        .addConstraintViolation();
                result = false;
            }

            // サービス種別の値に紐づくサービスの有無を確認し、選択可能なサービスがある場合必須
            if (!variableService.findAllByTypeAndValueX(VariableType.DOC_SERVICE3.name(), 2, documentForm.getDocService2()).isEmpty()) {
                if (documentForm.getDocService3() == null) {
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode("docService3")
                            .addConstraintViolation();
                    result = false;
                }
            }

        }
        return result;
    }


}
