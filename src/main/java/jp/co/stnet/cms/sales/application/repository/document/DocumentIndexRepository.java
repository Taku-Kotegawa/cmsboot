package jp.co.stnet.cms.sales.application.repository.document;

import jp.co.stnet.cms.sales.domain.model.document.DocumentIndex;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndexPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentIndexRepository extends JpaRepository<DocumentIndex, DocumentIndexPK> {

    void deleteById(Long id);
}
