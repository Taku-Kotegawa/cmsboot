package jp.co.stnet.cms.example.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileForm implements Serializable {

    // TODO type と fileUuid は両方セットでなければならない入力チェックの追加

    private String type;

    private String fileUuid;

    private FileManaged fileManaged;

    private String pdfUuid;

    private FileManaged pdfManaged;

}
