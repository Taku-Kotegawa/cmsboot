package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.application.service.authentication.AccountSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.TEMPLATE_FORM;

@Controller
@RequestMapping(BASE_PATH)
public class AdminAccountController {

    @Autowired
    AdminAccountHelper helper;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountSharedService accountSharedService;

    @ModelAttribute
    AccountForm setUp() {
        return new AccountForm();
    }

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{username}")
    public String view(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                       @PathVariable("username") String username) {

        // 権限チェック
        accountService.hasAuthority(Constants.OPERATION.VIEW, loggedInUser);

        // データ取得
        Account account = accountService.findById(username);
        model.addAttribute("account", account);

        // ロック状態確認
        model.addAttribute("isLocked", accountSharedService.isLocked(username));

        model.addAttribute("buttonState", helper.getButtonStateMap(Constants.OPERATION.VIEW, account, null).asMap());
        model.addAttribute("fieldState", helper.getFiledStateMap(Constants.OPERATION.VIEW, account, null).asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return TEMPLATE_FORM;
    }

}
