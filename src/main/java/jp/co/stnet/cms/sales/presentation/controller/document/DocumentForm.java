package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.sales.domain.model.document.CustomerPublic;
import jp.co.stnet.cms.sales.domain.model.document.DocPublicScope;
import jp.co.stnet.cms.sales.domain.validation.ValidService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidService
public class DocumentForm implements Serializable {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(groups = Update.class)
    private Long version;

    /**
     * タイトル
     */
    @NotNull
    private String title;

    /**
     * 本文
     */
    @NotNull
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
     * 版数
     */
    private String versionNumber;

    /**
     * 作成部門
     */
    @NotNull
    private String departmentForCreation;

    /**
     * 作成担当者
     */
    @NotNull
    private String chargePersonForCreation;

    /**
     * 発行担当者
     */
    private String chargePersonForPublish;

    /**
     * 発行部門
     */
    private String departmentForPublish;

    /**
     * 発行責任者
     */
    private String responsiblePersonForPublish;

    /**
     * 発行日
     */
    private LocalDate publishedDate;

    /**
     * 改定日
     */
    private LocalDate lastRevisedDate;

    /**
     * 廃止日
     */
    private LocalDate invalidationDate;

    /**
     * 周知日
     */
    private LocalDate announceDate;

    /**
     * 変更理由
     */
    private String reasonForChange;

    /**
     * 活用シーン
     */
    private Set<String> useStage;

    /**
     * 区分1
     */
    @NotNull
    private String docCategory1;

    /**
     * 区分2
     */
    @NotNull
    private String docCategory2;

    /**
     * サービス-事業領域
     */
    @NotNull
    private String docService1;

    /**
     * サービス-サービス種別
     */
    @NotNull
    private String docService2;

    /**
     * サービス-サービス
     */
    private String docService3;

    /**
     * ファイル
     */
    private List<@Valid FileForm> files = new ArrayList<>();

    /**
     * 想定読者
     */
    private String intendedReader;

    /**
     * 備考
     */
    private String remark;

    /**
     * 変更履歴を残す
     */
    private boolean saveRevision;

    /**
     * 顧客公開区分
     */
    private String customerPublic = CustomerPublic.OPEN.getValue();


    public interface Create {
    }

    public interface Update {
    }

}
