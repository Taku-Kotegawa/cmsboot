package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileForm implements Serializable {

    private String fileUuid;

    private FileManaged fileManaged;

    private String pdfUuid;

    private FileManaged pdfManaged;

    private String fileMemo;
}
