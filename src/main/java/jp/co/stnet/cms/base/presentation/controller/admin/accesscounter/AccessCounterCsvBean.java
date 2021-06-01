package jp.co.stnet.cms.base.presentation.controller.admin.accesscounter;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * アクセスカウンター管理のCSVファイルのBean
 */
@Data
@CsvEntity
public class AccessCounterCsvBean implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1396585187753711980L;

    // TODO 項目のカスタマイズ

    /**
     * 内部ID
     */
    @CsvColumn(name = "ID")
    private Long id;
    /**
     * バージョン
     */
    @CsvColumn(name = "バージョン")
    private Long version;
    /**
     * ステータス
     */
    @CsvColumn(name = "ステータス")
    private String status;
    /**
     * ステータス
     */
    @CsvColumn(name = "ステータス")
    private String statusLabel;
    /**
     * URL
     */
    @CsvColumn(name = "URL")
    private String url;
    /**
     * アクセス数
     */
    @CsvColumn(name = "アクセス数")
    private Long count;
}