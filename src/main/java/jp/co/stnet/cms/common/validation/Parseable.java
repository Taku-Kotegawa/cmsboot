package jp.co.stnet.cms.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = jp.co.stnet.cms.common.validation.ParseableValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Parseable {

    ParseableType value();

    String message() default "{string.parseable}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}