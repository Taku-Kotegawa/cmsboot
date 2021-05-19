package jp.co.stnet.cms.base.presentation.controller.admin.permission;

import lombok.Data;

import java.util.Map;

@Data
public class PermissionForm {

    private Map<String, Map<String, Boolean>> permissionRoles;

}
