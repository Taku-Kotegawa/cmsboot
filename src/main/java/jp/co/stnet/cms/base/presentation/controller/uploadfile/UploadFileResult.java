package jp.co.stnet.cms.base.presentation.controller.uploadfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResult implements Serializable {

    private String message;

    private Long fid;

    private String uuid;

    private String name;

    private String type;

    private Long size;

    private String url;

//    private String deleteUrl;

}
