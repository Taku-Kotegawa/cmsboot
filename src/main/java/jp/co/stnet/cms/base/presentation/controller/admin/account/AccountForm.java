package jp.co.stnet.cms.base.presentation.controller.admin.account;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.common.validation.NotContainControlChars;
import jp.co.stnet.cms.common.validation.NotContainControlCharsExceptNewlines;
import jp.co.stnet.cms.common.validation.StrongPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


//TODO キーの重複チェックの追加
@Data
@StrongPassword(usernamePropertyName = "username", newPasswordPropertyName = "password")
public class AccountForm {

    private Long version;

    @NotNull                    //null禁止
    @Size(min = 4, max = 128)   //4文字以上128文字以下
    private String username;

    @NotNull
    @Size(min = 1, max = 128)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 128)
    private String lastName;

    /**
     * 所属
     */
    @Size(min = 1, max = 128)
    private String department;

    @NotNull
    @Size(min = 1, max = 128)
    private String email;

    @NotNull(groups = Create.class)
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
