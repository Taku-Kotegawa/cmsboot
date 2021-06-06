package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.github.dozermapper.core.Mapper;

import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.application.service.authentication.AccountSharedService;
import jp.co.stnet.cms.base.application.service.authentication.PasswordChangeService;
import jp.co.stnet.cms.base.application.service.authentication.UnlockService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.common.util.StateMap;
import jp.co.stnet.cms.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.session.SessionRegistry;
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

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AccountForm.Create;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AccountForm.Update;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/account")
@TransactionTokenCheck("admin/account")
public final class AdminAccountController {

    private final String BASE_PATH = "admin/account";
    private final String JSP_LIST = BASE_PATH + "/list";
    private final String JSP_FORM = BASE_PATH + "/form";
    private final String JSP_VIEW = BASE_PATH + "/view";
    private final String JSP_ACTIVE_LIST = BASE_PATH + "/activeList";

    @Autowired
    AccountService accountService;

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    UnlockService unlockService;

    @Autowired
    PasswordChangeService passwordChangeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Mapper beanMapper;

    @Autowired
    SessionRegistry sessionRegistry;

    @PersistenceContext
    EntityManager entityManager;

    @ModelAttribute
    private AccountForm setUp() {
        return new AccountForm();
    }

    // ---------------- 一覧 -----------------------------------------------------

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list")
    public String list(Model model) {
        return JSP_LIST;
    }

    /**
     * アクティブユーザ一覧画面の表示
     */
    @GetMapping(value = "active-list")
    public String activeList(Model model) {

        List<Object> principals = sessionRegistry.getAllPrincipals();

        List<Account> activeUserList = new ArrayList<>();

        for (Object principal: principals) {
            if (principal instanceof LoggedInUser) {
                activeUserList.add(accountService.findById(((LoggedInUser) principal).getUsername()));
            }
        }

        model.addAttribute("activeUserList", activeUserList);

        return JSP_ACTIVE_LIST;
    }

