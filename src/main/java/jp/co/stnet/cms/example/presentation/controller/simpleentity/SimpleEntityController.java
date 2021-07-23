package jp.co.stnet.cms.example.presentation.controller.simpleentity;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.presentation.controller.admin.upload.UploadForm;
import jp.co.stnet.cms.base.presentation.controller.job.JobStarter;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.common.util.CsvUtils;
import jp.co.stnet.cms.common.util.StateMap;
import jp.co.stnet.cms.example.application.service.SimpleEntityService;
import jp.co.stnet.cms.example.domain.model.simpleentity.LineItem;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityRevision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobOperator;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("example/simpleentity")
@TransactionTokenCheck("example/simpleentity")
public class SimpleEntityController {

    // JSPのパス設定
    private final String BASE_PATH = "example/simpleentity";
    private final String JSP_LIST = BASE_PATH + "/list";
    private final String JSP_FORM = BASE_PATH + "/form";
    private final String JSP_VIEW = BASE_PATH + "/view";

    private final String JSP_UPLOAD_FORM = BASE_PATH + "/uploadform";
    private final String JSP_UPLOAD_COMPLETE = "common/upload/complete";

    // CSV/Excelのファイル名(拡張子除く)
    private final String DOWNLOAD_FILENAME = "simpleentity";

    // アップロード用のインポートジョブID
    private final String UPLOAD_JOB_ID = "job03";

    @Autowired
    SimpleEntityService simpleEntityService;

    @Autowired
    FileManagedService fileManagedService;

    @Autowired
    SimpleEntityAuthority authority;

    @Autowired
    JobOperator jobOperator;

    @Autowired
    JobStarter jobStarter;

    @Autowired
    @Named("CL_STATUS")
    CodeList statusCodeList;

    @Autowired
    Mapper beanMapper;

    @ModelAttribute
    private SimpleEntityForm setUp() {
        return new SimpleEntityForm();
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
    public DataTablesOutput<SimpleEntityListRow> listJson(@Validated DataTablesInputDraft input,
                                                          @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        List<SimpleEntityListRow> listRows = new ArrayList<>();
        List<SimpleEntity> simpleEntityList = new ArrayList<>();
        Long recordsFiltered = 0L;

        if (input.getDraft()) { // 下書き含む最新
            Page<SimpleEntity> simpleEntityPage = simpleEntityService.findPageByInput(input);
            simpleEntityList.addAll(simpleEntityPage.getContent());
            recordsFiltered = simpleEntityPage.getTotalElements();

        } else {
            Page<SimpleEntityRevision> simpleEntityPage2 = simpleEntityService.findMaxRevPageByInput(input);
            for (SimpleEntityRevision simpleEntityRevision : simpleEntityPage2.getContent()) {
                simpleEntityList.add(beanMapper.map(simpleEntityRevision, SimpleEntity.class));
            }
            recordsFiltered = simpleEntityPage2.getTotalElements();
        }

        for (SimpleEntityBean bean : getBeanList(simpleEntityList)) {
            SimpleEntityListRow simpleEntityListRow = beanMapper.map(bean, SimpleEntityListRow.class);
            simpleEntityListRow.setOperations(getToggleButton(bean.getId().toString(), op()));
            simpleEntityListRow.setDT_RowId(bean.getId().toString());
            // ステータスラベル
            simpleEntityListRow.setStatusLabel(Status.getByValue(bean.getStatus()).getCodeLabel());
            listRows.add(simpleEntityListRow);
        }

        DataTablesOutput<SimpleEntityListRow> output = new DataTablesOutput<>();
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
    public String listCsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

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
    public String listTsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        input.setStart(0);
        input.setLength(Constants.CSV.MAX_LENGTH);
        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getTsvDefault());
        model.addAttribute("csvFileName", DOWNLOAD_FILENAME + ".tsv");
        return "csvDownloadView";
    }

