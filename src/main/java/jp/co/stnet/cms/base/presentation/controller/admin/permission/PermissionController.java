package jp.co.stnet.cms.base.presentation.controller.admin.permission;


import jp.co.stnet.cms.base.application.service.permission.PermissionService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.authentication.Permission;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("admin/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    private final String BASE_PATH = "/admin/permission/";
    private final String JSP_LIST = "admin/permission/list";

    @ModelAttribute
    public PermissionForm setUp() {
        return new PermissionForm();
    }

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list")
    public String list(Model model, PermissionForm form, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        form.setPermissionRoles(permissionService.findAllMap());

        model.addAttribute(Permission.values());
        model.addAttribute(Role.values());

        return JSP_LIST;
    }

    /**
     * 保存
     */
    @PostMapping(value = "list")
    public String save(Model model, PermissionForm form, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        permissionService.deleteAll();

        permissionService.saveAll(form.getPermissionRoles());

        form.setPermissionRoles(permissionService.findAllMap());

//        form.setPermissionRoles(permissionService.findAllMap());

//        model.addAttribute(Permission.values());
//        model.addAttribute(Role.values());

//        return JSP_LIST;
        return "redirect:/admin/permission/list";
    }




}

