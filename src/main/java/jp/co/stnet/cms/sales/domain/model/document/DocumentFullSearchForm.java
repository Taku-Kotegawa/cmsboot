package jp.co.stnet.cms.sales.domain.model.document;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class DocumentFullSearchForm {

    /**
     * 全文検索の入力内容
     */
    private String q;

    /**
     * 期間指定
     */
    private String period;

    /**
     * 期間指定をLocalDateTimeに変換したもの
     */
    private String periodDate;

    /**
     * ソート順
     */
    private String sort;

    /**
     * ファセット項目で選択した区分2
     */
    private List<String> facetsDoc2;

    /**
     * ファセット項目で選択したサービス種別
     */
    private List<String> facetsService2;

    /**
     * キーワード検索の対象
     */
    private Set<KeywordSearchTarget> keywordSearchTarget;

}
