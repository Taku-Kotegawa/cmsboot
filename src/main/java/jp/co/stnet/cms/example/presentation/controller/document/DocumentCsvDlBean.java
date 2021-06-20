package jp.co.stnet.cms.example.presentation.controller.document;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@CsvEntity
public class DocumentCsvDlBean implements Serializable {

    @CsvColumn(name = "id", position = 0)
    private Long id;

    /**
     * ステータス(ラベル)
     */
    @CsvColumn(name = "status", position = 1)
    private String statusLabel;

    /**
     * タイトル
     */
    @CsvColumn(name = "title", position = 2)
    private String title;

    /**
     * 本文
     */
    @CsvColumn(name = "body", position = 3)
    private String body;

    /**
     * 公開区分
     */
    @CsvColumn(name = "publicScope", position = 4)
    private String publicScope;

    /**
     * 管理部門
     */
    @CsvColumn(name = "chargeDepartment", position = 5)
    private String chargeDepartment;

    /**
     * 管理担当者
     */
    @CsvColumn(name = "chargePerson", position = 6)
    private String chargePerson;

    /**
     * 制定日
     */
    private LocalDate enactmentDate;

    /**
     * 最終改定日
     */
    private LocalDate lastRevisedDate;

    /**
     * 実施日
     */
    private LocalDate implementationDate;

    /**
     * 制定箇所
     */
    private String enactmentDepartment;

    /**
     * 変更理由
     */
    private String reasonForChange;

    /**
     * 利用シーン
     */
    private Set<String> useStage;

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 概要
     */
    private String summary;


}
