package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface DocumentHistoryService {

    Page<DocumentRevision> search(Long id, boolean saveRevisionOnly, Set<String> publicScope);

    Account nameSearch(String id);
}
