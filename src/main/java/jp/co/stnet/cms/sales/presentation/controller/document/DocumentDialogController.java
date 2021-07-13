package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.*;

@Controller
@RequestMapping(BASE_PATH)
public class DocumentDialogController {

    @Autowired
    VariableService variableService;

    /**
     * Variableの一覧をDataTablesOutput(JSON)で返す。
     */
    @ResponseBody
    @GetMapping("variableDialog/{type}/json")
    public DataTablesOutput<Variable> variableDialogJson(@PathVariable("type") String type,  @AuthenticationPrincipal LoggedInUser loggedInUser) {

        // TODO 権限チェック

        DataTablesOutput<Variable> output = new DataTablesOutput<>();
        output.setData(variableService.findAllByType(type));
//        output.setData(variableService.findAllByType(VariableType.DOC_CATEGORY.name()));
        return output;
    }


    @GetMapping("docCategoryDialog")
    public String docCategoryDialog(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser){

        // TODO 権限チェック

        return TEMPLATE_DOC_CATEGORY_DIALOG;
    }

    @GetMapping("docServiceDialog")
    public String docServiceDialog(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser){

        // TODO 権限チェック

        return TEMPLATE_DOC_SERVICE_DIALOG;
    }

}