    /**
     * DataTables用のJSONの作成
     *
     * @param input DataTablesから要求
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "/list/json")
    public DataTablesOutput<AccountListBean> listJson(@Validated DataTablesInput input) {

        OperationsUtil op = new OperationsUtil(null);

        List<AccountListBean> list = new ArrayList<>();
        Page<Account> accountPage = accountService.findPageByInput(input);

        for (Account account : accountPage.getContent()) {
            AccountListBean accountListBean = beanMapper.map(account, AccountListBean.class);
            accountListBean.setOperations(getToggleButton(account.getUsername(), op));
            accountListBean.setDT_RowId(account.getUsername());

            // ステータスラベル
            String statusLabel = account.getStatus().equals(Status.VALID.getCodeValue()) ? Status.VALID.getCodeLabel() : Status.INVALID.getCodeLabel();
            if (accountSharedService.isLocked(account.getUsername())) statusLabel = statusLabel + "(ロック)";
            accountListBean.setStatusLabel(statusLabel);

            list.add(accountListBean);
        }

        DataTablesOutput<AccountListBean> output = new DataTablesOutput<>();
        output.setData(list);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(accountPage.getTotalElements());

        return output;
    }

    private String getToggleButton(String id, OperationsUtil op) {

        StringBuffer link = new StringBuffer();
        link.append("<div class=\"btn-group\">");
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
        link.append("<button type=\"button\" class=\"btn btn-button btn-sm dropdown-toggle dropdown-toggle-split\"data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">");
        link.append("</button>");
        link.append("<ul class=\"dropdown-menu\">");
        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getViewUrl(id) + "\">" + op.getLABEL_VIEW() + "</a></li>");
        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getCopyUrl(id) + "\">" + op.getLABEL_COPY() + "</a></li>");
        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getInvalidUrl(id) + "\">" + op.getLABEL_INVALID() + "</a></li>");
//        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getSwitchUserUrl(id) + "\">" + op.getLABEL_SWITCH_USER() + "</a></li>");
//        link.append("<li><form method=\"post\" action=\"" + op.getSwitchUserUrl(id) + "\"><button class=\"dropdown-item\">" + op.getLABEL_SWITCH_USER() + "</button></form></li>");
        link.append("</ul>");
        link.append("</div>");

        return link.toString();
    }

    // ---------------- 新規登録 -----------------------------------------------------

    /**
     * 新規作成画面を開く
     */
    @GetMapping(value = "create", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String createForm(AccountForm form,
                             Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @RequestParam(value = "copy", required = false) String copy) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (!StringUtils.isEmpty(copy)) {
            Account source = accountService.findById(copy);
            beanMapper.map(source, form);
            form.setUsername(null);
            form.setPassword(null);
        }

        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.CREATE, null, form).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.CREATE, null).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }

    /**
     * 新規登録
     */
    @PostMapping(value = "create")
    @TransactionTokenCheck
    public String create(@Validated({Create.class, Default.class}) AccountForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

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

    // ---------------- 編集 ---------------------------------------------------------

    /**
     * 編集画面を開く
     */
    @GetMapping(value = "{username}/update", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String updateForm(AccountForm form, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.SAVE, loggedInUser);

        Account account = accountService.findById(username);

        // DBからデータ取得し、modelとformにセット
        if (form.getFirstName() == null) {
            beanMapper.map(account, form);
            form.setPassword(null);
        }

        model.addAttribute("account", account);

        // 添付フィアルの情報をセット
        FileManaged fileManaged = fileManagedSharedService.findByUuid(account.getImageUuid());
        model.addAttribute("imageFileManaged", fileManaged);

        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.SAVE, account, form).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.SAVE, account).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
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
        accountService.hasAuthority(Constants.OPERATION.SAVE, loggedInUser);

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


    // ---------------- 削除 ---------------------------------------------------------

    /**
     * 削除
     */
    @GetMapping(value = "{username}/delete")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String delete(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.DELETE, loggedInUser);

        try {
            accountService.delete(username);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0007);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getListUrl();
    }

    // ---------------- 無効化 ---------------------------------------------------------

    @GetMapping(value = "{username}/invalid")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String invalid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                          @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.INVALID, loggedInUser);

        try {
            accountService.invalid(username);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0002);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getViewUrl(username);
    }

    // ---------------- 参照 ---------------------------------------------------------

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{username}")
    public String view(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                       @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.VIEW, loggedInUser);

        Account account = accountService.findById(username);
        model.addAttribute("account", account);

        // ロック状態確認
        model.addAttribute("isLocked", accountSharedService.isLocked(username));

        // 添付フィアルの情報をセット
        FileManaged fileManaged = fileManagedSharedService.findByUuid(account.getImageUuid());
        model.addAttribute("imageFileManaged", fileManaged);

        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.VIEW, account, null).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.VIEW, account).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }


    // ---------------- ロック解除 ---------------------------------------------------------

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{username}/unlock")
    public String unlock(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("username") String username) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.SAVE, loggedInUser);

        // 存在しなければ例外
        Account account = accountService.findById(username);

        // ロック解除
        unlockService.unlock(username);

        // 参照画面へ
        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getViewUrl(username);
    }

    // ---------------- ダウンロード -----------------------------------------------

    @GetMapping("{uuid}/download")
    public String download(
            Model model,
            @PathVariable("uuid") String uuid,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        accountService.hasAuthority(Constants.OPERATION.SAVE, loggedInUser);

        FileManaged fileManaged = fileManagedSharedService.findByUuid(uuid);
        model.addAttribute(fileManaged);
        return "fileManagedDownloadView";
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


    // ---------------- 共通(private) -----------------------------------------------

    private OperationsUtil op() {
        return new OperationsUtil(BASE_PATH);
    }

    private OperationsUtil op(String param) {
        return new OperationsUtil(param);
    }

    /**
     * @param operation
     * @param record
     * @param form
     * @return
     */
    private StateMap getButtonStateMap(String operation, Account record, AccountForm form) {

        if (record == null) {
            record = new Account();
        }

        List<String> includeKeys = new ArrayList<>();
        includeKeys.add(Constants.BUTTON.GOTOLIST);
        includeKeys.add(Constants.BUTTON.GOTOUPDATE);
        includeKeys.add(Constants.BUTTON.VIEW);
        includeKeys.add(Constants.BUTTON.SAVE);
        includeKeys.add(Constants.BUTTON.INVALID);
        includeKeys.add(Constants.BUTTON.DELETE);
        includeKeys.add(Constants.BUTTON.UNLOCK);
        includeKeys.add(Constants.BUTTON.SET_APIKEY);
        includeKeys.add(Constants.BUTTON.UNSET_APIKEY);
        includeKeys.add(Constants.BUTTON.SWITCH_USER);

        StateMap buttonState = new StateMap(Default.class, includeKeys, new ArrayList<>());

        // 常に表示
        buttonState.setViewTrue(Constants.BUTTON.GOTOLIST);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            buttonState.setViewTrue(Constants.BUTTON.SAVE);
        }

        // 編集
        if (Constants.OPERATION.SAVE.equals(operation)) {

            buttonState.setViewTrue(Constants.BUTTON.UNLOCK);
            buttonState.setViewTrue(Constants.BUTTON.VIEW);
            buttonState.setViewTrue(Constants.BUTTON.SWITCH_USER);

            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
                buttonState.setViewTrue(Constants.BUTTON.UNLOCK);
            }

            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
            }

            if (StringUtils.isEmpty(form.getApiKey())) {
                buttonState.setViewTrue(Constants.BUTTON.SET_APIKEY);
            } else {
                buttonState.setViewTrue(Constants.BUTTON.UNSET_APIKEY);
            }

        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {

            buttonState.setViewTrue(Constants.BUTTON.SWITCH_USER);

            // スタータスが公開時
            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
            }

        }

        return buttonState;
    }

    /**
     * @param operation
     * @param record
     * @return
     */
    private StateMap getFiledStateMap(String operation, Account record) {
        List<String> excludeKeys = new ArrayList<>();

        // 常設の隠しフィールドは状態管理しない
        StateMap fieldState = new StateMap(AccountForm.class, new ArrayList<>(), excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setReadOnlyTrue("apiKey");
        }

        // 編集
        if (Constants.OPERATION.SAVE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setReadOnlyTrue("username");
            fieldState.setReadOnlyTrue("apiKey");

            // スタータスが無効
            if (Status.INVALID.toString().equals(record.getStatus())) {
                fieldState.setReadOnlyTrueAll();
            }
        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {
            fieldState.setViewTrueAll();
            fieldState.setViewFalse("password");
        }

        return fieldState;
    }




}
