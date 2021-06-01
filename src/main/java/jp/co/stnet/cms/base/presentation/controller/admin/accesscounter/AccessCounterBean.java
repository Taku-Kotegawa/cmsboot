package jp.co.stnet.cms.base.presentation.controller.admin.accesscounter;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccessCounterBean implements Serializable {

    /**
     * 内部ID
     */
    private Long id;

    /**
     * バージョン
     */
    private Long version;

    /**
     * ステータス
     */
    private String status;

    /**
     * ステータス
     */
    private String statusLabel;

    /**
     * URL
     */
    private String url;

    /**
     * アクセス数
     */
    private Long count;

}
