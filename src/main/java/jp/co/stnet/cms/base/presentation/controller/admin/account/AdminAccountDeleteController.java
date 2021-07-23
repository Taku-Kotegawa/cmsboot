package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.base.application.service.account.AccountService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;


@Controller
@RequestMapping(BASE_PATH)
public class AdminAccountDeleteController {

    @Autowired
    AccountService accountService;

    @Autowired
    AdminAccountAuthority authority;

    /**
     * 削除
     */
    @GetMapping(value = "{username}/delete")
    public String delete(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("username") String username) {

        authority.hasAuthority(Constants.OPERATION.DELETE, loggedInUser);

        try {
            accountService.delete(username);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return "redirect:" + new OperationsUtil(BASE_PATH).getEditUrl(username);
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0003);
        redirect.addFlashAttribute(messages);

        return "redirect:" + new OperationsUtil(BASE_PATH).getListUrl();
    }
}