package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndex;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndexPK;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DocumentIndexSearchRow extends DocumentIndex {

    /**
     * ハイライトテキスト
     */
    private String contentHighlight;

    /**
     * ハイライトボディ
     */
    private String bodyHighlight;

    /**
     * タイトルハイライト
     */
    private String titleHighlight;

    /**
     *
     */
    private String fileNameHighlight;

    /**
     * 区分1 コード名称
     */
    private String docCategory1Name;

    /**
     * 区分2 コード名称
     */
    private String docCategory2Name;

    /**
     * サービス-事業領域 コード名称
     */
    private String docService1Name;

    /**
     * サービス-サービス種別 コード名称
     */
    private String docService2Name;

    /**
     * サービス-サービス コード名称
     */
    private String docService3Name;
}