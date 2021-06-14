package jp.co.stnet.cms.example.application.service;

import jp.co.stnet.cms.base.application.service.AbstractNodeService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.datatables.Column;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.util.StringUtils;
import jp.co.stnet.cms.example.application.repository.person.PersonRepository;
import jp.co.stnet.cms.example.domain.model.person.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.InflectionAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;
import org.apache.tika.exception.TikaException;
import org.hibernate.search.backend.lucene.LuceneBackend;
import org.hibernate.search.engine.backend.Backend;
import org.hibernate.search.engine.backend.index.IndexManager;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

@Slf4j
@Service
@Transactional
public class PersonServiceImpl extends AbstractNodeService<Person, Long> implements PersonService {

//    @Value("${hibernate.search.backend.directory.root}")
//    private String indexDirectoryPath;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @PersistenceContext
    EntityManager entityManager;


    @Override
    protected JpaRepository<Person, Long> getRepository() {
        return this.personRepository;
    }

//    private boolean changeContent(Person entity, Person current) {
//        if (entity == null || entity.getAttachedFile01Uuid() == null) {
//            return false;
//        } else {
//            return current == null || current.getAttachedFile01Uuid() == null
//                    || !Objects.equals(entity.getAttachedFile01Uuid(), current.getAttachedFile01Uuid());
//        }
//    }


    @Override
    protected void beforeSave(Person entity, Person current) {
        // 添付ファイルの本文を差し込み
        try {
            String content = null;
            if (current != null) {
                content = current.getContent();
            }

            if (entity == null || entity.getAttachedFile01Uuid() == null) {
                content = null;
            } else if (current == null || current.getAttachedFile01Uuid() == null
                    || !Objects.equals(entity.getAttachedFile01Uuid(), current.getAttachedFile01Uuid())) {

                content = fileManagedSharedService.getContent(entity.getAttachedFile01Uuid());
                content = content
//                        .replaceAll("[　]+", " ")
//                        .replaceAll("[ ]+", " ")
//                        .replaceAll("[\t]+", " ")
//                        .replaceAll("[ |\t]+", " ")
                        .replaceAll("[ |　|\t|\\n|\\r\\n|\\r]+", " ");
            }

            entity.setContent(escapeHtml4(content));

        } catch (IOException e) {
            e.printStackTrace();
            // TODO ビジネス例外をスロー
        } catch (TikaException e) {
            e.printStackTrace();
            // TODO ビジネス例外をスロー
        }

    }

//    @Override
//    public Person save(Person entity) {
//
//        // 保存前データ取得
//        String currentUuid = null;
//        if (entity.getId() != null) {
//            currentUuid = findById(entity.getId()).getAttachedFile01Uuid();
//        }
//
//        Person person = super.save(entity);
//
//
//
//
//
//        return person;
//    }

    @Override
    protected void afterSave(Person entity, Person current) {
        // UUID変更されていた場合、以前のUUIDを物理削除
        if (current != null && current.getAttachedFile01Uuid() != null &&
                !current.getAttachedFile01Uuid().equals(entity.getAttachedFile01Uuid())) {
            fileManagedSharedService.delete(current.getAttachedFile01Uuid());
        }

        // 添付ファイル確定
        if (entity.getAttachedFile01Uuid() != null) {
            fileManagedSharedService.permanent(entity.getAttachedFile01Uuid());
        }
    }

    @Override
    public void delete(Long id) {

        // 添付ファイルURI取得
        String uri = null;
        if (findById(id).getAttachedFile01Uuid() != null) {
            uri = fileManagedSharedService.findByUuid(findById(id).getAttachedFile01Uuid()).getUri();
        }

        super.delete(id);

        // 添付ファイル削除
        fileManagedSharedService.deleteFile(uri);
    }

