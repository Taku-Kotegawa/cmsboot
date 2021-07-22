package jp.co.stnet.cms.base.application.service.variable;


import jp.co.stnet.cms.base.application.service.NodeIService;
import jp.co.stnet.cms.base.domain.model.variable.Variable;

import java.util.List;

/**
 * VariableService
 */
public interface VariableService extends NodeIService<Variable, Long> {

    /**
     * タイプで検索する。
     *
     * @param type タイプ
     * @return ヒットしたデータのリスト
     */
    List<Variable> findAllByType(String type);

    /**
     * タイプとコードで検索する。
     *
     * @param type タイプ
     * @param code コード
     * @return ヒットしたデータのリスト
     */
    List<Variable> findAllByTypeAndCode(String type, String code);

    /**
     * タイプとvalueXの値で検索する。(X = 1 〜 10)
     *
     * @param type  タイプ
     * @param i     valueXのXを示す整数(1 - 10)
     * @param value valueXの値
     * @return ヒットしたデータのリスト
     */
    List<Variable> findAllByTypeAndValueX(String type, int i, String value);

}
