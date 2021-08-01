package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.sales.domain.model.document.DocumentFullSearchForm;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndex;
import jp.co.stnet.cms.sales.domain.model.document.KeywordSearchTarget;
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
import org.hibernate.search.backend.lucene.LuceneBackend;
import org.hibernate.search.engine.backend.Backend;
import org.hibernate.search.engine.backend.index.IndexManager;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DocumentFullSearchServiceImpl implements DocumentFullSearchService {

    private static final String sort1 = "lastModifiedDate";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CustomDateFactory dateFactory;

    /**
     * 入力フォームに入力した内容で全文検索を行う
     * ① 入力内容を形態素解析する
     * ② ①の値を用いて全文検索
     * where: ①の結果がcontentにマッチしたデータ and 閲覧権限がある
     * aggregation: 指定したフィールドのジャンル別の数
     * sort: sort項目の内容
     * .fetch: ページの表示数
     *
     * @param form
     * @param pageable
     * @return
     */
    @Override
    public SearchResult<DocumentIndex> search(DocumentFullSearchForm form, Set<String> scope, Pageable pageable) {

        SearchSession searchSession = Search.session(entityManager);

        List<String> tokens = null;
        if (form.getQ() != null) {
            tokens = List.of(form.getQ().replace('　', ' ').split(" "));
        }

        AggregationKey<Map<String, Long>> countsByDocCategory2 = AggregationKey.of("countsByDocCategory2");
        AggregationKey<Map<String, Long>> countsByDocService2 = AggregationKey.of("countsByDocService2");

        int pageSize = 10;
        long offset = 0;

        if (pageable != null) {
            pageSize = pageable.getPageSize();
            offset = pageable.getOffset();
        }

        List<String> finalTokens = tokens;
        SearchResult<DocumentIndex> result = searchSession.search(DocumentIndex.class)
                .where(
                        f -> {
                            boolean hasSearch = false;
                            BooleanPredicateClausesStep<?> b1 = f.bool();

                            // キーワードによる検索(ファイルの中身、本文、タイトル、ファイル名をOR条件で検索）
                            if (finalTokens != null) {
                                BooleanPredicateClausesStep<?> b2 = f.bool();
                                for (String s : finalTokens) {
                                    BooleanPredicateClausesStep<?> b3 = f.bool();

                                    if (form.getKeywordSearchTarget().contains(KeywordSearchTarget.CONTENT)) {
                                        b3 = b3.should(f.match().field("content").matching(s));
                                    }

                                    if (form.getKeywordSearchTarget().contains(KeywordSearchTarget.BODY)) {
                                        b3 = b3.should(f.match().field("bodyPlain").matching(s));
                                    }

                                    if (form.getKeywordSearchTarget().contains(KeywordSearchTarget.TITLE)) {
                                        b3 = b3.should(f.wildcard().field("title").matching("*" + s + "*"));
                                    }

                                    if (form.getKeywordSearchTarget().contains(KeywordSearchTarget.FILENAME)) {
                                        b3 = b3.should(f.wildcard().field("fileManaged.originalFilename").matching("*" + s + "*"));
                                    }
                                    b2.must(b3);
                                }
                                b1.must(b2);
                                hasSearch = true;
                            }

                            // 区分２・サービス種別による検索
                            if (form.getFacetsDoc2() != null && !form.getFacetsDoc2().isEmpty()) {
                                BooleanPredicateClausesStep<?> b2 = f.bool();
                                for (String s : form.getFacetsDoc2()) {
                                    b2 = b2.should(f.match().field("docCategoryVariable2.value1").matching(s));
                                }
                                b1.must(b2);
                                hasSearch = true;
                            }

                            // サービス種別による検索
                            if (form.getFacetsService2() != null && !form.getFacetsService2().isEmpty()) {
                                BooleanPredicateClausesStep<?> b2 = f.bool();
                                for (String s : form.getFacetsService2()) {
                                    b2 = b2.should(f.match().field("docServiceVariable2.value1").matching(s));
                                }
                                b1.must(b2);
                                hasSearch = true;
                            }

                            // 期間指定
                            if (form.getPeriod() != null && !"not".equals(form.getPeriod())) {
                                BooleanPredicateClausesStep<?> b2 = f.bool();
                                b2 = b2.must(f.range().field("lastModifiedDate")
                                        .atLeast(getFromDate(form.getPeriod(), dateFactory.newLocalDateTime())));
                                b1.must(b2);
                                hasSearch = true;
                            }

                            // 公開範囲

                            if (scope != null) {
                                BooleanPredicateClausesStep<?> b2 = f.bool();
                                for (String s : scope) {
                                    b2.should(f.match().field("publicScope").matching(s));
                                }
                                b1.must(b2);
                            }

                            return b1;
                        }
                )
                .aggregation(countsByDocCategory2, f -> f.terms().field("docCategory2", String.class))
                .aggregation(countsByDocService2, f -> f.terms().field("docService2", String.class))
                .sort(
                        f -> {
                            if ("score".equals(form.getSort())) {
                                return f.score();
                            } else {
                                return f.field("lastModifiedDate").desc();
                            }
                        }
                )
                .fetch((int) offset, pageSize);

        return result;
    }

    /**
     * 指定した日付(dt)の１週間後 / １ヶ月後 / １年後を計算する。
     * @param period week = １週間 / month =１ヶ月 / year =１年後
     * @param dt 計算の基準となる日時
     * @return 計算後の日時
     */
    private LocalDateTime getFromDate(String period, LocalDateTime dt) {
        if ("year".equals(period)) {
            return dt.minusYears(1);
        }
        else if ("month".equals(period)) {
            return dt.minusMonths(1);
        }
        else if ("week".equals(period)) {
            return dt.minusWeeks(1);
        }
        return dt;
    }


    /**
     * textからtermの箇所を検索し、その前後の文字を抽出する
     *
     * @param text ヒットした添付ファイルの内容(content)
     * @param term 入力フォームに入力した内容
     * @return ヒットした箇所前後の文字列
     */
    @Override
    public String highlight(String text, String term, String fieldName) {
        try {

            SearchMapping mapping = Search.mapping(entityManager.getEntityManagerFactory());
            IndexManager indexManager = mapping.indexManager("DocumentIndex");
            Backend backend = mapping.backend();
            LuceneBackend luceneBackend = backend.unwrap(LuceneBackend.class);
            Analyzer analyzer = luceneBackend.analyzer("japanese").orElseThrow(() -> new IllegalStateException());
//
//            Analyzer analyzer2 = new JapaneseAnalyzer();
            QueryParser queryParser = new QueryParser(fieldName, analyzer);
//            queryParser.setAllowLeadingWildcard(true);
            Query query = queryParser.parse(term);
            QueryScorer scorer = new QueryScorer(query, fieldName);
            Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
            Highlighter highlighter = new Highlighter(formatter, scorer);

            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            highlighter.setTextFragmenter(fragmenter);

            TokenStream stream = analyzer.tokenStream(fieldName, text);
//
            String[] frags = highlighter.getBestFragments(stream, text, 1);


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

    @Override
    public String highlightKeywordField(String text, List<String> terms) {

        String highlightText = text;
        for (String term : terms) {
            highlightText = highlightText.replace(term, "<em>" + term + "</em>");
        }
        return highlightText;
    }

    /**
     * 日本語形態素解析を行う
     *
     * @param q 入力フォームに入力した内容
     * @return 形態素解析の結果(List)
     */
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
                System.out.println(term + "\t" // 表層形
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

    /**
     * ジャンル別に集約したデータからコードを抜き出し、コードで検索する
     *
     * @param text ジャンル別に集約したコード
     * @return
     */
    @Override
    public List<Variable> label(Map<String, Long> text) {
        // Mapのコードの値だけをリストに格納する
        List<String> code = new ArrayList<>();
        //variableRepository.findByCode(code);
        return null;
    }
}
