package jp.co.stnet.cms.example.application.service;


import jp.co.stnet.cms.base.application.service.NodeIService;
import jp.co.stnet.cms.example.domain.model.person.Person;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.data.domain.Pageable;


public interface PersonService extends NodeIService<Person, Long> {


    void test(String term);

    SearchResult<Person> search(String term, Pageable pageable);

    String highlight(String text, String term);

}
