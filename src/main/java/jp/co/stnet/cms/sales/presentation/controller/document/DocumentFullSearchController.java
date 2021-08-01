package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.sales.application.service.document.DocumentFullSearchService;
import jp.co.stnet.cms.sales.domain.model.document.DocumentFullSearchForm;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndex;
import jp.co.stnet.cms.sales.domain.model.document.KeywordSearchTarget;
import jp.co.stnet.cms.sales.domain.model.variable.VariableBean;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;

@Controller
@RequestMapping(BASE_PATH)
public class DocumentFullSearchController {

    private static final String TYPE_CATEGORY = "Category";
    private static final String TYPE_SERVICE = "Service";

    private static final String docCategory1 = "DOC_CATEGORY1";
    private static final String docCategory2 = "DOC_CATEGORY2";
    private static final String docService1 = "DOC_SERVICE1";
    private static final String docService2 = "DOC_SERVICE2";
    private static final String docService3 = "DOC_SERVICE3";

    @Autowired
    DocumentFullSearchService documentFullSearchService;

    @Autowired
    Mapper beanMapper;

    @Autowired
    DocumentHelper helper;


    @Autowired
    VariableService variableService;

    @ModelAttribute
    private DocumentFullSearchForm setUpFullSearchForm() {
        DocumentFullSearchForm form = new DocumentFullSearchForm();
        form.setKeywordSearchTarget(Set.of(
                KeywordSearchTarget.CONTENT,
                KeywordSearchTarget.BODY,
                KeywordSearchTarget.TITLE,
                KeywordSearchTarget.FILENAME));

        return form;
    }

    @GetMapping("search")
    public String search(Model model, @Validated DocumentFullSearchForm form, BindingResult bindingResult,
                         @PageableDefault(size = 5) Pageable pageable, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        return searchFacets(model, form, bindingResult, pageable, loggedInUser);
    }

    /**
     * 検索入力フォームに入力した内容をキーワードに検索する
     * 値が入力されている場合: 入力された内容でルシーンから検索
     * 値が入っていない場合: 検索ボタンを選択できない or 全検索
     *
     * @param model
     * @param form
     * @param bindingResult
     * @param pageable
     * @param loggedInUser
     * @return
     */
    @GetMapping(value = "search", params = {"q", "period", "sort", "facets"})
    public String searchFacets(Model model, @Validated DocumentFullSearchForm form, BindingResult bindingResult,
                               @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        // 権限チェック？

        // 公開区分を格納
        Set<String> publicScope = helper.getPublicScopeSet(loggedInUser);

        // データ取得
        SearchResult<DocumentIndex> result = documentFullSearchService.search(form, publicScope, pageable);

        List<DocumentIndexSearchRow> rows = new ArrayList<>();
        for (DocumentIndex d : result.hits()) {
            DocumentIndexSearchRow row = beanMapper.map(d, DocumentIndexSearchRow.class);

            String q = form.getQ();
            if (q != null) {
                if (form.getKeywordSearchTarget().contains(KeywordSearchTarget.CONTENT)) {
                    row.setContentHighlight(documentFullSearchService.highlight(d.getContent(), q, "content"));
                }
                if (form.getKeywordSearchTarget().contains(KeywordSearchTarget.BODY)) {
                    row.setBodyHighlight(documentFullSearchService.highlight(d.getBodyPlain(), q, "bodyPlain"));
                }

                row.setTitleHighlight(documentFullSearchService.highlightKeywordField(d.getTitle(), Arrays.asList(form.getQ().split(" "))));

                if (d.getFileManaged() != null) {
                    row.setFileNameHighlight(documentFullSearchService.highlightKeywordField(d.getFileManaged().getOriginalFilename(), Arrays.asList(form.getQ().split(" "))));
                }
            }

            // ハイライトテキストが取得できなかった場合、代替文字列を設定する。
            if (StringUtils.isEmpty(row.getContentHighlight())) {
                row.setContentHighlight(StringUtils.substring(d.getContent(), 0, 100));
            }

            if (StringUtils.isEmpty(row.getBodyHighlight())) {
                row.setBodyHighlight(StringUtils.substring(d.getBodyPlain(), 0, 100));
            }

            if (StringUtils.isEmpty(row.getTitleHighlight())) {
                row.setTitleHighlight(d.getTitle());
            }

            if (d.getFileManaged() != null && StringUtils.isEmpty(row.getFileNameHighlight())) {
                row.setFileNameHighlight(d.getFileManaged().getOriginalFilename());
            }

            row.setDocCategory1Name(getCodeName(docCategory1, row.getDocCategory1()));
            row.setDocCategory2Name(getCodeName(docCategory2, row.getDocCategory2()));
            row.setDocService1Name(getCodeName(docService1, row.getDocService1()));
            row.setDocService2Name(getCodeName(docService2, row.getDocService2()));
            row.setDocService3Name(getCodeName(docService3, row.getDocService3()));
            rows.add(row);
        }

        // ResultSet -> Page 変換
        Page<DocumentIndexSearchRow> page = new PageImpl<>(rows, pageable, result.total().hitCount());

        // Modelに格納
        model.addAttribute("op", new DocumentOperationUtil(BASE_PATH));
        model.addAttribute("page", page);
        model.addAttribute("form", form);
        model.addAttribute("result", result);
        model.addAttribute("hits", rows);
        model.addAttribute("totalHitCount", result.total().hitCount());
        model.addAttribute("variableCategoryBeanList", getVariableBeanList(TYPE_CATEGORY, result));
        model.addAttribute("variableServiceBeanList", getVariableBeanList(TYPE_SERVICE, result));

        return BASE_PATH + "/search";
    }

