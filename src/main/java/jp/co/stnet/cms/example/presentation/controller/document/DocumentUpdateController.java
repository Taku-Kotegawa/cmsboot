package jp.co.stnet.cms.example.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.example.application.service.document.DocumentService;
import jp.co.stnet.cms.example.domain.model.document.Document;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static jp.co.stnet.cms.example.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.example.presentation.controller.document.DocumentConstant.TEMPLATE_FORM;


@Controller
@RequestMapping(BASE_PATH)
@TransactionTokenCheck(BASE_PATH)
public class DocumentUpdateController {

    private final OperationsUtil op = new OperationsUtil(BASE_PATH);

    @Autowired
    DocumentHelper helper;

    @Autowired
    DocumentService documentService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    Mapper beanMapper;

    static void addFilesItem(DocumentForm form) {

        if (form.getFiles() == null) {
            form.setFiles(new ArrayList<>());
            return;
        }

        Iterator<FileForm> iterator = form.getFiles().iterator();
        while (iterator.hasNext()) {
            FileForm fileForm = iterator.next();
            if (StringUtils.isAllBlank(fileForm.getType(), fileForm.getFileUuid(), fileForm.getPdfUuid())) {
                iterator.remove();
            }
        }

        for (FileForm fileForm : form.getFiles()) {
            if (fileForm.getFileUuid() == null) {
                return;
            }
        }

//        form.getFiles().add(new FileForm());

    }

    static void setFileManaged(Collection<FileForm> fileForms, FileManagedSharedService service) {
        for (FileForm fileForm : fileForms) {
            if (fileForm.getFileUuid() != null) {
                fileForm.setFileManaged(service.findByUuid(fileForm.getFileUuid()));
            }
            if (fileForm.getPdfUuid() != null) {
                fileForm.setPdfManaged(service.findByUuid(fileForm.getPdfUuid()));
            }
        }
    }

    @ModelAttribute
    DocumentForm setUp() {
        return new DocumentForm();
    }

    /**
     * 編集画面を開く
     */
    @GetMapping(value = "{id}/update", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String updateForm(DocumentForm form, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("id") Long id) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        documentService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        Document document = documentService.findById(id);

        // 初回表示(入力チェックエラー時の再表示でない場合)
        if (form.getVersion() == null) {
            beanMapper.map(document, form);
        }

        setFileManaged(form.getFiles(), fileManagedSharedService);
//        addFilesItem(form);
        form.setSaveRevision(false);

        model.addAttribute("document", document);
        model.addAttribute("buttonState", helper.getButtonStateMap(Constants.OPERATION.UPDATE, document, form).asMap());
        model.addAttribute("fieldState", helper.getFiledStateMap(Constants.OPERATION.UPDATE, document, form).asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return TEMPLATE_FORM;
    }

    /**
     * 更新
     */
    @PostMapping(value = "{id}/update")
    @TransactionTokenCheck
    public String update(@Validated({DocumentForm.Update.class, Default.class}) DocumentForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @AuthenticationPrincipal LoggedInUser loggedInUser,
                         @PathVariable("id") Long id) {

        // 実行権限が無い場合、AccessDeniedExceptionをスローし、キャッチしないと権限エラー画面に遷移
        documentService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return updateForm(form, model, loggedInUser, id);
        }

        Document document = documentService.findById(id);
        document.setFiles(new ArrayList<>());
        document.setUseStage(new HashSet<>());
        beanMapper.map(form, document);

        try {
            documentService.save(document);
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return updateForm(form, model, loggedInUser, id);
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0004);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getEditUrl(id.toString());
    }

    /**
     * 無効化
     */
    @GetMapping(value = "{id}/invalid")
    public String invalid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                          @PathVariable("id") Long id) {

        documentService.hasAuthority(Constants.OPERATION.INVALID, loggedInUser);

        Document entity = documentService.findById(id);

        try {
            entity = documentService.invalid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op.getEditUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return "redirect:" + op.getViewUrl(id.toString());
    }

    /**
     * 無効解除
     */
    @GetMapping(value = "{id}/valid")
    public String valid(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                        @PathVariable("id") Long id) {

        documentService.hasAuthority(Constants.OPERATION.VALID, loggedInUser);

        // 存在チェックを兼ねる
        Document entity = documentService.findById(id);

        try {
            entity = documentService.valid(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op.getViewUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        return "redirect:" + op.getEditUrl(id.toString());
    }

    /**
     * 下書き取消
     */
    @GetMapping(value = "{id}/cancel_draft")
    public String cancelDraft(Model model, RedirectAttributes redirect, @AuthenticationPrincipal LoggedInUser loggedInUser,
                              @PathVariable("id") Long id) {

        documentService.hasAuthority(Constants.OPERATION.CANCEL_DRAFT, loggedInUser);

        // 存在チェックを兼ねる
        Document entity = documentService.findById(id);

        try {
            entity = documentService.cancelDraft(id);
        } catch (BusinessException e) {
            redirect.addFlashAttribute(e.getResultMessages());
            return "redirect:" + op.getEditUrl(id.toString());
        }

        redirect.addFlashAttribute(ResultMessages.info().add(MessageKeys.I_CM_FW_0002));

        if (entity != null) {
            return "redirect:" + op.getEditUrl(id.toString());
        } else {
            return "redirect:" + op.getListUrl();
        }
    }

    /**
     * 行追加ボタン押下時
     */
    @PostMapping(value = "{id}/update", params = "addlineitem")
    @TransactionTokenCheck
    public String updateAddLineItem(DocumentForm form,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirect,
                                    @AuthenticationPrincipal LoggedInUser loggedInUser,
                                    @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        setFileManaged(form.getFiles(), fileManagedSharedService);
        addFilesItem(form);
        form.getFiles().add(new FileForm());

        return updateForm(form, model, loggedInUser, form.getId());

    }


}
