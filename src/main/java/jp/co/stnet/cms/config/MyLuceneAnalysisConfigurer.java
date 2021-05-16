package jp.co.stnet.cms.config;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.ja.JapaneseTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

/**
 * 全文検索エンジン(Lucene)の設定クラス.
 */
public class MyLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {

        // 日本語解析用(辞書ファイルを指定)
        context.analyzer("japanese").custom()
                .tokenizer(JapaneseTokenizerFactory.class)
                .param("mode", "search")
                .param("userDictionary", "META-INF/dict/userdict_ja.txt")
                .tokenFilter(LowerCaseFilterFactory.class)
                .tokenFilter(ASCIIFoldingFilterFactory.class);
    }
}
