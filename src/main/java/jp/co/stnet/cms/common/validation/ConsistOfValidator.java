package jp.co.stnet.cms.common.validation;

import org.terasoluna.gfw.common.codepoints.CodePoints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ConsistOfValidator implements
        ConstraintValidator<ConsistOf, CharSequence> {

    private final List<String> classNameList = new ArrayList<>();
    /**
     * Array of CodePoints to check
     */
    private CodePoints[] codePointsArray;

    /**
     * initialize to validate with {@link ConsistOf}
     *
     * @param consistOf {@link ConsistOf} annotation
     */
    @Override
    public void initialize(ConsistOf consistOf) {
        Class<? extends CodePoints>[] classes = consistOf.value();
        this.codePointsArray = new CodePoints[classes.length];
        for (int i = 0; i < classes.length; i++) {
            this.classNameList.add(classes[i].getName());
            this.codePointsArray[i] = CodePoints.of(classes[i]);
        }
    }

    /**
     * Validate whether all code points in the given string are included in any {@link CodePoints} class specified by
     * {@link ConsistOf#value()}
     *
     * @param value   the string to check
     * @param context validation context
     * @return {@code true} if all code points in the given string are included in any {@link CodePoints} class specified by
     * {@link ConsistOf#value()} or the given string is {@code null}. {@code false} otherwise.
     */
    @Override
    public boolean isValid(CharSequence value,
                           ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String s = value.toString();

        context.disableDefaultConstraintViolation();
        for (String className : classNameList) {
            context.buildConstraintViolationWithTemplate("{org.terasoluna.gfw.common.codepoints.ConsistOf." + className + ".message}")
                    .addConstraintViolation();
        }

        return CodePoints.containsAllInAnyCodePoints(s, codePointsArray);
    }


}