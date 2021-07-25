package jp.co.stnet.cms.sales.application.repository.document;

import jp.co.stnet.cms.sales.domain.model.document.DocumentMaxRev;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentMaxRevRepository extends JpaRepository<DocumentMaxRev, Long> {
}
