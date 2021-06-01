package jp.co.stnet.cms.base.presentation.controller.admin.account;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.common.validation.NotContainControlChars;
import jp.co.stnet.cms.common.validation.NotContainControlCharsExceptNewlines;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


//TODO キーの重複チェックの追加
@Data
public class AccountForm {

    @NotNull                    //null禁止
    @NotContainControlChars     //制御文字禁止
    @Size(min = 4, max = 128)   //4文字以上128文字以下
    private String username;

    @NotNull
    @NotContainControlChars
    @Size(min = 1, max = 128)
    private String firstName;

    @NotNull
    @NotContainControlChars
    @Size(min = 1, max = 128)
    private String lastName;

    @NotNull
    @NotContainControlChars
    @Size(min = 1, max = 128)
    private String email;

    @NotNull(groups = Create.class)
    @NotContainControlChars
    private String password;

    @NotContainControlChars
    private String url;

    // ダミー
    private String image;

    private String imageUuid;

    private FileManaged imageManaged;

    @NotContainControlCharsExceptNewlines
    private String profile;

    private String apiKey;

    private List<String> roles;

    private String allowedIp;

    public interface Create {
    }

    public interface Update {
    }

}
