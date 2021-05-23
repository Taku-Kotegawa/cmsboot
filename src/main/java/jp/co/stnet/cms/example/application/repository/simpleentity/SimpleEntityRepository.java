package jp.co.stnet.cms.example.application.repository.simpleentity;

import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleEntityRepository extends JpaRepository<SimpleEntity, Long> {
}
