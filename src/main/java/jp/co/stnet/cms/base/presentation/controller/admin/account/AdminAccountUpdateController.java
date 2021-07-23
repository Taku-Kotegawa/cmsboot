package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.account.AccountService;
import jp.co.stnet.cms.base.application.service.authentication.UnlockService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import org.apache.commons.lang3.StringUtils;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.groups.Default;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AccountForm.Update;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.TEMPLATE_FORM;

@Controller
@RequestMapping(BASE_PATH)
@TransactionTokenCheck(BASE_PATH)
public class AdminAccountUpdateController {

    @Autowired
    AdminAccountHelper helper;

    @Autowired
    AccountService accountService;

    @Autowired
    AdminAccountAuthority authority;

    @Autowired
    FileManagedService fileManagedService;

    @Autowired
    UnlockService unlockService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Mapper beanMapper;

    @PersistenceContext
    EntityManager entityManager;

    @ModelAttribute
    AccountForm setUp() {
        return new AccountForm();
    }

    /**
     * 編集画面を開く
     */
    @GetMapping(value = "{username}/update", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String updateForm(AccountForm form, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        Account account = accountService.findById(username);

        if (form.getImageUuid() != null) {
            form.setImageManaged(fileManagedService.findByUuid(form.getImageUuid()));
        }

        // 初回表示(入力チェックエラー時の再表示でない場合)
        if (form.getVersion() == null) {
            beanMapper.map(account, form);
            form.setPassword(null);
        }

        model.addAttribute("account", account);
        model.addAttribute("buttonState", helper.getButtonStateMap(Constants.OPERATION.UPDATE, account, form).asMap());
        model.addAttribute("fieldState", helper.getFiledStateMap(Constants.OPERATION.UPDATE, account, form).asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return TEMPLATE_FORM;
    }

    /**
     * 更新
     */
    @PostMapping(value = "{username}/update")
    @TransactionTokenCheck
    public String update(@Validated({Update.class, Default.class}) AccountForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return updateForm(form, model, loggedInUser, username);
        }

        Account account = accountService.findById(username);
        entityManager.detach(account);
        String password = account.getPassword();
        beanMapper.map(form, account);
        account.setPassword(password);
        account.setRoles(form.getRoles());

        // パスワード欄が入力された場合にパスワード設定
        if (StringUtils.isNotBlank(form.getPassword())) {
            account.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        try {
            accountService.save(account);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return updateForm(form, model, loggedInUser, username);
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0004);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getEditUrl(account.getUsername());
    }

    @PostMapping(value = "{username}/update", params = "setApiKey")
    @TransactionTokenCheck
    public String setApiKey(AccountForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirect,
                            @AuthenticationPrincipal LoggedInUser loggedInUser,
                            @PathVariable("username") String username) {

        form.setApiKey(accountService.generateApiKey(username));

        return updateForm(form, model, loggedInUser, username);
    }

    @PostMapping(value = "{username}/update", params = "unsetApiKey")
    @TransactionTokenCheck
    public String unsetApiKey(AccountForm form,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirect,
                              @AuthenticationPrincipal LoggedInUser loggedInUser,
                              @PathVariable("username") String username) {

        form.setApiKey(null);

        return updateForm(form, model, loggedInUser, username);
    }

    // ---------------- 無効化 ---------------------------------------------------------

    @GetMapping(value = "{username}/invalid")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String invalid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                          @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        authority.hasAuthority(Constants.OPERATION.INVALID, loggedInUser);

        try {
            accountService.invalid(username);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0002);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getEditUrl(username);
    }

    // ---------------- 無効解除 ---------------------------------------------------------

    @GetMapping(value = "{username}/valid")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String valid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                        @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        authority.hasAuthority(Constants.OPERATION.VALID, loggedInUser);

        try {
            accountService.valid(username);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0002);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getEditUrl(username);
    }

    // ---------------- ロック解除 ---------------------------------------------------------

    /**
     * ロック解除
     */
    @GetMapping(value = "{username}/unlock")
    public String unlock(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        // 存在しなければ例外
        Account account = accountService.findById(username);

        // ロック解除
        unlockService.unlock(username);

        // 参照画面へ
        return "redirect:" + new OperationsUtil(BASE_PATH).getViewUrl(username);
    }

    // ---------------- API KEY 生成 / 削除 -----------------------------------------------

    @GetMapping("{username}/generateapikey")
    public String generateApiKey(Model model, @PathVariable("username") String username,
                                 RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        accountService.saveApiKey(username);

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_SL_AC_0001);
        redirect.addFlashAttribute(messages);

        return "redirect:/admin/account/" + username;
    }

    @GetMapping("{username}/deleteapikey")
    public String deleteApiKey(Model model, @PathVariable("username") String username,
                               RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        accountService.deleteApiKey(username);

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_SL_AC_0001);
        redirect.addFlashAttribute(messages);

        return "redirect:/admin/account/" + username;
    }


}
