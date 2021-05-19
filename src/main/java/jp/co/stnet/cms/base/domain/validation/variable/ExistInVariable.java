package jp.co.stnet.cms.base.domain.validation.variable;



import jp.co.stnet.cms.common.message.MessageKeys;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ExistInVariableValidator.class})
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ExistInVariable {

    String message() default "{" + MessageKeys.E_VALIDATION_FW_8001 + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String type() default "";

    String code() default "";

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ExistInVariable[] value();
    }

}
