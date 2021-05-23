package jp.co.stnet.cms.example.application.repository.select;

import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.example.domain.model.select.SelectRevision;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectRevisionRepository extends NodeRevRepository<SelectRevision, Long> {

    @Query("SELECT c FROM SelectRevision c INNER JOIN SelectMaxRev m ON m.rid = c.rid AND c.revType < 2 WHERE m.id = :id")
    SelectRevision findByIdLatestRev(@Param("id") Long id);

}
