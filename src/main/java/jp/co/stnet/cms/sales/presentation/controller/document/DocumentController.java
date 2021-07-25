package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.sales.application.service.document.DocumentAccessService;
import jp.co.stnet.cms.sales.application.service.document.DocumentService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.TEMPLATE_FORM;

@Controller
@RequestMapping(BASE_PATH)
public class DocumentController {

    @Autowired
    DocumentHelper helper;

    @Autowired
    DocumentAuthority authority;

    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentAccessService documentAccessService;

    @PersistenceContext
    EntityManager entityManager;

    @ModelAttribute
    DocumentForm setUp() {
        return new DocumentForm();
    }

    /**
     * 参照画面の表示
     */
    @GetMapping(value = "{id}")
    public String view(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                       @PathVariable("id") Long id) {

        Document document = documentService.findById(id);
        entityManager.detach(document);

        authority.hasAuthority(Constants.OPERATION.VIEW, loggedInUser, document);

        PolicyFactory sanitizer = new HtmlPolicyBuilder()
                .allowElements("a")
                .allowUrlProtocols("http", "https")
                .allowAttributes("href", "target").onElements("a")
                .requireRelNofollowOnLinks()
                .toFactory()
                .and(Sanitizers.FORMATTING)
                .and(Sanitizers.BLOCKS)
                .and(Sanitizers.TABLES)
                .and(Sanitizers.STYLES);

        document.setBody(sanitizer.sanitize(document.getBody()));

        model.addAttribute("document", document);
        model.addAttribute("buttonState", helper.getButtonStateMap(Constants.OPERATION.VIEW, document, null).asMap());
        model.addAttribute("fieldState", helper.getFiledStateMap(Constants.OPERATION.VIEW, document, null).asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        documentAccessService.save(id, loggedInUser.getUsername());

        return TEMPLATE_FORM;
    }

}
