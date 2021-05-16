package jp.co.stnet.cms.common.validation;


import org.terasoluna.gfw.common.codepoints.CodePoints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(ConsistOf.List.class)
@Constraint(validatedBy = {ConsistOfValidator.class})
@Documented
public @interface ConsistOf {

    /**
     * CodePoints
     *
     * @return codePoints
     */
    Class<? extends CodePoints>[] value();

    /**
     * Error message or message key
     *
     * @return error message or message key
     */
    String message() default "{org.terasoluna.gfw.common.codepoints.ConsistOf.aaa.message}";

    /**
     * Constraint groups
     *
     * @return constraint groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload
     *
     * @return payload
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several <code>@ConsistOf</code> annotations on the same element
     *
     * @see ConsistOf
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER,
            TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        /**
         * <code>@ConsistOf</code> annotations
         *
         * @return annotations
         */
        ConsistOf[] value();
    }

}
