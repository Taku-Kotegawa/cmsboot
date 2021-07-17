package jp.co.stnet.cms.base.presentation.controller.admin.variable;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.codelist.CodeListService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.base.presentation.controller.admin.upload.UploadForm;
import jp.co.stnet.cms.base.presentation.controller.job.JobStarter;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.common.util.CsvUtils;
import jp.co.stnet.cms.common.util.StateMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.codelist.CodeList;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import javax.inject.Named;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("admin/variable")
public class VariableController {

    private final String BASE_PATH = "admin/variable";
    private final String JSP_LIST = BASE_PATH + "/list";
    private final String JSP_FORM = BASE_PATH + "/form";
    private final String JSP_VIEW = BASE_PATH + "/view";
    private final String JSP_UPLOAD_FORM = BASE_PATH + "/uploadform";
    private final String JSP_UPLOAD_COMPLETE = "common/upload/complete";

    // CSV/Excelのファイル名(拡張子除く)
    private final String DOWNLOAD_FILENAME = "variable";

    // アップロード用のインポートジョブID
    private final String UPLOAD_JOB_ID = Constants.JOBID.IMPORT_VARIABLE;

    @Autowired
    VariableService variableService;

    @Autowired
    VariableAuthority authority;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    CodeListService codeListService;

    @Autowired
    JobStarter jobStarter;

    @Autowired
    @Named("CL_STATUS")
    CodeList statusCodeList;

    @Autowired
    Mapper beanMapper;

    @ModelAttribute
    private VariableForm setUp() {
        return new VariableForm();
    }

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list")
    public String list(Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        for (VariableType v : VariableType.values()) {
            redirectAttributes.addFlashAttribute("stateSaveClear", "true");
            return "redirect:/admin/variable/list?type=" + v.name();
        }

        return JSP_LIST;
    }

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list", params = "type")
    public String listByType(Model model, @RequestParam(value = "type", required = false) String type, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        VariableType variableType = null;
        if (type != null && VariableType.valueOf(type) != null) {
            variableType = VariableType.valueOf(type);

        } else if (VariableType.values().length > 0) {
            variableType = VariableType.values()[0];
        }

        model.addAttribute("variableType", variableType);
        model.addAttribute("columnVisible", getJSArrayColumnVisible(variableType));

        return JSP_LIST;
    }

