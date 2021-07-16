package jp.co.stnet.cms.sales.application.repository.document;

import jp.co.stnet.cms.sales.domain.model.document.DocumentAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DocumentAccessRepository extends JpaRepository<DocumentAccess, Long> {

    DocumentAccess findByAccessDateAndDocumentIdAndUsername(LocalDate accessDate, Long documentId, String username);

}