    @Override
    @PostAuthorize("returnObject == true")
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return true;
    }

    @Override
    public void test(String term) {

        SearchSession searchSession = Search.session(entityManager);

        SearchResult<Person> result = searchSession.search(Person.class)
                .where(f -> f.match()
//                .where(f -> f.wildcard()
                        .fields("content")
//                        .matching("*" + term + "*"))
                        .matching(term))
                .fetch(20);

        long totalHitCount = result.total().hitCount();
        System.out.println(totalHitCount);
        List<Person> hits = result.hits();
        System.out.println(totalHitCount);

        for (Person person : hits) {
            String text = person.getContent();
            System.out.println(highlight(text, term));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SearchResult<Person> search(String term, Pageable pageable) {
        SearchSession searchSession = Search.session(entityManager);

        List<String> tokens = analyze(term);
        System.out.println(StringUtils.join(tokens, " + "));

        AggregationKey<Map<String, Long>> countsByGenreKey = AggregationKey.of("countsByGenre");

        int pageSize = 5;
        long offset = 0;

        if (pageable != null) {
            pageSize = pageable.getPageSize();
            offset = pageable.getOffset();
        }

        SearchResult<Person> result = searchSession.search(Person.class)
                .where(
//                        f -> f.wildcard()
//                              .fields("content")
//                              .matching("*" + term + "*")

                        f -> f.simpleQueryString()
                                .fields("content")
                                .matching(StringUtils.join(tokens, " + "))
                )
                .aggregation(countsByGenreKey, f -> f.terms()
                        .field("code", String.class))
                .sort(f -> f.score())
                .fetch((int) offset, pageSize);


//        long totalHitCount = result.total().hitCount();
//        List<Person> hits = result.hits();

//        for (Person person : result.hits()) {
//            String text = person.getContent();
//            if (text == null) {
//                text = "";
//            }
//        }

        return result;
    }

    private List<String> analyze(String q) {

        List<String> tokens = new ArrayList<>();

        JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false, JapaneseTokenizer.Mode.NORMAL);

        CharTermAttribute term = tokenizer.addAttribute(CharTermAttribute.class);
        OffsetAttribute offset = tokenizer.addAttribute(OffsetAttribute.class);
        PartOfSpeechAttribute partOfSpeech = tokenizer.addAttribute(PartOfSpeechAttribute.class);
        InflectionAttribute inflection = tokenizer.addAttribute(InflectionAttribute.class);
        BaseFormAttribute baseForm = tokenizer.addAttribute(BaseFormAttribute.class);
        ReadingAttribute reading = tokenizer.addAttribute(ReadingAttribute.class);

        tokenizer.setReader(new StringReader(q));
        try {
            tokenizer.reset();
            while (tokenizer.incrementToken()) {

                tokens.add(term.toString());
                System.out.println(term.toString() + "\t" // 表層形
                        + offset.startOffset() + "-" + offset.endOffset() + "," // 文字列中の位置
                        + partOfSpeech.getPartOfSpeech() + "," // 品詞-品詞細分類1-品詞細分類2
                        + inflection.getInflectionType() + "," // 活用型
                        + inflection.getInflectionForm() + "," // 活用形
                        + baseForm.getBaseForm() + "," // 原形 (活用しない語では null)
                        + reading.getReading() + "," // 読み
                        + reading.getPronunciation()); // 発音
            }
            tokenizer.close();

        } catch (IOException e) {
            e.printStackTrace();
            // TODO throw new Exception
        }

        return tokens;

    }

    @Override
    protected boolean isFilterINClause(String fieldName) {
        return "status".equals(fieldName);
    }

    public String highlight(String text, String term) {

        try {


            SearchMapping mapping = Search.mapping(entityManager.getEntityManagerFactory());
            IndexManager indexManager = mapping.indexManager("Person");
            Backend backend = mapping.backend();
            LuceneBackend luceneBackend = backend.unwrap(LuceneBackend.class);
            Analyzer analyzer = luceneBackend.analyzer("japanese").orElseThrow(() -> new IllegalStateException());

//            Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath + "/" + "Person"));
//
//            // IndexSearcher
//            IndexReader reader = DirectoryReader.open(indexDirectory);
//            IndexSearcher searcher = new IndexSearcher(reader);
//
//            Analyzer analyzer2 = new JapaneseAnalyzer();
            QueryParser queryParser = new QueryParser("content", analyzer);
//            queryParser.setAllowLeadingWildcard(true);
            Query query = queryParser.parse(term);
            QueryScorer scorer = new QueryScorer(query, "content");
            Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
            Highlighter highlighter = new Highlighter(formatter, scorer);

            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            highlighter.setTextFragmenter(fragmenter);

            TokenStream stream = analyzer.tokenStream("content", text);
//
            String[] frags = highlighter.getBestFragments(stream, text, 4);


            String fragsString = "";
            for (String f : frags) {
                fragsString = fragsString + f + " ";
            }

            return fragsString;

            //
//
        } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }

        return "";

    }
}
