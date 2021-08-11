package jp.co.stnet.cms.sales.domain.validation;

import jp.co.stnet.cms.sales.presentation.controller.document.DocumentForm;
import jp.co.stnet.cms.sales.presentation.controller.document.FileForm;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileFormRequiredValidator implements ConstraintValidator<FileFormRequired, Object> {

    protected FileFormRequired fileFormRequired;
    private String message;


    @Override
    public void initialize(FileFormRequired constraintAnnotation) {
        this.fileFormRequired = constraintAnnotation;
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        FileForm fileForm = (FileForm) value;

        // PDFフィアル、フィルメモが設定された場合、原本のファイルの添付は必須
        if (StringUtils.isNotBlank(fileForm.getPdfUuid()) || StringUtils.isNotBlank(fileForm.getFileMemo())) {
            if (StringUtils.isBlank(fileForm.getFileUuid())) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("fileUuid")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