    /**
     * @param model
     * @param form
     * @param bindingResult
     * @param pageable
     * @param loggedInUser
     * @return
     */
    @GetMapping(value = "search", params = {"q", "period", "sort"})
    public String searchForm(Model model, @Validated DocumentFullSearchForm form, BindingResult bindingResult,
                             @PageableDefault(size = 5) Pageable pageable, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        return searchFacets(model, form, bindingResult, pageable, loggedInUser);
    }

    /**
     * 文書区分、サービスのファセットを準備
     * @param type Category か Service
     * @param result 検索結果
     * @return ファセット検索用の区分、サービスのリスト
     */
    private List<VariableBean> getVariableBeanList(String type, SearchResult<DocumentIndex> result) {

        if (!(TYPE_CATEGORY.equals(type) || TYPE_SERVICE.equals(type))) {
            throw new IllegalArgumentException("type is not allowed.");
        }

        List<Variable> lv1s = variableService.findAllByType("DOC_" + type.toUpperCase() + "1");
        List<Variable> lv2s = variableService.findAllByType("DOC_" + type.toUpperCase() + "2");

        Map<String, Long> countsByLv2 = result.aggregation(AggregationKey.of("countsByDoc" + type + "2"));

        List<VariableBean> variableBeans = new ArrayList<>();
        for (Variable lv1 : lv1s) {
            List<VariableBean> child = new ArrayList<>();
            long countLv1 = 0;
            for (Variable lv2 : lv2s) {
                // 親子が一致、件数が取得できる
                if (lv1.getCode().equals(lv2.getValue2()) && countsByLv2.get(lv2.getCode()) != null) {
                    child.add(new VariableBean(lv2.getCode(), lv2.getValue1(), "2", countsByLv2.get(lv2.getCode()), null));
                    countLv1 = countLv1 + countsByLv2.get(lv2.getCode());
                }
            }
            if (!child.isEmpty()) {
                variableBeans.add(new VariableBean(lv1.getCode(), lv1.getValue1(), "1", countLv1, child));
            }
        }

        return variableBeans;
    }


    /**
     * @param type
     * @param code
     * @return
     */
    private String getCodeName(String type, String code) {
        List<Variable> variableList = variableService.findAllByTypeAndCode(type, code);
        String codeName = "";
        for (Variable v : variableList) {
            codeName = v.getValue1();
        }
        return codeName;
    }

//    @SafeVarargs
//    private void sortList(List<Variable>... list) {
//        for (List<Variable> l : list) {
//            Comparator<Variable> compare = Comparator.comparing(Variable::getCode);
//            l.sort(compare);
//        }
//    }
}
