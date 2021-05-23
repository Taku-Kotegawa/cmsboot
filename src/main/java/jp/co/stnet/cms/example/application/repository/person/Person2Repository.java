package jp.co.stnet.cms.example.application.repository.person;



import jp.co.stnet.cms.example.domain.model.person.Person2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Person2Repository extends JpaRepository<Person2, Long> {
}
