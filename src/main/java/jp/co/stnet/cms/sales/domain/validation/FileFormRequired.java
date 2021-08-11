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
@Constraint(validatedBy = {FileFormRequiredValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface FileFormRequired {

    String message() default "{jp.co.stnet.cms.sales.domain.validation.FileFormRequired.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
