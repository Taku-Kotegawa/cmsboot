package jp.co.stnet.cms.sales.domain.validation;

import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.sales.domain.model.document.DocumentCsvBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DocumentCsvBeanDocService3Validator implements ConstraintValidator<DocumentCsvBeanDocService3, Object> {

    private static final String TYPE = VariableType.DOC_SERVICE3.name();
    @Autowired
    VariableService variableService;

    private DocumentCsvBeanDocService3 documentCsvBeanDocService2;

    private String message;

    @Override
    public void initialize(DocumentCsvBeanDocService3 constraintAnnotation) {
        documentCsvBeanDocService2 = constraintAnnotation;
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        DocumentCsvBean csvBean = (DocumentCsvBean) value;

        // チェック対象がnullの場合チェックしない。
        if (csvBean.getDocService2() == null) {
            return true;
        }

        // 区分１，2 の組み合わせが存在しない場合、エラー
        if (!isParent(csvBean.getDocService3(), csvBean.getDocService2())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("docService3")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }


    private boolean isParent(String child, String parent) {
        if (parent == null) {
            return false;
        }
        List<Variable> variables = variableService.findAllByTypeAndCode(TYPE, child);
        for (Variable variable : variables) {
            if (variable.getValue2().equals(parent)) {
                return true;
            }
        }
        return false;
    }


}
