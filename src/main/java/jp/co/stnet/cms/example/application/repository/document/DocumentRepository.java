package jp.co.stnet.cms.example.application.repository.document;

import jp.co.stnet.cms.example.domain.model.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
