package jp.co.stnet.cms.example.application.repository.select;


import jp.co.stnet.cms.example.domain.model.select.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectRepository extends JpaRepository<Select, Long> {
}
