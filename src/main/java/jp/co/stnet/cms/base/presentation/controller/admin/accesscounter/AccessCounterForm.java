package jp.co.stnet.cms.base.presentation.controller.admin.accesscounter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * アクセスカウンター管理の編集画面のBean
 * @author Automatically generated
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessCounterForm implements Serializable {
 
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1396585187753711980L;

    // TODO validation をカスタマイズ


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


    public interface Create {
    }

    public interface Update {
    }

}