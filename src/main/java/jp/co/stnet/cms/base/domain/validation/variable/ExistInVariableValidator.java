package jp.co.stnet.cms.base.domain.validation.variable;


import jp.co.stnet.cms.base.application.repository.variable.VariableRepository;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.presentation.controller.admin.variable.VariableForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ExistInVariableValidator implements ConstraintValidator<ExistInVariable, Object> {

    @Autowired
    VariableRepository variableRepository;

    private String message;

    @Override
    public void initialize(ExistInVariable constraintAnnotation) {
          message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        VariableForm variableForm = (VariableForm) value;
        List<Variable> list = variableRepository.findAllByTypeAndCode(variableForm.getType(), variableForm.getCode());
        if (list.size() == 0) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("code").addConstraintViolation();
            return false;
        }
    }

}