    /**
     * Excelファイルのダウンロード
     *
     * @param input DataTablesからの要求(Server-side処理)
     * @param model モデル
     * @return ファイルダウンロード用View
     */
    @GetMapping(value = "/list/excel")
    public String listExcel(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);

        input.setStart(0);
        input.setLength(Constants.EXCEL.MAX_LENGTH);
        // TODO: 汎用的な仕組みに改造が必要
        model.addAttribute("list", simpleEntityService.findPageByInput(input).getContent());
        model.addAttribute("excelFileName", DOWNLOAD_FILENAME + ".xlsx");
        return "excelDownloadView";
    }

    /**
     * csvDownloadViewに渡すデータ準備
     *
     * @param input DataTablesからの要求(Server-side処理)
     * @param model データをセットするモデル
     */
    private void setModelForCsv(DataTablesInputDraft input, Model model) {

        List<SimpleEntityCsvBean> csvBeans = new ArrayList<>();
        List<SimpleEntity> simpleEntityList = new ArrayList<>();

        if (input.getDraft() == null || input.getDraft()) { // 下書き含む最新
            Page<SimpleEntity> simpleEntityPage = simpleEntityService.findPageByInput(input);
            simpleEntityList.addAll(simpleEntityPage.getContent());

        } else {
            Page<SimpleEntityRevision> simpleEntityPage2 = simpleEntityService.findMaxRevPageByInput(input);
            for (SimpleEntityRevision simpleEntityRevision : simpleEntityPage2.getContent()) {
                simpleEntityList.add(beanMapper.map(simpleEntityRevision, SimpleEntity.class));
            }
        }

        for (SimpleEntity simpleEntity : getBeanList(simpleEntityList)) {
            SimpleEntityCsvBean row = beanMapper.map(simpleEntity, SimpleEntityCsvBean.class);
            customMap(row, simpleEntity);
            row.setStatusLabel(Status.getByValue(simpleEntity.getStatus()).getCodeLabel());
            csvBeans.add(row);
        }

        model.addAttribute("exportCsvData", csvBeans);
        model.addAttribute("class", SimpleEntityCsvBean.class);
    }


    private SimpleEntityBean getBean(SimpleEntity entity) {

        SimpleEntityBean bean = beanMapper.map(entity, SimpleEntityBean.class);

        // テキストフィールド(複数の値)
        if (entity.getText05() != null) {
            bean.setText05Label(String.join(",", entity.getText05()));
        }

        // ラジオボタン(真偽値)ラベル
        if (entity.getRadio01() != null) {
            bean.setRadio01Label(entity.getRadio01() ? "はい" : "いいえ");
        }

        // チェックボックス(文字列)ラベル
        if (entity.getCheckbox01() != null) {
            bean.setCheckbox01Label("はい".equals(entity.getCheckbox01()) ? "☑" : "□" + "利用規約に合意する");
        }

        // チェックボックス(複数の値)ラベル
        if (entity.getCheckbox02() != null) {
            String s = entity.getCheckbox02().stream()
                    .map(str -> statusCodeList.asMap().get(str))
                    .collect(Collectors.joining(", "));
            bean.setCheckbox02Label(s);
        }

        // セレクト(単一の値)ラベル
        if (entity.getSelect01() != null) {
            bean.setSelect01Label(statusCodeList.asMap().get(entity.getSelect01()));
        }

        // セレクト(複数の値)
        if (entity.getSelect02() != null) {
            String t = entity.getSelect02().stream()
                    .map(str -> statusCodeList.asMap().get(str))
                    .collect(Collectors.joining(", "));
            bean.setSelect02Label(t);
        }

        // セレクト(単一の値, select2)
        if (entity.getSelect03() != null) {
            bean.setSelect03Label(statusCodeList.asMap().get(entity.getSelect03()));
        }

        // セレクト(複数の値, select2)
        String u = entity.getSelect04().stream()
                .map(str -> statusCodeList.asMap().get(str))
                .collect(Collectors.joining(", "));
        bean.setSelect04Label(u);

        // コンボボックス(単一の値, Select2)
        if (entity.getCombobox02() != null) {
            bean.setCombobox02Label(statusCodeList.asMap().get(entity.getCombobox02()));
        }

        // コンボボックス(複数の値, Select2)
        if (entity.getCombobox03() != null) {
            String v = entity.getCombobox03().stream()
                    .map(str -> statusCodeList.asMap().get(str))
                    .collect(Collectors.joining(", "));
            bean.setCombobox03Label(v);
        }

        // 添付ファイル名
//            if (entity.getAttachedFile01Uuid() != null) {
//                bean.setAttachedFile01Managed(fileManagedSharedService.findByUuid(entity.getAttachedFile01Uuid()));
//                if (bean.getAttachedFile01Managed() != null) {
//                    bean.setAttachedFile01FileName(bean.getAttachedFile01Managed().getOriginalFilename());
//                }
//            }

        if (entity.getAttachedFile01Managed() != null) {
            bean.setAttachedFile01FileName(bean.getAttachedFile01Managed().getOriginalFilename());
        }

        return bean;
    }

    /**
     * 一覧画面、CSVファイルのためのデータ変換
     *
     * @param entities
     * @return
     */
    private List<SimpleEntityBean> getBeanList(List<SimpleEntity> entities) {
        List<SimpleEntityBean> beans = new ArrayList<>();
        for (SimpleEntity entity : entities) {
            beans.add(getBean(entity));
        }
        return beans;
    }

    /**
     * CSVファイル固有要件のデータ変換
     *
     * @param row          変換後のCSVビーン
     * @param simpleEntity エンティティ
     */
    private void customMap(SimpleEntityCsvBean row, SimpleEntity simpleEntity) {

    }

    /**
     * リビジョンテーブルのデータを通常のエンティティに変換
     *
     * @param entities リビジョンエンティティのリスト
     * @return エンティティのリスト
     */
    private List<SimpleEntityBean> getBeanListByRev(List<SimpleEntityRevision> entities) {
        List<SimpleEntity> beans = new ArrayList<>();
        for (SimpleEntityRevision entity : entities) {
            SimpleEntity bean = beanMapper.map(entities, SimpleEntity.class);
            beans.add(bean);
        }
        return getBeanList(beans);
    }

    /**
     * 一覧画面のトグルボタンHTMLの生成
     *
     * @param id エンティティの内部ID
     * @param op OperationsUtil リンクURLを生成するクラス
     * @return HTML
     */
    private String getToggleButton(String id, OperationsUtil op) {

        // fixedColumnを使うとトグルボタンは使えない。
//        StringBuffer link = new StringBuffer();
//        link.append("<div class=\"whitespace-nowrap\">");
//        link.append("<a class=\"whitespace-nowrap\" href=\"" + op.getEditUrl(id) + "\">" + op.getLABEL_EDIT() + "</a>");
//        link.append(" | ");
//        link.append("<a class=\"whitespace-nowrap\" href=\"" + op.getViewUrl(id) + "\">" + op.getLABEL_VIEW() + "</a></li>");
//        link.append("</div>");

        StringBuffer link = new StringBuffer();
//        link.append("<div class=\"btn-group\">");
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
//        link.append("<button type=\"button\" class=\"btn btn-button btn-sm dropdown-toggle dropdown-toggle-split\"data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">");
//        link.append("</button>");
//        link.append("<div class=\"dropdown-menu\">");
//        link.append("<a class=\"dropdown-item\" href=\"" + op.getViewUrl(id) + "\">" + op.getLABEL_VIEW() + "</a>");
//        link.append("<a class=\"dropdown-item\" href=\"" + op.getCopyUrl(id) + "\">" + op.getLABEL_COPY() + "</a>");
//        link.append("<a class=\"dropdown-item\" href=\"" + op.getInvalidUrl(id) + "\">" + op.getLABEL_INVALID() + "</a>");
//        link.append("</div>");
//        link.append("</div>");

        return link.toString();
    }

    private OperationsUtil op() {
        return new OperationsUtil(BASE_PATH);
    }

