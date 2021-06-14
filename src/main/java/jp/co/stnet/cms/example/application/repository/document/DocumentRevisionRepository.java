package jp.co.stnet.cms.example.application.repository.document;

import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.example.domain.model.document.DocumentRevision;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentRevisionRepository extends NodeRevRepository<DocumentRevision, Long> {

    @Query("SELECT c FROM DocumentRevision c INNER JOIN DocumentMaxRev m ON m.rid = c.rid AND c.revType < 2 WHERE m.id = :id")
    DocumentRevision findByIdLatestRev(@Param("id") Long id);

}
