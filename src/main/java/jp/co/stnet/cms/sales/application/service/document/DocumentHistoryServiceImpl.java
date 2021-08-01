package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRevisionRepository;
import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DocumentHistoryServiceImpl implements DocumentHistoryService {

    @Autowired
    DocumentRevisionRepository documentRevisionRepository;

    /**
     * 全件表示のチェック有無を確認し、対応したRepositoryを呼び出す
     *
     * @param id               ドキュメントID
     * @param saveRevisionOnly 全件表示のチェック有無
     * @param publicScope      公開区分
     * @return DocumentRevisionのリスト
     */
    @Override
    public Page<DocumentRevision> search(Long id, boolean saveRevisionOnly, Set<String> publicScope) {
        if (saveRevisionOnly) {
            return documentRevisionRepository.findByIdAndSaveRevisionAndPublicScopeIn(id, true, publicScope, PageRequest.of(0, 20));
        } else {
            return documentRevisionRepository.findByIdAndPublicScopeIn(id, publicScope, PageRequest.of(0, 20));
        }
    }

    @Override
    public Account nameSearch(String id) {
        return documentRevisionRepository.findPerson(id);
    }
}
