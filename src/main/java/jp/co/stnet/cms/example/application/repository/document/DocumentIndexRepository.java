package jp.co.stnet.cms.example.application.repository.document;

import jp.co.stnet.cms.example.domain.model.document.DocumentIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentIndexRepository extends JpaRepository<DocumentIndex, Long> {
}
