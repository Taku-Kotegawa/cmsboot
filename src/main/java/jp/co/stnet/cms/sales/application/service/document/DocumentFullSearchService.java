package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.sales.domain.model.document.DocumentFullSearchForm;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndex;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DocumentFullSearchService {

    SearchResult<DocumentIndex> search(DocumentFullSearchForm form, Set<String> scope, Pageable pageable);

    String highlight(String text, String term, String target);

    String highlightKeywordField(String text, List<String> terms);

    List<Variable> label(Map<String, Long> text);
}
