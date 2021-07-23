package jp.co.stnet.cms.base.presentation.controller.admin.accesscounter;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.accesscounter.AccessCounterService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.AccessCounter;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.common.util.CsvUtils;
import jp.co.stnet.cms.common.util.StateMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.ArrayList;
import java.util.List;

import static jp.co.stnet.cms.common.constant.Constants.REDIRECT;

@Slf4j
@Controller
@RequestMapping("admin/accesscounter")
@TransactionTokenCheck("accesscounter")
public class AccessCounterController {

    // JSPのパス設定
    private static final String BASE_PATH = "admin/accesscounter";
    private static final String JSP_LIST = BASE_PATH + "/list";
    private static final String JSP_FORM = BASE_PATH + "/form";

    // CSV/Excelのファイル名(拡張子除く)
    private static final String DOWNLOAD_FILENAME = "accesscounter";

    @Autowired
    AccessCounterService accessCounterService;

    @Autowired
    FileManagedService fileManagedService;

    @Autowired
    AccessCounterAuthority authority;

    @Autowired
    Mapper beanMapper;

    @ModelAttribute
    private AccessCounterForm setUp() {
        return new AccessCounterForm();
    }

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list")
    public String list(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);
        return JSP_LIST;
    }

    /**
     * DataTables用のJSONの作成
     *
     * @param input DataTablesからの要求(Server-side処理)
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "/list/json")
    public DataTablesOutput<AccessCounterListRow> listJson(@Validated DataTablesInputDraft input,
                                                           @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        List<AccessCounterListRow> listRows = new ArrayList<>();
        Page<jp.co.stnet.cms.base.domain.model.common.AccessCounter> accessCounterPage = accessCounterService.findPageByInput(input);
        long recordsFiltered = accessCounterPage.getTotalElements();

        for (AccessCounter bean : accessCounterPage.getContent()) {
            AccessCounterListRow accessCounterListRow = beanMapper.map(bean, AccessCounterListRow.class);
            accessCounterListRow.setOperations(getToggleButton(bean.getId().toString(), op(null)));
            accessCounterListRow.setDT_RowId(bean.getId().toString());
            // ステータスラベル
            accessCounterListRow.setStatusLabel(Status.getByValue(bean.getStatus()).getCodeLabel());
            listRows.add(accessCounterListRow);
        }

        DataTablesOutput<AccessCounterListRow> output = new DataTablesOutput<>();
        output.setData(listRows);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(recordsFiltered);

        return output;
    }

    /**
     * CSVファイルのダウンロード
     *
     * @param input DataTablesからの要求(Server-side処理)
     * @param model モデル
     * @return ファイルダウンロード用View
     */
    @GetMapping(value = "/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model,
                          @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        input.setStart(0);
        input.setLength(Constants.CSV.MAX_LENGTH);
        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getCsvDefault());
        model.addAttribute("csvFileName", DOWNLOAD_FILENAME + ".csv");
        return "csvDownloadView";
    }

    /**
     * TSVファイルのダウンロード
     *
     * @param input DataTablesからの要求(Server-side処理)
     * @param model モデル
     * @return ファイルダウンロード用View
     */
    @GetMapping(value = "/list/tsv")
    public String listTsv(@Validated DataTablesInputDraft input, Model model,
                          @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        input.setStart(0);
        input.setLength(Constants.CSV.MAX_LENGTH);
        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getTsvDefault());
        model.addAttribute("csvFileName", DOWNLOAD_FILENAME + ".tsv");
        return "csvDownloadView";
    }

    /**
     * csvDownloadViewに渡すデータ準備
     *
     * @param input DataTablesからの要求(Server-side処理)
     * @param model データをセットするモデル
     */
    private void setModelForCsv(DataTablesInputDraft input, Model model) {

        List<AccessCounterCsvBean> csvBeans = new ArrayList<>();
        Page<jp.co.stnet.cms.base.domain.model.common.AccessCounter> accessCounterPage = accessCounterService.findPageByInput(input);

        for (AccessCounter accessCounter : accessCounterPage.getContent()) {
            AccessCounterCsvBean row = beanMapper.map(accessCounter, AccessCounterCsvBean.class);
            row.setStatusLabel(Status.getByValue(accessCounter.getStatus()).getCodeLabel());
            csvBeans.add(row);
        }

        model.addAttribute("exportCsvData", csvBeans);
        model.addAttribute("class", AccessCounterCsvBean.class);
    }

    /**
     * 一覧画面のトグルボタンHTMLの生成
     *
     * @param id エンティティの内部ID
     * @param op OperationsUtil リンクURLを生成するクラス
     * @return HTML
     */
    private String getToggleButton(String id, OperationsUtil op) {
        StringBuilder link = new StringBuilder();
        link.append("<div class=\"btn-group\">");
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
        link.append("<button type=\"button\" class=\"btn btn-button btn-sm dropdown-toggle dropdown-toggle-split\"data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">");
        link.append("</button>");
        link.append("<div class=\"dropdown-menu\">");
        link.append("<a class=\"dropdown-item\" href=\"" + op.getViewUrl(id) + "\">" + op.getLABEL_VIEW() + "</a>");
        link.append("</div>");
        link.append("</div>");

        return link.toString();
    }

    private OperationsUtil op() {
        return new OperationsUtil(BASE_PATH);
    }

    private OperationsUtil op(String param) {
        return new OperationsUtil(param);
    }

    /**
     * ダウンロード
     */
    @GetMapping("{uuid}/download")
    public String download(Model model, @PathVariable("uuid") String uuid,
                           @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.DOWNLOAD, loggedInUser);

        model.addAttribute(fileManagedService.findByUuid(uuid));
        return "fileManagedDownloadView";
    }

    /**
     * 編集画面を開く
     */
    @GetMapping(value = "{id}/update", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String updateForm(AccessCounterForm form, Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        jp.co.stnet.cms.base.domain.model.common.AccessCounter accessCounter = accessCounterService.findById(id);
        model.addAttribute("accessCounter", accessCounter);

        // 状態=無効の場合、参照画面に強制遷移
        if (accessCounter.getStatus().equals(Status.INVALID.getCodeValue())) {
            model.addAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0008));
            return view(model, loggedInUser, id);
        }

        // 入力チェック再表示の場合、formの情報をDBの値で上書きしない
        if (form.getVersion() == null) {
            beanMapper.map(accessCounter, form);
        }

        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.UPDATE, accessCounter).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.UPDATE, accessCounter).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }

    /**
     * 更新
     */
    @PostMapping(value = "{id}/update")
    @TransactionTokenCheck
    public String update(@Validated({AccessCounterForm.Update.class, Default.class}) AccessCounterForm form,
                         BindingResult bindingResult, Model model, RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return updateForm(form, model, loggedInUser, id);
        }

        jp.co.stnet.cms.base.domain.model.common.AccessCounter accessCounter = beanMapper.map(form, jp.co.stnet.cms.base.domain.model.common.AccessCounter.class);
        try {
            accessCounter.setStatus(Status.VALID.getCodeValue());
            accessCounterService.save(accessCounter);

        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return updateForm(form, model, loggedInUser, id);
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0004));
        return REDIRECT + op().getEditUrl(accessCounter.getId().toString());
    }

    /**
     * 削除
     */
    @GetMapping(value = "{id}/delete")
    public String delete(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.DELETE, loggedInUser);

        try {
            accessCounterService.delete(id);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0003));

        return REDIRECT + op().getListUrl();
    }

    /**
     * 無効化
     */
    @GetMapping(value = "{id}/invalid")
    public String invalid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                          @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.INVALID, loggedInUser);

        accessCounterService.findById(id);

        try {
            accessCounterService.invalid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return REDIRECT + op().getEditUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return REDIRECT + op().getViewUrl(id.toString());
    }

    /**
     * 無効解除
     */
    @GetMapping(value = "{id}/valid")
    public String valid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                        @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.VALID, loggedInUser);

        // 存在チェックを兼ねる
        accessCounterService.findById(id);

        try {
            accessCounterService.valid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return REDIRECT + op().getViewUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return REDIRECT + op().getEditUrl(id.toString());
    }

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{id}")
    public String view(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                       @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.VIEW, loggedInUser);

        jp.co.stnet.cms.base.domain.model.common.AccessCounter accessCounter = accessCounterService.findById(id);
        model.addAttribute("accessCounter", accessCounter);
        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.VIEW, accessCounter).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.VIEW, accessCounter).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }

    /**
     * ボタンの状態設定
     */
    private StateMap getButtonStateMap(String operation, jp.co.stnet.cms.base.domain.model.common.AccessCounter accessCounter) {

        if (accessCounter == null) {
            accessCounter = new jp.co.stnet.cms.base.domain.model.common.AccessCounter();
        }

        List<String> includeKeys = new ArrayList<>();
        includeKeys.add(Constants.BUTTON.GOTOLIST);
        includeKeys.add(Constants.BUTTON.GOTOUPDATE);
        includeKeys.add(Constants.BUTTON.VIEW);
        includeKeys.add(Constants.BUTTON.SAVE);
        includeKeys.add(Constants.BUTTON.INVALID);
        includeKeys.add(Constants.BUTTON.VALID);
        includeKeys.add(Constants.BUTTON.DELETE);

        StateMap buttonState = new StateMap(Default.class, includeKeys, new ArrayList<>());

        // 常に表示
        buttonState.setViewTrue(Constants.BUTTON.GOTOLIST);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            buttonState.setViewTrue(Constants.BUTTON.SAVE);
        }

        // 編集
        if (Constants.OPERATION.UPDATE.equals(operation)) {

            if (Status.DRAFT.getCodeValue().equals(accessCounter.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
            }

            if (Status.VALID.getCodeValue().equals(accessCounter.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
            }

            if (Status.INVALID.getCodeValue().equals(accessCounter.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
            }

        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {

            // スタータスが公開時
            if (Status.VALID.getCodeValue().equals(accessCounter.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
            }

            // スタータスが無効
            if (Status.INVALID.getCodeValue().equals(accessCounter.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
            }
        }

        return buttonState;
    }

    /**
     * フィールドの状態設定
     */
    private StateMap getFiledStateMap(String operation, jp.co.stnet.cms.base.domain.model.common.AccessCounter accessCounter) {

        // 常設の隠しフィールドは状態管理しない
        List<String> excludeKeys = new ArrayList<>();
        excludeKeys.add("id");
        excludeKeys.add("version");

        StateMap fieldState = new StateMap(AccessCounterForm.class, new ArrayList<>(), excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
        }

        // 編集
        if (Constants.OPERATION.UPDATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setViewTrue("status");

            // スタータスが無効
            if (Status.INVALID.toString().equals(accessCounter.getStatus())) {
                fieldState.setReadOnlyTrueAll();
            }
        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {
            fieldState.setViewTrueAll();
        }

        return fieldState;
    }


}
