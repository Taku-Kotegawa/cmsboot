package jp.co.stnet.cms.sales.domain.model.document;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import jp.co.stnet.cms.common.validation.IsDate;
import jp.co.stnet.cms.common.validation.Parseable;
import lombok.Data;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static jp.co.stnet.cms.common.validation.ParseableType.TO_INT;
import static jp.co.stnet.cms.common.validation.ParseableType.TO_LONG;

@Data
@CsvEntity
public class DocumentCsvBean implements Serializable {

    @Parseable(value = TO_LONG)
    @CsvColumn(name = "id")
    private String id;

    /**
     * ステータス
     */
    @Parseable(value = TO_INT)
    @Pattern(regexp = "^[0129]$")
    @CsvColumn(name = "status")
    private String status;

    /**
     * ステータス(ラベル)
     */
    @CsvColumn(name = "statusLabel")
    private String statusLabel;

    /**
     * タイトル
     */
    @CsvColumn(name = "title")
    private String title;

    /**
     * 区分 - コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_CATEGORY")
    @CsvColumn(name = "docCategory")
    private String docCategory;

    /**
     * 区分 - 区分1
     */
    @CsvColumn(name = "docCategoryValue1")
    private String docCategoryValue1;

    /**
     * 区分 - 区分2
     */
    @CsvColumn(name = "docCategoryValue2")
    private String docCategoryValue2;

    /**
     * 区分 - 区分3
     */
    @CsvColumn(name = "docCategoryValue3")
    private String docCategoryValue3;

    /**
     * サービス - コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_SERVICE")
    @CsvColumn(name = "docService")
    private String docService;

    /**
     * サービス - 事業
     */
    @CsvColumn(name = "docServiceValue1")
    private String docServiceValue1;

    /**
     * サービス - サービス種別
     */
    @CsvColumn(name = "docServiceValue2")
    private String docServiceValue2;

    /**
     * サービス - サービス
     */
    @CsvColumn(name = "docServiceValue3")
    private String docServiceValue3;

    /**
     * 本文
     */
    @CsvColumn(name = "body")
    private String body;

    /**
     * ドキュメント管理番号
     */
    @CsvColumn(name = "documentNumber")
    private String documentNumber;

    /**
     * 管理部門
     */
    @CsvColumn(name = "chargeDepartment")
    private String chargeDepartment;

    /**
     * 管理担当者
     */
    @CsvColumn(name = "chargePerson")
    private String chargePerson;

    /**
     * 制定日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "enactmentDate")
    private String enactmentDate;

    /**
     * 最終改定日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "lastRevisedDate")
    private String lastRevisedDate;

    /**
     * 実施日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "implementationDate")
    private String implementationDate;

    /**
     * 制定箇所
     */
    @CsvColumn(name = "enactmentDepartment")
    private String enactmentDepartment;

    /**
     * 変更理由
     */
    @CsvColumn(name = "reasonForChange")
    private String reasonForChange;

    /**
     * 活用シーン
     */
    @CsvColumn(name = "useStage")
    private String useStage;

    /**
     * 活用シーン(ラベル)
     */
    @CsvColumn(name = "useStageLabel")
    private String useStageLabel;

    /**
     * 想定読者
     */
    @CsvColumn(name = "intendedReader")
    private String intendedReader;

    /**
     * 概要
     */
    @CsvColumn(name = "summary")
    private String summary;

    /**
     * 文書種類
     */
    @CsvColumn(name = "fileTypeLabel")
    private String fileTypeLabel;
    /**
     * ファイル名
     */
    @CsvColumn(name = "filesLabel")
    private String filesLabel;

    /**
     * ファイル名(PDF)
     */
    @CsvColumn(name = "pdfFilesLabel")
    private String pdfFilesLabel;

    /**
     * 公開区分
     */
    @CsvColumn(name = "publicScope")
    private String publicScope;

    /**
     * 公開区分(ラベル)
     */
    @CsvColumn(name = "publicScopeLabel")
    private String publicScopeLabel;

    /**
     * 顧客公開
     */
    @CsvColumn(name = "customerPublic")
    private String customerPublic;

    /**
     * 顧客公開(ラベル)
     */
    @CsvColumn(name = "customerPublicLabel")
    private String customerPublicLabel;

    /**
     * 最終更新者(ユーザID)
     */
    @CsvColumn(name = "lastModifiedBy")
    private String lastModifiedBy;

    /**
     * 最終更新者(氏名)
     */
    @CsvColumn(name = "lastModifiedByLabel")
    private String lastModifiedByLabel;

    /**
     * 最終更新日時
     */
    @IsDate("yyyy/MM/dd HH:mm:ss")
    @CsvColumn(name = "lastModifiedDate")
    private String lastModifiedDate;
}
