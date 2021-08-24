package jp.co.stnet.cms.sales.domain.validation;

import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.sales.domain.model.document.DocumentCsvBean;
import jp.co.stnet.cms.sales.presentation.controller.document.DocumentForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DocumentCsvBeanDocCategory2Validator implements ConstraintValidator<DocumentCsvBeanDocCategory2, Object> {

    @Autowired
    VariableService variableService;

    private DocumentCsvBeanDocCategory2 documentCsvBeanDocCategory2;

    private String message;

    @Override
    public void initialize(DocumentCsvBeanDocCategory2 constraintAnnotation) {
        documentCsvBeanDocCategory2 = constraintAnnotation;
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        DocumentCsvBean csvBean = (DocumentCsvBean) value;

        // チェック対象がnullの場合チェックしない。
        if (csvBean.getDocCategory2() == null) {
            return true;
        }

        // 区分１，2 の組み合わせが存在しない場合、エラー
        if (!isParent(csvBean.getDocCategory2(), csvBean.getDocCategory1())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("docCategory2")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }


    private boolean isParent(String child, String parent) {
        String type = VariableType.DOC_CATEGORY2.name();
        if (parent == null) {
            return false;
        }
        List<Variable> variables = variableService.findAllByTypeAndCode(type, child);
        for (Variable variable : variables) {
            if (variable.getValue2().equals(parent)) {
                return true;
            }
        }
        return false;
    }


}
