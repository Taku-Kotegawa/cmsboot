package jp.co.stnet.cms.base.application.service.variable;


import jp.co.stnet.cms.base.application.repository.variable.VariableRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import jp.co.stnet.cms.common.datatables.Column;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.query.QueryEscapeUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Service
@Transactional
public class VariableServiceImpl extends AbstractNodeService<Variable, Long> implements VariableService {

    @Autowired
    VariableRepository variableRepository;

    @Override
    protected void beforeSave(Variable entity, Variable current) {

        // 文書区分
        if (VariableType.DOC_CATEGORY.name().equals(entity.getType())) {
            entity.setValue10(
                      entity.getValue1()
                              + separateValue(entity.getValue2())
                              + separateValue(entity.getValue3())
                              + separateValue(entity.getValue4())
                              + separateValue(entity.getValue5())
                              + separateValue(entity.getValue6())
            );
        } else if (VariableType.DOC_SERVICE.name().equals(entity.getType())) {
            entity.setValue10(
                    entity.getValue1()
                            + separateValue(entity.getValue2())
                            + separateValue(entity.getValue3())
                            + separateValue(entity.getValue4())
                            + separateValue(entity.getValue5())
                            + separateValue(entity.getValue6())
            );
        }

        super.beforeSave(entity, current);
    }

    private String separateValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            return " / " + value;
        }
        return "";
    }



    @Override
    protected JpaRepository<Variable, Long> getRepository() {
        return variableRepository;
    }

    @Override
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {

        // TODO 権限チェックの追加
        return true;
    }

    @Override
    public List<Variable> findAllByTypeAndCode(String type, String code) {
        return variableRepository.findAllByTypeAndCode(type, code);
    }
}
