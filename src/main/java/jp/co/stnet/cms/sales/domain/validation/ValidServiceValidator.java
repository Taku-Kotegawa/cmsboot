package jp.co.stnet.cms.sales.domain.validation;

import jp.co.stnet.cms.sales.presentation.controller.document.DocumentForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 事業領域を選択したら、サービス種別とサービスを必須とする。
 */
public class ValidServiceValidator implements ConstraintValidator<ValidService, Object> {

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

        if (documentForm.getDocService1() != null) {
            if (documentForm.getDocService2() == null) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("docService2")
                        .addConstraintViolation();
                result = false;
            }

            if (documentForm.getDocService3() == null) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("docService3")
                        .addConstraintViolation();
                result = false;
            }
        }
        return result;
    }
}
