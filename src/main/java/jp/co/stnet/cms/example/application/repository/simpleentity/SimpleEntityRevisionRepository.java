package jp.co.stnet.cms.example.application.repository.simpleentity;

import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityRevision;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleEntityRevisionRepository extends NodeRevRepository<SimpleEntityRevision, Long> {

    @Query("SELECT c FROM SimpleEntityRevision c INNER JOIN SimpleEntityMaxRev m ON m.rid = c.rid AND c.revType < 2 WHERE m.id = :id")
    SimpleEntityRevision findByIdLatestRev(@Param("id") Long id);

}