    private String getJSArrayColumnVisible(VariableType variableType) {

        String javaScriptArray = "";

        if (variableType == null) {
            throw new IllegalArgumentException();
        }

        javaScriptArray += "let columnVisible = {};\n";
        javaScriptArray += "columnVisible['value1'] = " + (variableType.getLabelValue1().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value2'] = " + (variableType.getLabelValue2().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value3'] = " + (variableType.getLabelValue3().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value4'] = " + (variableType.getLabelValue4().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value5'] = " + (variableType.getLabelValue5().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value6'] = " + (variableType.getLabelValue6().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value7'] = " + (variableType.getLabelValue7().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value8'] = " + (variableType.getLabelValue8().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value9'] = " + (variableType.getLabelValue9().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['value10'] = " + (variableType.getLabelValue10().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['valint1'] = " + (variableType.getLabelValint1().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['valint2'] = " + (variableType.getLabelValint2().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['valint3'] = " + (variableType.getLabelValint3().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['valint4'] = " + (variableType.getLabelValint4().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['valint5'] = " + (variableType.getLabelValint5().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['date1'] = " + (variableType.getLabelDate1().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['date2'] = " + (variableType.getLabelDate2().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['date3'] = " + (variableType.getLabelDate3().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['date4'] = " + (variableType.getLabelDate4().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['date5'] = " + (variableType.getLabelDate5().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['textarea'] = " + (variableType.getLabelTextarea().isEmpty() ? "false;" : "true;") + "\n";
        javaScriptArray += "columnVisible['faile1'] = " + (variableType.getLabelFile1().isEmpty() ? "false;" : "true;") + "\n";

        return javaScriptArray;
    }

    /**
     * DataTables用のJSONの作成
     *
     * @param input DataTablesから要求
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "/list/json")
    public DataTablesOutput<VariableListRow> listJson(@Validated() DataTablesInputDraft input, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        OperationsUtil op = new OperationsUtil(null);

        List<VariableListRow> list = new ArrayList<>();
        List<Variable> variableList = new ArrayList<>();
        long recordsFiltered = 0L;


//        if (input.getDraft() == null || input.getDraft()) { // 下書き含む最新
        Page<Variable> variablePage = variableService.findPageByInput(input);
        variableList.addAll(variablePage.getContent());
        recordsFiltered = variablePage.getTotalElements();

//        } else {
//            Page<VariableRevision> variablePage2 = variableService.findMaxRevPageByInput(input);
//            for (VariableRevision variableRevision : variablePage2.getContent()) {
//                variableList.add(beanMapper.map(variableRevision, Variable.class));
//            }
//            recordsFiltered = variablePage2.getTotalElements();
//        }

        for (Variable variable : variableList) {
            VariableListRow variableListRow = beanMapper.map(variable, VariableListRow.class);
            variableListRow.setOperations(getToggleButton(variable.getId().toString(), op));
            variableListRow.setDT_RowId(variable.getId().toString());

            // ステータスラベル
            variableListRow.setStatusLabel(Status.getByValue(variable.getStatus()).getCodeLabel());

            list.add(variableListRow);
        }

        DataTablesOutput<VariableListRow> output = new DataTablesOutput<>();
        output.setData(list);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(recordsFiltered);

        return output;
    }

    @GetMapping(value = "/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getCsvDefault());
        model.addAttribute("csvFileName", "Variable.csv");
//        model.addAttribute("handler", VariableCsvBean.getHandler());
        return "csvDownloadView";
    }

    @GetMapping(value = "/list/tsv")
    public String listTsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getTsvDefault());
        model.addAttribute("csvFileName", "Variable.tsv");
//        model.addAttribute("handler", VariableCsvBean.getHandler());
        return "csvDownloadView";
    }

    private void setModelForCsv(DataTablesInputDraft input, Model model) {
        input.setStart(0);
        input.setLength(Constants.CSV.MAX_LENGTH);

        List<VariableCsvBean> list = new ArrayList<>();
        List<Variable> variableList = new ArrayList<>();

//        if (input.getDraft()) { // 下書き含む最新
        Page<Variable> variablePage = variableService.findPageByInput(input);
        variableList.addAll(variablePage.getContent());

//        } else {
//            Page<VariableRevision> variablePage2 = variableService.findMaxRevPageByInput(input);
//            for (VariableRevision variableRevision : variablePage2.getContent()) {
//                variableList.add(beanMapper.map(variableRevision, Variable.class));
//            }
//        }

        for (Variable variable : variableList) {
            VariableCsvBean row = beanMapper.map(variable, VariableCsvBean.class);
            row.setStatusLabel(Status.getByValue(variable.getStatus()).getCodeLabel());
            list.add(row);
        }

        model.addAttribute("exportCsvData", list);
        model.addAttribute("class", VariableCsvBean.class);
    }


    private String getToggleButton(String id, OperationsUtil op) {

        StringBuffer link = new StringBuffer();
        link.append("<div class=\"btn-group\">");
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
        link.append("<button type=\"button\" class=\"btn btn-button btn-sm dropdown-toggle dropdown-toggle-split\"data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">");
        link.append("</button>");
        link.append("<div class=\"dropdown-menu\">");
        link.append("<a class=\"dropdown-item\" href=\"" + op.getViewUrl(id) + "\">" + op.getLABEL_VIEW() + "</a>");
        link.append("<a class=\"dropdown-item\" href=\"" + op.getCopyUrl(id) + "\">" + op.getLABEL_COPY() + "</a>");
        link.append("<a class=\"dropdown-item\" href=\"" + op.getInvalidUrl(id) + "\">" + op.getLABEL_INVALID() + "</a>");
        link.append("</div>");
        link.append("</div>");

        return link.toString();
    }

    protected OperationsUtil op() {
        return new OperationsUtil(BASE_PATH);
    }

    protected OperationsUtil op(String variableType) {
        OperationsUtil op = new OperationsUtil(BASE_PATH);
        op.setURL_LIST("list?type=" + variableType);
        return op;
    }

    /**
     * 新規作成画面を開く
     */
    @GetMapping(value = "create", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String createForm(VariableForm form,
                             Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @RequestParam(value = "copy", required = false) Long copy,
                             @RequestParam(value = "variable_type", required = false) String variableType) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (copy != null) {
            Variable source = variableService.findById(copy);
            beanMapper.map(source, form);
            form.setId(null);
            form.setCode(null);
            form.setVersion(null);
            variableType = source.getType();
        }

        form.setType(variableType);

        if (form.getFile1Uuid() != null) {
            form.setFile1Managed(fileManagedSharedService.findByUuid(form.getFile1Uuid()));
        }

        Variable variable = beanMapper.map(form, Variable.class);

        model.addAttribute("variable", variable);
        model.addAttribute("fieldLabel", getFieldLabel(variableType));
        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.CREATE, variable).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.CREATE, variable).asMap());
        model.addAttribute("op", op(variableType));

        return JSP_FORM;
    }

    /**
     * 新規登録
     */
    @PostMapping(value = "create")
    @TransactionTokenCheck
    public String create(@Validated({VariableForm.Create.class, Default.class}) VariableForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return createForm(form, model, loggedInUser, null, form.getType());
        }

        Variable variable = beanMapper.map(form, Variable.class);

        try {
//            if ("true".equals(saveDraft)) {
//                variable.setStatus(Status.VALID.getCodeValue());
//                variableService.saveDraft(variable);
//            } else {
            variable.setStatus(Status.VALID.getCodeValue());
            variableService.save(variable);
//            }
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return createForm(form, model, loggedInUser, null, form.getType());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0001));

        return "redirect:" + op().getEditUrl(variable.getId().toString());
    }

    /**
     * 編集画面を開く
     */
    @GetMapping(value = "{id}/update", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String updateForm(VariableForm form, Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        Variable variable = variableService.findById(id);

        // 状態=無効の場合、参照画面に強制遷移
        if (variable.getStatus().equals(Status.INVALID.getCodeValue())) {
            model.addAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0008));
            return view(model, loggedInUser, id, null);
        }

        // 入力チェック再表示の場合、formの情報をDBの値で上書きしない
        if (form.getVersion() == null) {
            beanMapper.map(variable, form);
        }

        if (form.getFile1Uuid() != null) {
            form.setFile1Managed(fileManagedSharedService.findByUuid(form.getFile1Uuid()));
        }

        model.addAttribute("variable", variable);
        model.addAttribute("fieldLabel", getFieldLabel(form.getType()));
        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.UPDATE, variable).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.UPDATE, variable).asMap());
        model.addAttribute("op", op(variable.getType()));

        return JSP_FORM;
    }

    /**
     * 更新
     */
    @PostMapping(value = "{id}/update")
    @TransactionTokenCheck
    public String update(@Validated({VariableForm.Update.class, Default.class}) VariableForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("id") Long id,
                         @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return updateForm(form, model, loggedInUser, id);
        }

        Variable variable = beanMapper.map(form, Variable.class);

        try {
//            if ("true".equals(saveDraft)) {
//                variableService.saveDraft(variable);
//            } else {
            variable.setStatus(Status.VALID.getCodeValue());
            variableService.save(variable);
//            }
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return updateForm(form, model, loggedInUser, id);
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0004));

        return "redirect:" + op().getEditUrl(variable.getId().toString());
    }

    /**
     * 削除
     */
    @GetMapping(value = "{id}/delete")
    public String delete(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.DELETE, loggedInUser);

        try {
            variableService.delete(id);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0007));

        return "redirect:" + op().getListUrl();
    }

    /**
     * 無効化
     */
    @GetMapping(value = "{id}/invalid")
    public String invalid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                          @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.INVALID, loggedInUser);

        Variable entity = variableService.findById(id);

        try {
            entity = variableService.invalid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op().getEditUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return "redirect:" + op().getViewUrl(id.toString());
    }

    /**
     * 無効解除
     */
    @GetMapping(value = "{id}/valid")
    public String valid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                        @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.VALID, loggedInUser);

        Variable entity = variableService.findById(id);

        try {
            entity = variableService.valid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op().getViewUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return "redirect:" + op().getEditUrl(id.toString());
    }

