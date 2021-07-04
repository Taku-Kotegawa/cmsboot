package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.sales.application.service.document.DocumentService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static jp.co.stnet.cms.common.constant.Constants.OPERATION;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.TEMPLATE_LIST;


@Controller
@RequestMapping(BASE_PATH)
public class DocumentListController {

    @Autowired
    DocumentAuthority authority;

    @Autowired
    DocumentService documentService;

    @Autowired
    Documents documents;

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list")
    public String list(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        authority.hasAuthority(OPERATION.LIST, loggedInUser);
        return TEMPLATE_LIST;
    }

    /**
     * DataTables用のJSONの作成
     *
     * @param input DataTablesから要求
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "/list/json")
    public DataTablesOutput<DocumentListBean> listJson(@Validated DataTablesInput input,
                                                       @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(OPERATION.LIST, loggedInUser);

        Page<Document> documentPage = documentService.findPageByInput(input);

        DataTablesOutput<DocumentListBean> output = new DataTablesOutput<>();
        output.setData(documents.getDocumentListBeans(documentPage.getContent()));
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(documentPage.getTotalElements());

        return output;
    }

}
