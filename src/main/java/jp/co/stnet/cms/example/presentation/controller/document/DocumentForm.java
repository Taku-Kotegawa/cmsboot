package jp.co.stnet.cms.example.presentation.controller.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.co.stnet.cms.example.domain.model.document.DocPublicScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentForm implements Serializable {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(groups = Update.class)
    private Long version;

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
    private String publicScope = DocPublicScope.ALL.getValue();

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
     * 区分
     */
    @NotNull
    private Long docCategory;

    /**
     * サービス
     */
    private Long docService;

    /**
     * ファイル
     */
    private Collection<@Valid FileForm> files = new ArrayList<>();

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 概要
     */
    private String summary;

    /**
     * 変更履歴を残す
     */
    private boolean saveRevision;

    public interface Create {
    }

    public interface Update {
    }

}
