package jp.co.stnet.cms.base.presentation.controller.admin.upload;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadForm {

    /**
     * アプロード用のジョブ名
     */
    @NotNull
    private String jobName;

    /**
     * アップロードファイル(Uuid)
     */
    @NotNull
    private String uploadFileUuid;

    /**
     * アップロードファイル(Managed)
     */
    private FileManaged uploadFileManaged;

    private String encoding;

    private String fileType;

}
