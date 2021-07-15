package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.base.presentation.dto.SelectItem;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.sales.application.service.document.DocumentService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
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
import java.util.List;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.TEMPLATE_FORM;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentUpdateController.addFilesItem;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentUpdateController.setFileManaged;

@Controller
@RequestMapping(BASE_PATH)
@TransactionTokenCheck(BASE_PATH)
public class DocumentCreateController {

    @Autowired
    DocumentHelper helper;

    @Autowired
    DocumentAuthority authority;

    @Autowired
    DocumentService documentService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    VariableService variableService;

    @Autowired
    Mapper beanMapper;

    @ModelAttribute
    DocumentForm setUp() {
        DocumentForm form = new DocumentForm();
        form.getFiles().add(new FileForm());
        form.setSaveRevision(true);
        form.setReasonForChange("新規作成");
        return form;
    }

    /**
     * 新規作成画面を開く
     */
    @GetMapping(value = "create", params = "form")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String createForm(DocumentForm form,
                             Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @RequestParam(value = "copy", required = false) Long copy) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (copy != null) {
            Document source = documentService.findById(copy);
            beanMapper.map(source, form);
            form.setId(null);
            form.setVersion(null);
        }

        setFileManaged(form.getFiles(), fileManagedSharedService);
        addFilesItem(form);
        form.getFiles().add(new FileForm());

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
    public String create(@Validated({DocumentForm.Create.class, Default.class}) DocumentForm form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirect,
                         @RequestParam(value = "saveDraft", required = false) boolean saveDraft,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.CREATE, loggedInUser);

        if (bindingResult.hasErrors()) {
            return createForm(form, model, loggedInUser, null);
        }

        Document document = beanMapper.map(form, Document.class);
        document.setStatus(Status.VALID.getCodeValue());

        try {
            if (saveDraft) {
                documentService.saveDraft(document);
            } else {
                documentService.save(document);
            }
        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return createForm(form, model, loggedInUser, null);
        }

        ResultMessages messages = ResultMessages.info().add(MessageKeys.I_CM_FW_0001);
        redirect.addFlashAttribute(messages);

        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "redirect:" + op.getEditUrl(document.getId().toString());
    }

    @PostMapping(value = "create", params = "addlineitem")
    @TransactionTokenCheck
    public String createAddLineItem(DocumentForm form,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirect,
                                    @AuthenticationPrincipal LoggedInUser loggedInUser,
                                    @RequestParam(value = "saveDraft", required = false) String saveDraft) {

        addFilesItem(form);
        return createForm(form, model, loggedInUser, null);

    }


    @ResponseBody
    @GetMapping(value = "doc_category2/json", params = "selected_value")
    public List<SelectItem> docCategory2Json(@RequestParam(value = "selected_value", required = false) String selected_value) {
        List<SelectItem> list = new ArrayList<>();
        for (Variable variable : variableService.findAllByTypeAndValueX(VariableType.DOC_CATEGORY2.name(), 2, selected_value)) {
            list.add(new SelectItem(variable.getCode(), variable.getValue1()));
        }
        return list;
    }

    @ResponseBody
    @GetMapping(value = "doc_service2/json", params = "selected_value")
    public List<SelectItem> docService2Json(@RequestParam(value = "selected_value", required = false) String selected_value) {
        List<SelectItem> list = new ArrayList<>();
        for (Variable variable : variableService.findAllByTypeAndValueX(VariableType.DOC_SERVICE2.name(), 2, selected_value)) {
            list.add(new SelectItem(variable.getCode(), variable.getValue1()));
        }
        return list;
    }

    @ResponseBody
    @GetMapping(value = "doc_service3/json", params = "selected_value")
    public List<SelectItem> docService3Json(@RequestParam(value = "selected_value", required = false) String selected_value) {
        List<SelectItem> list = new ArrayList<>();
        for (Variable variable : variableService.findAllByTypeAndValueX(VariableType.DOC_SERVICE3.name(), 2, selected_value)) {
            list.add(new SelectItem(variable.getCode(), variable.getValue1()));
        }
        return list;
    }
}
