package jp.co.stnet.cms.sales.domain.model.variable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariableBean implements Serializable {

    /**
     * コード
     */
    private String code;

    /**
     * コード名称
     */
    private String codeName;

    /**
     * レベル
     */
    private String level;

    /**
     * カウント数
     */
    private Long counts;

    /**
     * 配下のリスト
     */
    private List<VariableBean> child;
}
