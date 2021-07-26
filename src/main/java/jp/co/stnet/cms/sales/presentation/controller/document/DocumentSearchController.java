package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.sales.application.service.document.DocumentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;

@Controller
@RequestMapping(BASE_PATH)
public class DocumentSearchController {

    private static final String TEMPLATE_SEARCH_LIST = BASE_PATH + "/searchlist";

    @Autowired
    DocumentAuthority authority;

    @Autowired
    DocumentSearchService documentSearchService;

    @Autowired
    Documents documents;

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "searchlist")
    public String searchList(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        authority.hasAuthority(Constants.OPERATION.LIST, loggedInUser);
        return TEMPLATE_SEARCH_LIST;
    }

    /**
     * DataTables用のJSONの作成
     *
     * @param input DataTablesから要求
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "searchlist/json")
    public DataTablesOutput<DocumentListBean> listJson(@Validated DataTablesInput input,
                                                       @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority("SEARCH_LIST", loggedInUser);

        var documentIndexSearchResult = documentSearchService.searchByInput(input);

        var output = new DataTablesOutput<DocumentListBean>();
        output.setData(documents.getDocumentListBeansFromDocumentIndex(documentIndexSearchResult.hits()));
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(documentIndexSearchResult.total().hitCount());

        return output;
    }


}
