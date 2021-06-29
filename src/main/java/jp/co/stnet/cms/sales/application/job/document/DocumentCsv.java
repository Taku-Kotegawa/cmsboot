package jp.co.stnet.cms.sales.application.job.document;

import jp.co.stnet.cms.common.validation.IsDate;
import jp.co.stnet.cms.common.validation.Parseable;
import lombok.Data;
import org.terasoluna.gfw.common.codelist.ExistInCodeList;

import javax.validation.constraints.Pattern;

import static jp.co.stnet.cms.common.validation.ParseableType.TO_INT;
import static jp.co.stnet.cms.common.validation.ParseableType.TO_LONG;

@Data
public class DocumentCsv {

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
     * 本文
     */
    private String body;

    /**
     * 公開区分
     */
    private String publicScope;

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
    private String publicScopeLabel;

    /**
     * 文書種類
     */
    private String fileTypeLabel;

    /**
     * 顧客公開
     */
    private String customerPublicLabel;
}
