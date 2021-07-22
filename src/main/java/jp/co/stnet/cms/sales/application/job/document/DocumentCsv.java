package jp.co.stnet.cms.sales.application.job.document;

import jp.co.stnet.cms.common.validation.IsDate;
import jp.co.stnet.cms.common.validation.Parseable;
import lombok.Data;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import javax.validation.constraints.Pattern;

import static jp.co.stnet.cms.common.validation.ParseableType.TO_INT;
import static jp.co.stnet.cms.common.validation.ParseableType.TO_LONG;

@Deprecated
@Data
public class DocumentCsv {

    /**
     * ID
     */
    @Parseable(value = TO_LONG)
    private String id;

    /**
     * ステータス
     */
    @Parseable(value = TO_INT)
    @Pattern(regexp = "^[0129]$")
    private String status;

    /**
     * ステータス(ラベル)
     */
    private String statusLabel;

    /**
     * タイトル
     */
    private String title;

    /**
     * 区分 - コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_CATEGORY")
    private String docCategory;

    /**
     * 区分 - 区分1
     */
    private String docCategoryValue1;

    /**
     * 区分 - 区分2
     */
    private String docCategoryValue2;

    /**
     * 区分 - 区分3
     */
    private String docCategoryValue3;

    /**
     * サービス - コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_SERVICE")
    private String docService;

    /**
     * サービス - 事業
     */
    private String docServiceValue1;

    /**
     * サービス - サービス種別
     */
    private String docServiceValue2;

    /**
     * サービス - サービス
     */
    private String docServiceValue3;

    /**
     * 本文
     */
    private String body;

    /**
     * ドキュメント管理番号
     */
    private String documentNumber;

    /**
     * 管理部門
     */
    private String chargeDepartment;

    /**
     * 管理担当者
     */
    private String chargePerson;

    /**
     * 制定日
     */
    @IsDate("yyyy/MM/dd")
    private String enactmentDate;

    /**
     * 最終改定日
     */
    @IsDate("yyyy/MM/dd")
    private String lastRevisedDate;

    /**
     * 実施日
     */
    @IsDate("yyyy/MM/dd")
    private String implementationDate;

    /**
     * 制定箇所
     */
    private String enactmentDepartment;

    /**
     * 変更理由
     */
    private String reasonForChange;

    /**
     * 活用シーン
     */
    private String useStageLabel;

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 概要
     */
    private String summary;

    /**
     * 文書種類
     */
    private String fileTypeLabel;
    /**
     * ファイル名
     */
    private String filesLabel;

    /**
     * ファイル名(PDF)
     */
    private String pdfFilesLabel;

    /**
     * 公開区分
     */
    private String publicScope;

    /**
     * 公開区分(ラベル)
     */
    private String publicScopeLabel;

    /**
     * 顧客公開
     */
    private String customerPublic;

    /**
     * 顧客公開(ラベル)
     */
    private String customerPublicLabel;

    /**
     * 最終更新者(ユーザID)
     */
    private String lastModifiedBy;

    /**
     * 最終更新者(氏名)
     */
    private String lastModifiedByLabel;

    /**
     * 最終更新日時
     */
    @IsDate("yyyy/MM/dd HH:mm:ss")
    private String lastModifiedDate;

}
