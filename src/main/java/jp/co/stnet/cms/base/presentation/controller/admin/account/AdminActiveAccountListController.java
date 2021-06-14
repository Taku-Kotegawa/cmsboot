package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;

@Controller
@RequestMapping(BASE_PATH)
public class AdminActiveAccountListController {

    private final String BASE_PATH = "admin/account";
    private final String JSP_ACTIVE_LIST = BASE_PATH + "/activeList";

    @Autowired
    AccountService accountService;

    @Autowired
    SessionRegistry sessionRegistry;

    /**
     * アクティブユーザ一覧画面の表示
     */
    @GetMapping(value = "active-list")
    public String activeList(Model model) {

        List<Object> principals = sessionRegistry.getAllPrincipals();

        List<String> ids = new ArrayList<>();

        for (Object principal: principals) {
            if (principal instanceof LoggedInUser) {
                ids.add(((LoggedInUser) principal).getUsername());
            }
        }

        List<Account> activeUserList = accountService.findAllById(ids);

        model.addAttribute("activeUserList", activeUserList);

        return JSP_ACTIVE_LIST;
    }

}
