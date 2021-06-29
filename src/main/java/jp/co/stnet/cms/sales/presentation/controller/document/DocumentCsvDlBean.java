package jp.co.stnet.cms.sales.presentation.controller.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@CsvEntity
public class DocumentCsvDlBean implements Serializable {

    @CsvColumn(name = "id")
    private Long id;

    /**
     * ステータス(ラベル)
     */
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
    @CsvColumn(name = "docCategoryCode")
    private String docCategoryCode;

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
    @CsvColumn(name = "docServiceCode")
    private String docServiceCode;

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
    @CsvColumn(name = "enactmentDate", format = "yyyy/MM/dd")
    private Date enactmentDate;

    /**
     * 最終改定日
     */
    @CsvColumn(name = "lastRevisedDate", format = "yyyy/MM/dd")
    private Date lastRevisedDate;

    /**
     * 実施日
     */
    @CsvColumn(name = "implementationDate", format = "yyyy/MM/dd")
    private Date implementationDate;

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
    @CsvColumn(name = "lastModifiedDate", format = "yyyy/MM/dd HH:mm:ss")
    private Date lastModifiedDate;
}
