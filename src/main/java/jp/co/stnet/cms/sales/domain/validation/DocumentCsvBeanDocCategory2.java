package jp.co.stnet.cms.sales.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {DocumentCsvBeanDocCategory2Validator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface DocumentCsvBeanDocCategory2 {

    String message() default "{jp.co.stnet.cms.sales.domain.validation.DocumentCsvBeanDocCategory2.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
