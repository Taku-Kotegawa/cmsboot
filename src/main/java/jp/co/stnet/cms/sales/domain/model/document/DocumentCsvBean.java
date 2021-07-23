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
     * ドキュメント管理番号
     */
    @CsvColumn(name = "documentNumber")
    private String documentNumber;

    /**
     * タイトル
     */
    @CsvColumn(name = "title")
    private String title;

    /**
     * 版数
     */
    @CsvColumn(name = "versionNumber")
    private String versionNumber;

    /**
     * 区分 - 区分1コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_CATEGORY1")
    @CsvColumn(name = "docCategory1")
    private String docCategory1;

    /**
     * 区分 - 区分1
     */
    @CsvColumn(name = "docCategoryValue1")
    private String docCategoryValue1;
    /**
     * 区分 - 区分1コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_CATEGORY2")
    @CsvColumn(name = "docCategory2")
    private String docCategory2;

    /**
     * 区分 - 区分2
     */
    @CsvColumn(name = "docCategoryValue2")
    private String docCategoryValue2;

    /**
     * サービス - 事業コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_SERVICE1")
    @CsvColumn(name = "docService1")
    private String docService1;

    /**
     * サービス - 事業
     */
    @CsvColumn(name = "docServiceValue1")
    private String docServiceValue1;

    /**
     * サービス - サービス種別コード
     */
    @ExistInCodeList(codeListId = "CL_DOC_SERVICE2")
    @CsvColumn(name = "docService2")
    private String docService2;

    /**
     * サービス - サービス種別
     */
    @CsvColumn(name = "docServiceValue2")
    private String docServiceValue2;

    /**
     * サービス - サービスコード
     */
    @ExistInCodeList(codeListId = "CL_DOC_SERVICE3")
    @CsvColumn(name = "docService3")
    private String docService3;

    /**
     * サービス - サービス
     */
    @CsvColumn(name = "docServiceValue3")
    private String docServiceValue3;

    /**
     * 発行日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "publishedDate")
    private String publishedDate;

    /**
     * 改定日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "lastRevisedDate")
    private String lastRevisedDate;

    /**
     * 廃止日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "invalidationDate")
    private String invalidationDate;

    /**
     * 周知日
     */
    @IsDate("yyyy/MM/dd")
    @CsvColumn(name = "announceDate")
    private String announceDate;

    /**
     * 作成担当者
     */
    @CsvColumn(name = "chargePersonForCreation")
    private String chargePersonForCreation;

    /**
     * 発行担当者
     */
    @CsvColumn(name = "chargePersonForPublish")
    private String chargePersonForPublish;

    /**
     * 発行責任者
     */
    @CsvColumn(name = "responsiblePersonForPublish")
    private String responsiblePersonForPublish;

    /**
     * 作成部門
     */
    @CsvColumn(name = "departmentForCreation")
    private String departmentForCreation;

    /**
     * 発行部門
     */
    @CsvColumn(name = "departmentForPublish")
    private String departmentForPublish;

    /**
     * 備考
     */
    @CsvColumn(name = "remark")
    private String remark;

    /**
     * 変更理由
     */
    @CsvColumn(name = "reasonForChange")
    private String reasonForChange;

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
     * 文書種類
     */
    @CsvColumn(name = "fileMemo")
    private String fileMemo;

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
     * 本文
     */
    @CsvColumn(name = "body")
    private String body;

    /**
     * 最終更新日時
     */
    @IsDate("yyyy/MM/dd HH:mm:ss")
    @CsvColumn(name = "lastModifiedDate")
    private String lastModifiedDate;

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

}