//    private OperationsUtil op(String param) {
//        return new OperationsUtil(param);
//    }

    /**
     * ダウンロード
     */
    @GetMapping("{uuid}/download")
    public String download(
            Model model,
            @PathVariable("uuid") String uuid,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.DOWNLOAD, loggedInUser);

        // TODO 厳密な権限チェック

        model.addAttribute(fileManagedService.findByUuid(uuid));
        return "fileManagedDownloadView";
    }

    /**
     * 一括削除
     */
    @PostMapping("bulk_delete")
    public String bulkDelete(Model model, String selectedKey, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.BULK_DELETE, loggedInUser);

        String[] strKeys = selectedKey.split(",");
        List<SimpleEntity> deleteEntities = new ArrayList<>();
        for (String key : strKeys) {
            SimpleEntity entity = simpleEntityService.findById(Long.valueOf(key));
            if (entity.getStatus().equals(Status.INVALID.getCodeValue())) {
                deleteEntities.add(entity);
            }
        }

        simpleEntityService.delete(deleteEntities);

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0003));
        return "redirect:" + op().getListUrl();
    }

    /**
     * 一括無効化(有効データのみ対象)
     */
    @PostMapping("bulk_invalid")
    public String bulkInvalid(Model model, String selectedKey, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.BULK_INVALID, loggedInUser);

        String[] strKeys = selectedKey.split(",");
        List<Long> ids = new ArrayList<>();
        for (String key : strKeys) {
            Long id = Long.valueOf(key);
            SimpleEntity entity = simpleEntityService.findById(id);
            if (entity.getStatus().equals(Status.VALID.getCodeValue())) {
                ids.add(id);
            }
        }

        simpleEntityService.invalid(ids);

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));
        return "redirect:" + op().getListUrl();
    }

    /**
     * 一括有効化(無効データのみ対象)
     */
    @PostMapping("bulk_valid")
    public String bulkValid(Model model, String selectedKey, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.BULK_VALID, loggedInUser);

        String[] strKeys = selectedKey.split(",");
        List<Long> ids = new ArrayList<>();
        for (String key : strKeys) {
            Long id = Long.valueOf(key);
            SimpleEntity entity = simpleEntityService.findById(id);
            if (entity.getStatus().equals(Status.INVALID.getCodeValue())) {
                ids.add(id);
            }
        }

        simpleEntityService.valid(ids);

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));
        return "redirect:" + op().getListUrl();
    }

    /**
     * 新規作成画面を開く
     */
    @GetMapping(value = "create", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String createForm(SimpleEntityForm form,
                             Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @RequestParam(value = "copy", required = false) Long copy) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (copy != null) {
            SimpleEntity source = simpleEntityService.findById(copy);
            beanMapper.map(source, form);
            form.setId(null);
            form.setVersion(null);

            if (source.getAttachedFile01Uuid() != null) {
                try {
                    FileManaged file = fileManagedService.copyFile(source.getAttachedFile01Uuid());
                    form.setAttachedFile01Uuid(file.getUuid());
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute(ResultMessages.error().add(MessageKeys.E_SL_FW_6001));
                    return JSP_FORM;
                }
            }
        }

        setFileManagedToForm(form);

        // 明細業を１行準備
        if (form.getLineItems() == null) {
            form.setLineItems(new ArrayList<LineItem>());
        }
        if (form.getLineItems().size() == 0) {
            form.getLineItems().add(new LineItem());
        }

        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.CREATE, null).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.CREATE, null).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }

    /**
     * UUIDからFileManagedオブジェクトを取得し、formにセットする。
     *
     * @param form フォーム
     */
    private void setFileManagedToForm(SimpleEntityForm form) {
        // TODO ファイルフィールドごとに調整
        if (form.getAttachedFile01Uuid() != null) {
            form.setAttachedFile01Managed(fileManagedService.findByUuid(form.getAttachedFile01Uuid()));
        }
    }

