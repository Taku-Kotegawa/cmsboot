package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.account.AccountService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.validation.groups.Default;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.TEMPLATE_FORM;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Controller
@RequestMapping(BASE_PATH)
@TransactionTokenCheck(BASE_PATH)
public class AdminAccountCreateController {

    @Autowired
    AdminAccountHelper helper;

    @Autowired
    AccountService accountService;

    @Autowired
    Mapper beanMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileManagedService fileManagedService;

    @Autowired
    AdminAccountAuthority authority;


    @ModelAttribute
    AccountForm setUp() {
        return new AccountForm();
    }

    /**
     * 新規作成画面を開く
     */
    @GetMapping(value = "create", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String createForm(AccountForm form,
                             Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @RequestParam(value = "copy", required = false) String copy) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (isNotEmpty(copy)) {
            Account source = accountService.findById(copy);
            beanMapper.map(source, form);
            form.setUsername(null);
            form.setPassword(null);
        }

        if (form.getImageUuid() != null) {
            form.setImageManaged(fileManagedService.findByUuid(form.getImageUuid()));
        }

        model.addAttribute("buttonState", helper.getButtonStateMap(Constants.OPERATION.CREATE, null, form).asMap());
        model.addAttribute("fieldState", helper.getFiledStateMap(Constants.OPERATION.CREATE, null, form).asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return TEMPLATE_FORM;
    }

    /**
     * 新規登録
     */
    @PostMapping(value = "create")
    @TransactionTokenCheck
    public String create(@Validated({AccountForm.Create.class, Default.class}) AccountForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return createForm(form, model, loggedInUser, null);
        }

        Account account = beanMapper.map(form, Account.class);
        account.setStatus(Status.VALID.getCodeValue());
        account.setPassword(passwordEncoder.encode(form.getPassword()));

        try {
            accountService.save(account);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return createForm(form, model, loggedInUser, null);
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0001);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getEditUrl(account.getUsername());
    }
}