//    /**
//     * 下書き取消
//     */
//    @GetMapping(value = "{id}/cancel_draft")
//    public String cancelDraft(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
//                              @PathVariable("id") Long id) {
//
//        variableService.hasAuthority(Constants.OPERATION.CANCEL_DRAFT, loggedInUser);
//
//        Variable entity = null;
//        try {
//            entity = variableService.cancelDraft(id);
//        } catch (BusinessException e) {
//            redirect.addFlashAttribute(e.getResultMessages());
//            return "redirect:" + op().getEditUrl(id.toString());
//        }
//
//        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));
//
//        if (entity != null) {
//            return "redirect:" + op().getEditUrl(id.toString());
//        } else {
//            return "redirect:" + op().getListUrl();
//        }
//    }

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{id}")
    public String view(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                       @PathVariable("id") Long id,
                       @RequestParam(value = "rev", required = false) Long rev) {

        authority.hasAuthority(Constants.OPERATION.VIEW, loggedInUser);

        Variable variable = null;
//        if (rev == null) {
        // 下書きを含む最新
        variable = variableService.findById(id);

//        } else if (rev == 0) {
        // 有効な最新リビジョン
//            variable = beanMapper.map(variableService.findByIdLatestRev(id), Variable.class);

//        } else {
        // リビジョン番号指定
//            variable = beanMapper.map(variableService.findByRid(rev), Variable.class);
//        }


        if (variable.getFile1Uuid() != null) {
            variable.setFile1Managed(fileManagedSharedService.findByUuid(variable.getFile1Uuid()));
        }

        model.addAttribute("variable", variable);
        model.addAttribute("fieldLabel", getFieldLabel(variable.getType()));
        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.VIEW, variable).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.VIEW, variable).asMap());
        model.addAttribute("op", op(variable.getType()));

        return JSP_FORM;
    }

    /**
     * ダウンロード
     */
    @GetMapping("{uuid}/download")
    public String download(
            Model model,
            @PathVariable("uuid") String uuid,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.DOWNLOAD, loggedInUser);

        model.addAttribute(fileManagedSharedService.findByUuid(uuid));
        return "fileManagedDownloadView";
    }

    /**
     * アップロードファイル指定画面の表示
     */
    @GetMapping(value = "upload", params = "form")
    public String uploadForm(@ModelAttribute UploadForm form, Model model,
                             @RequestParam(value = "variable_type", required = false) String variableType,
                             @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.UPLOAD, loggedInUser);

        form.setJobName(UPLOAD_JOB_ID);

        if (form.getUploadFileUuid() != null) {
            form.setUploadFileManaged(fileManagedSharedService.findByUuid(form.getUploadFileUuid()));
        }

        model.addAttribute("pageTitle", "Import Variable");
        model.addAttribute("variableType", variableType);
        model.addAttribute("referer", "list?type=" + variableType);
        model.addAttribute("fieldState", new StateMap(UploadForm.class, new ArrayList<>(), new ArrayList<>()).setInputTrueAll().asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return JSP_UPLOAD_FORM;
    }

    /**
     * アップロード処理(バッチ実行)
     */
    @PostMapping(value = "upload")
    public String upload(@Validated UploadForm form, BindingResult result, Model model,
                         @RequestParam(value = "variable_type", required = false) String variableType,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.UPLOAD, loggedInUser);

        final String jobName = UPLOAD_JOB_ID;

        Long jobExecutionId = null;

        if (!jobName.equals(form.getJobName()) || result.hasErrors()) {
            return uploadForm(form, model, variableType, loggedInUser);
        }

        FileManaged uploadFile = fileManagedSharedService.findByUuid(form.getUploadFileUuid());
        String uploadFileAbsolutePath = fileManagedSharedService.getFileStoreBaseDir() + uploadFile.getUri();
        String jobParams = "inputFile=" + uploadFileAbsolutePath;
        jobParams += ", encoding=" + form.getEncoding();
        jobParams += ", filetype=" + form.getFileType();

        try {
            jobExecutionId = jobStarter.start(jobName, jobParams);

        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();

            // メッセージをセットして、フォーム画面に戻る。

        }

        redirectAttributes.addAttribute("jobName", jobName);
        redirectAttributes.addAttribute("jobExecutionId", jobExecutionId);

        return "redirect:upload?complete";
    }

    /**
     * アップロード完了画面
     */
    @GetMapping(value = "upload", params = "complete")
    public String uploadComplete(Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        model.addAttribute("returnBackBtn", "一覧画面に戻る");
        model.addAttribute("returnBackUrl", op().getListUrl());
        model.addAttribute("jobName", params.get("jobName"));
        model.addAttribute("jobExecutionId", params.get("jobExecutionId"));
        return JSP_UPLOAD_COMPLETE;
    }

    /**
     * コードリスト更新
     * @param model
     * @param redirect
     * @param type
     * @param loggedInUser
     * @return
     */
    @GetMapping("refresh")
    public String refresh(Model model, RedirectAttributes redirect, @RequestParam(value = "type") String type,
                          @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        codeListService.refresh(type);

        redirect.addFlashAttribute(ResultMessages.info().add("i.sl.va.0001"));

        return "redirect:" + op(type).getURL_LIST();
    }


    /**
     * ボタンの状態設定
     */
    private StateMap getButtonStateMap(String operation, Variable record) {

        if (record == null) {
            record = new Variable();
        }

        List<String> includeKeys = new ArrayList<>();
        includeKeys.add(Constants.BUTTON.GOTOLIST);
        includeKeys.add(Constants.BUTTON.GOTOUPDATE);
        includeKeys.add(Constants.BUTTON.VIEW);
        includeKeys.add(Constants.BUTTON.SAVE);
        includeKeys.add(Constants.BUTTON.INVALID);
        includeKeys.add(Constants.BUTTON.VALID);
        includeKeys.add(Constants.BUTTON.DELETE);
        includeKeys.add(Constants.BUTTON.UNLOCK);
//        includeKeys.add(Constants.BUTTON.SAVE_DRAFT);
//        includeKeys.add(Constants.BUTTON.CANCEL_DRAFT);

        StateMap buttonState = new StateMap(Default.class, includeKeys, new ArrayList<>());

        // 常に表示
        buttonState.setViewTrue(Constants.BUTTON.GOTOLIST);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            buttonState.setViewTrue(Constants.BUTTON.SAVE);
//            buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
        }

        // 編集
        if (Constants.OPERATION.UPDATE.equals(operation)) {

            if (Status.DRAFT.getCodeValue().equals(record.getStatus())) {
//                buttonState.setViewTrue(Constants.BUTTON.CANCEL_DRAFT);
//                buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
            }

            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
//                buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
            }

            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
            }

        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {

            // スタータスが公開時
            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
                buttonState.setViewTrue(Constants.BUTTON.UNLOCK);
            }

            // スタータスが無効
            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
            }
        }

        return buttonState;
    }

    /**
     * フィールドの状態設定
     */
    private StateMap getFiledStateMap(String operation, Variable record) {

        // 常設の隠しフィールドは状態管理しない
        List<String> excludeKeys = new ArrayList<>();
        excludeKeys.add("id");
        excludeKeys.add("version");
        excludeKeys.add("file1Managed");
        excludeKeys.add("file1Managed-createdBy");
        excludeKeys.add("file1Managed-createdDate");
        excludeKeys.add("file1Managed-fid");
        excludeKeys.add("file1Managed-filemime");
        excludeKeys.add("file1Managed-filesize");
        excludeKeys.add("file1Managed-filetype");
        excludeKeys.add("file1Managed-lastModifiedBy");
        excludeKeys.add("file1Managed-lastModifiedDate");
        excludeKeys.add("file1Managed-originalFilename");
        excludeKeys.add("file1Managed-status");
        excludeKeys.add("file1Managed-uri");
        excludeKeys.add("file1Managed-uuid");
        excludeKeys.add("file1Managed-version");

        StateMap fieldState = new StateMap(VariableForm.class, new ArrayList<>(), excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setInputFalse("type").setHiddenTrue("type").setViewTrue("type");
        }

        // 編集
        if (Constants.OPERATION.UPDATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setInputFalse("type").setHiddenTrue("type").setViewTrue("type");
            fieldState.setViewTrue("status");
            fieldState.setReadOnlyTrue("code");

            // スタータスが無効
            if (Status.INVALID.toString().equals(record.getStatus())) {
                fieldState.setReadOnlyTrueAll();
            }
        }

        // ラベル未設定フィールドは入力不可
        VariableType variableType = null;
        if (record.getType() != null) {
            variableType = VariableType.valueOf(record.getType());
        }

        if (variableType != null) {
            if (variableType.getLabelValue1().isEmpty()) {
                fieldState.setInputFalse("value1");
            }
            if (variableType.getLabelValue2().isEmpty()) {
                fieldState.setInputFalse("value2");
            }
            if (variableType.getLabelValue3().isEmpty()) {
                fieldState.setInputFalse("value3");
            }
            if (variableType.getLabelValue4().isEmpty()) {
                fieldState.setInputFalse("value4");
            }
            if (variableType.getLabelValue5().isEmpty()) {
                fieldState.setInputFalse("value5");
            }
            if (variableType.getLabelValue6().isEmpty()) {
                fieldState.setInputFalse("value6");
            }
            if (variableType.getLabelValue7().isEmpty()) {
                fieldState.setInputFalse("value7");
            }
            if (variableType.getLabelValue8().isEmpty()) {
                fieldState.setInputFalse("value8");
            }
            if (variableType.getLabelValue9().isEmpty()) {
                fieldState.setInputFalse("value9");
            }
            if (variableType.getLabelValue10().isEmpty()) {
                fieldState.setInputFalse("value10");
            }
            if (variableType.getLabelValint1().isEmpty()) {
                fieldState.setInputFalse("valint1");
            }
            if (variableType.getLabelValint2().isEmpty()) {
                fieldState.setInputFalse("valint2");
            }
            if (variableType.getLabelValint3().isEmpty()) {
                fieldState.setInputFalse("valint3");
            }
            if (variableType.getLabelValint4().isEmpty()) {
                fieldState.setInputFalse("valint4");
            }
            if (variableType.getLabelValint5().isEmpty()) {
                fieldState.setInputFalse("valint5");
            }
            if (variableType.getLabelDate1().isEmpty()) {
                fieldState.setInputFalse("date1");
            }
            if (variableType.getLabelDate2().isEmpty()) {
                fieldState.setInputFalse("date2");
            }
            if (variableType.getLabelDate3().isEmpty()) {
                fieldState.setInputFalse("date3");
            }
            if (variableType.getLabelDate4().isEmpty()) {
                fieldState.setInputFalse("date4");
            }
            if (variableType.getLabelDate5().isEmpty()) {
                fieldState.setInputFalse("date5");
            }
            if (variableType.getLabelTextarea().isEmpty()) {
                fieldState.setInputFalse("textarea");
            }
            if (variableType.getLabelFile1().isEmpty()) {
                fieldState.setInputFalse("file1Uuid");
            }
        }

//        if (record != null && record.getType() != null) {
//            List<Variable> variables = variableSharedService.findAllByTypeAndCode(VariableType.VARIABLE_LABEL.getCodeValue(), record.getType());
//            if (variables.size() > 0 && variables.get(0).getTextarea() != null) {
//                String[] t = variables.get(0).getTextarea().split(",");
//                for (int i = 0; i < t.length; i++) {
//                    String[] v = t[i].split("=");
//                    if (v.length == 1) {
//                        fieldState.setInputFalse(v[0].trim());
//                    }
//                }
//            }
//        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {
            fieldState.setViewTrueAll();

            if (variableType != null) {
                if (variableType.getLabelValue1().isEmpty()) {
                    fieldState.setViewFalse("value1");
                }
                if (variableType.getLabelValue2().isEmpty()) {
                    fieldState.setViewFalse("value2");
                }
                if (variableType.getLabelValue3().isEmpty()) {
                    fieldState.setViewFalse("value3");
                }
                if (variableType.getLabelValue4().isEmpty()) {
                    fieldState.setViewFalse("value4");
                }
                if (variableType.getLabelValue5().isEmpty()) {
                    fieldState.setViewFalse("value5");
                }
                if (variableType.getLabelValue6().isEmpty()) {
                    fieldState.setViewFalse("value6");
                }
                if (variableType.getLabelValue7().isEmpty()) {
                    fieldState.setViewFalse("value7");
                }
                if (variableType.getLabelValue8().isEmpty()) {
                    fieldState.setViewFalse("value8");
                }
                if (variableType.getLabelValue9().isEmpty()) {
                    fieldState.setViewFalse("value9");
                }
                if (variableType.getLabelValue10().isEmpty()) {
                    fieldState.setViewFalse("value10");
                }
                if (variableType.getLabelValint1().isEmpty()) {
                    fieldState.setViewFalse("valint1");
                }
                if (variableType.getLabelValint2().isEmpty()) {
                    fieldState.setViewFalse("valint2");
                }
                if (variableType.getLabelValint3().isEmpty()) {
                    fieldState.setViewFalse("valint3");
                }
                if (variableType.getLabelValint4().isEmpty()) {
                    fieldState.setViewFalse("valint4");
                }
                if (variableType.getLabelValint5().isEmpty()) {
                    fieldState.setViewFalse("valint5");
                }
                if (variableType.getLabelDate1().isEmpty()) {
                    fieldState.setViewFalse("date1");
                }
                if (variableType.getLabelDate2().isEmpty()) {
                    fieldState.setViewFalse("date2");
                }
                if (variableType.getLabelDate3().isEmpty()) {
                    fieldState.setViewFalse("date3");
                }
                if (variableType.getLabelDate4().isEmpty()) {
                    fieldState.setViewFalse("date4");
                }
                if (variableType.getLabelDate5().isEmpty()) {
                    fieldState.setViewFalse("date5");
                }
                if (variableType.getLabelTextarea().isEmpty()) {
                    fieldState.setViewFalse("textarea");
                }
                if (variableType.getLabelFile1().isEmpty()) {
                    fieldState.setViewFalse("file1Uuid");
                }
            }

        }

        return fieldState;
    }

    private Map<String, String> getFieldLabel(String code) {
        Map<String, String> labels = new HashMap<>();
        labels.put("value1", "値１");
        labels.put("value2", "値２");
        labels.put("value3", "値３");
        labels.put("value4", "値４");
        labels.put("value5", "値５");
        labels.put("value6", "値６");
        labels.put("value7", "値７");
        labels.put("value8", "値８");
        labels.put("value9", "値９");
        labels.put("value10", "値１０");
        labels.put("valint1", "数値１");
        labels.put("valint2", "数値２");
        labels.put("valint3", "数値３");
        labels.put("valint4", "数値４");
        labels.put("valint5", "数値５");
        labels.put("date1", "日付１");
        labels.put("date2", "日付２");
        labels.put("date3", "日付３");
        labels.put("date4", "日付４");
        labels.put("date5", "日付５");
        labels.put("textarea", "テキストエリア");
        labels.put("file1Uuid", "ファイル");
        labels.put("remark", "備考");


        VariableType variableType = null;
        if (code != null) {
            variableType = VariableType.valueOf(code);
        }

        if (variableType != null) {

            labels.put("value1", variableType.getLabelValue1());
            labels.put("value2", variableType.getLabelValue2());
            labels.put("value3", variableType.getLabelValue3());
            labels.put("value4", variableType.getLabelValue4());
            labels.put("value5", variableType.getLabelValue5());
            labels.put("value6", variableType.getLabelValue6());
            labels.put("value7", variableType.getLabelValue7());
            labels.put("value8", variableType.getLabelValue8());
            labels.put("value9", variableType.getLabelValue9());
            labels.put("value10", variableType.getLabelValue10());
            labels.put("valint1", variableType.getLabelValint1());
            labels.put("valint2", variableType.getLabelValint2());
            labels.put("valint3", variableType.getLabelValint3());
            labels.put("valint4", variableType.getLabelValint4());
            labels.put("valint5", variableType.getLabelValint5());
            labels.put("date1", variableType.getLabelDate1());
            labels.put("date2", variableType.getLabelDate2());
            labels.put("date3", variableType.getLabelDate3());
            labels.put("date4", variableType.getLabelDate4());
            labels.put("date5", variableType.getLabelDate5());
            labels.put("textarea", variableType.getLabelTextarea());
            labels.put("file1Uuid", variableType.getLabelFile1());
        }

//        List<Variable> variables = variableSharedService.findAllByTypeAndCode(VariableType.VARIABLE_LABEL.getCodeValue(), code);
//        if (variables.size() > 0 && variables.get(0).getTextarea() != null) {
//            String[] t = variables.get(0).getTextarea().split(",");
//            for (int i = 0; i < t.length; i++) {
//                String[] v = t[i].split("=");
//                if (v.length == 2) {
//                    labels.put(v[0].trim(), StringUtils.stripToEmpty(v[1]));
//                } else if (v.length == 1) {
//                    labels.put(v[0].trim(), "");
//                }
//            }
//        }
        return labels;
    }

}