//    /**
//     * UUIDからFileManagedオブジェクトを取得し、Entityにセットする。
//     * @param entity エンティティ
//     */
//    private void setFileManagedToEntity(SimpleEntity entity) {
//        // TODO ファイルフィールドごとに調整
//        if (entity.getAttachedFile01Uuid() != null) {
//            entity.setAttachedFile01Managed(fileManagedSharedService.findByUuid(entity.getAttachedFile01Uuid()));
//        }
//    }

    @PostMapping(value = "create", params = "addlineitem")
    @TransactionTokenCheck
    public String createAddLineItem(SimpleEntityForm form,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirect,
                                    @AuthenticationPrincipal LoggedInUser loggedInUser,
                                    @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        if (form.getLineItems() == null) {
            form.setLineItems(new ArrayList<LineItem>());
        }
        form.getLineItems().add(new LineItem());

        return createForm(form, model, loggedInUser, null);

    }

    /**
     * 新規登録
     */
    @PostMapping(value = "create")
    @TransactionTokenCheck
    public String create(@Validated({SimpleEntityForm.Create.class, Default.class}) SimpleEntityForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return createForm(form, model, loggedInUser, null);
        }

        SimpleEntity simpleEntity = beanMapper.map(form, SimpleEntity.class);

        try {
            if ("true".equals(saveDraft)) {
                simpleEntity.setStatus(Status.VALID.getCodeValue());
                simpleEntityService.saveDraft(simpleEntity);
            } else {
                simpleEntity.setStatus(Status.VALID.getCodeValue());
                simpleEntityService.save(simpleEntity);
            }
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return createForm(form, model, loggedInUser, null);
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0001));

        return "redirect:" + op().getEditUrl(simpleEntity.getId().toString());
    }

    /**
     * 編集画面を開く
     */
    @GetMapping(value = "{id}/update", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String updateForm(SimpleEntityForm form, Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        SimpleEntity simpleEntity = simpleEntityService.findById(id);
        model.addAttribute("simpleEntity", simpleEntity);

        // 状態=無効の場合、参照画面に強制遷移
        if (simpleEntity.getStatus().equals(Status.INVALID.getCodeValue())) {
            model.addAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0008));
            return view(model, loggedInUser, id, null);
        }

        // 入力チェック再表示の場合、formの情報をDBの値で上書きしない
        if (form.getVersion() == null) {
            beanMapper.map(simpleEntity, form);
        }

        setFileManagedToForm(form);

        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.UPDATE, simpleEntity).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.UPDATE, simpleEntity).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }

    /**
     * 行追加ボタン押下時
     */
    @PostMapping(value = "{id}/update", params = "addlineitem")
    @TransactionTokenCheck
    public String updateAddLineItem(SimpleEntityForm form,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirect,
                                    @AuthenticationPrincipal LoggedInUser loggedInUser,
                                    @PathVariable("id") Long id,
                                    @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        if (form.getLineItems() == null) {
            form.setLineItems(new ArrayList<LineItem>());
        }
        form.getLineItems().add(new LineItem());

        return updateForm(form, model, loggedInUser, id);
    }


    /**
     * 更新
     */
    @PostMapping(value = "{id}/update")
    @TransactionTokenCheck
    public String update(@Validated({SimpleEntityForm.Update.class, Default.class}) SimpleEntityForm form,
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

        SimpleEntity simpleEntity = beanMapper.map(form, SimpleEntity.class);

        try {
            if ("true".equals(saveDraft)) {
                simpleEntityService.saveDraft(simpleEntity);
            } else {
                simpleEntity.setStatus(Status.VALID.getCodeValue());
                simpleEntityService.save(simpleEntity);
            }
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return updateForm(form, model, loggedInUser, id);
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0004));

        return "redirect:" + op().getEditUrl(simpleEntity.getId().toString());
    }

    /**
     * 削除
     */
    @GetMapping(value = "{id}/delete")
    public String delete(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.DELETE, loggedInUser);

        try {
            simpleEntityService.delete(id);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0003));

        return "redirect:" + op().getListUrl();
    }

    /**
     * 無効化
     */
    @GetMapping(value = "{id}/invalid")
    public String invalid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                          @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.INVALID, loggedInUser);

        SimpleEntity entity = simpleEntityService.findById(id);

        try {
            entity = simpleEntityService.invalid(id);
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

        // 存在チェックを兼ねる
        SimpleEntity entity = simpleEntityService.findById(id);

        try {
            entity = simpleEntityService.valid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op().getViewUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return "redirect:" + op().getEditUrl(id.toString());
    }

    /**
     * 下書き取消
     */
    @GetMapping(value = "{id}/cancel_draft")
    public String cancelDraft(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                              @PathVariable("id") Long id) {

        authority.hasAuthority(Constants.OPERATION.CANCEL_DRAFT, loggedInUser);

        // 存在チェックを兼ねる
        SimpleEntity entity = simpleEntityService.findById(id);

        try {
            entity = simpleEntityService.cancelDraft(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op().getEditUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        if (entity != null) {
            return "redirect:" + op().getEditUrl(id.toString());
        } else {
            return "redirect:" + op().getListUrl();
        }
    }

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{id}")
    public String view(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                       @PathVariable("id") Long id,
                       @RequestParam(value = "rev", required = false) Long rev) {

        authority.hasAuthority(Constants.OPERATION.VIEW, loggedInUser);

        // SimpleEntity simpleEntity = simpleEntityService.findById(id);　// TODO: リビジョン管理がない場合はシンプルにできる

        SimpleEntity simpleEntity;

        if (rev == null) {
            // 下書きを含む最新
            simpleEntity = simpleEntityService.findById(id);

        } else if (rev == 0) {
            // 有効な最新リビジョン
            simpleEntity = beanMapper.map(simpleEntityService.findByIdLatestRev(id), SimpleEntity.class);

        } else {
            // リビジョン番号指定
            simpleEntity = beanMapper.map(simpleEntityService.findByRid(rev), SimpleEntity.class);
        }

//        setFileManagedToEntity(simpleEntity);

        model.addAttribute("simpleEntity", simpleEntity);
        model.addAttribute("simpleEntityBean", getBean(simpleEntity));
        model.addAttribute("buttonState", getButtonStateMap(Constants.OPERATION.VIEW, simpleEntity).asMap());
        model.addAttribute("fieldState", getFiledStateMap(Constants.OPERATION.VIEW, simpleEntity).asMap());
        model.addAttribute("op", op());

        return JSP_FORM;
    }

    /**
     * ボタンの状態設定
     */
    private StateMap getButtonStateMap(String operation, SimpleEntity record) {

        if (record == null) {
            record = new SimpleEntity();
        }

        List<String> includeKeys = new ArrayList<>();
        includeKeys.add(Constants.BUTTON.GOTOLIST);
        includeKeys.add(Constants.BUTTON.GOTOUPDATE);
        includeKeys.add(Constants.BUTTON.VIEW);
        includeKeys.add(Constants.BUTTON.SAVE);
        includeKeys.add(Constants.BUTTON.INVALID);
        includeKeys.add(Constants.BUTTON.VALID);
        includeKeys.add(Constants.BUTTON.DELETE);
        includeKeys.add(Constants.BUTTON.SAVE_DRAFT);
        includeKeys.add(Constants.BUTTON.CANCEL_DRAFT);
        includeKeys.add(Constants.BUTTON.COPY);

        StateMap buttonState = new StateMap(Default.class, includeKeys, new ArrayList<>());

        // 常に表示
        buttonState.setViewTrue(Constants.BUTTON.GOTOLIST);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            buttonState.setViewTrue(Constants.BUTTON.SAVE);
            buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
        }

        // 編集
        if (Constants.OPERATION.UPDATE.equals(operation)) {

            if (Status.DRAFT.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.CANCEL_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.COPY);
            }

            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.SAVE_DRAFT);
                buttonState.setViewTrue(Constants.BUTTON.SAVE);
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.INVALID);
                buttonState.setViewTrue(Constants.BUTTON.COPY);
            }

            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
                buttonState.setViewTrue(Constants.BUTTON.VIEW);
                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
                buttonState.setViewTrue(Constants.BUTTON.COPY);
            }

        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {

            buttonState.setViewTrue(Constants.BUTTON.GOTOUPDATE);
            buttonState.setViewTrue(Constants.BUTTON.COPY);

            // スタータスが公開時
//            if (Status.VALID.getCodeValue().equals(record.getStatus())) {
//                buttonState.setViewTrue(Constants.BUTTON.INVALID);
//            }

            // スタータスが無効
            if (Status.INVALID.getCodeValue().equals(record.getStatus())) {
//                buttonState.setViewTrue(Constants.BUTTON.VALID);
                buttonState.setViewTrue(Constants.BUTTON.DELETE);
//                buttonState.setViewTrue(Constants.BUTTON.COPY);
            }
        }

        return buttonState;
    }

    /**
     * フィールドの状態設定
     */
    private StateMap getFiledStateMap(String operation, SimpleEntity record) {

        // 常設の隠しフィールドは状態管理しない
        List<String> excludeKeys = new ArrayList<>();
        excludeKeys.add("id");
        excludeKeys.add("version");

        StateMap fieldState = new StateMap(SimpleEntityForm.class, new ArrayList<>(), excludeKeys);

        // 新規作成
        if (Constants.OPERATION.CREATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setInputFalse("status");
        }

        // 編集
        if (Constants.OPERATION.UPDATE.equals(operation)) {
            fieldState.setInputTrueAll();
            fieldState.setViewTrue("status");

            // スタータスが無効
            if (Status.INVALID.toString().equals(record.getStatus())) {
                fieldState.setReadOnlyTrueAll();
            }
        }

        // 参照
        if (Constants.OPERATION.VIEW.equals(operation)) {
            fieldState.setViewTrueAll();
        }

        return fieldState;
    }

    /**
     * アップロードファイル指定画面の表示
     */
    @GetMapping(value = "upload", params = "form")
    public String uploadForm(@ModelAttribute UploadForm form, Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser) {

        form.setJobName("job03");

        if (form.getUploadFileUuid() != null) {
            form.setUploadFileManaged(fileManagedService.findByUuid(form.getUploadFileUuid()));
        }

        model.addAttribute("pageTitle", "Import SimpleEntity");
        model.addAttribute("referer", "list");
//        model.addAttribute("inputFileColumns", "ファイルパスを指定する予定");
        model.addAttribute("fieldState", new StateMap(UploadForm.class, new ArrayList<>(), new ArrayList<>()).setInputTrueAll().asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return JSP_UPLOAD_FORM;
    }

    /**
     * アップロード処理(バッチ実行)
     */
    @PostMapping(value = "upload")
    public String upload(@Validated UploadForm form, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        final String jobName = UPLOAD_JOB_ID;

        Long jobExecutionId = null;

        if (!jobName.equals(form.getJobName()) || result.hasErrors()) {
            return uploadForm(form, model, loggedInUser);
        }

        FileManaged uploadFile = fileManagedService.findByUuid(form.getUploadFileUuid());
        String uploadFileAbsolutePath = fileManagedService.getFileStoreBaseDir() + uploadFile.getUri();
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

}
