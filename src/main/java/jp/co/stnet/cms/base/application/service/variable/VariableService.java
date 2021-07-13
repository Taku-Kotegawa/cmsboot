package jp.co.stnet.cms.base.application.service.variable;


import jp.co.stnet.cms.base.application.service.NodeIService;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import org.aspectj.weaver.ast.Var;

import java.util.List;

/**
 * VariableService
 */
public interface VariableService extends NodeIService<Variable, Long> {

    List<Variable> findAllByTypeAndCode(String type, String code);

    List<Variable> findAllByType(String type);

}
