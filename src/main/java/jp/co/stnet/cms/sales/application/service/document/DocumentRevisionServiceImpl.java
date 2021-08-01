package jp.co.stnet.cms.sales.application.service.document;


import jp.co.stnet.cms.sales.application.repository.document.DocumentRevisionRepository;
import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DocumentRevisionServiceImpl implements DocumentRevisionService {

    @Autowired
    DocumentRevisionRepository documentRevisionRepository;

    /**
     * 最新のドキュメント情報を取得する
     *
     * @param id          ドキュメントID
     * @param publicScope 公開区分
     * @return DocumentRevision型の検索結果
     */
    @Override
    public DocumentRevision findLatest(Long id, Set<String> publicScope) {
        return documentRevisionRepository.findTopByIdAndPublicScopeInOrderByVersionDesc(id, publicScope);
    }

    @Override
    public  DocumentRevision findById(Long id) {
        return documentRevisionRepository.findByIdLatestRev(id);
    }

    /**
     * @param id          ドキュメントID
     * @param version     ドキュメントVer
     * @param publicScope 公開区分
     * @return DocumentRevision型の検索結果
     */
    @Override
    public DocumentRevision versionSpecification(Long id, Long version, Set<String> publicScope) {
        return documentRevisionRepository.findByIdAndVersionAndPublicScopeIn(id, version, publicScope);
    }

    @Override
    public DocumentRevision findByIdAndVersion(Long id, Long version) {
        return documentRevisionRepository.findByIdAndVersion(id, version);
    }

}
