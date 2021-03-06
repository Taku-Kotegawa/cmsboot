package jp.co.stnet.cms.base.application.service.variable;


import jp.co.stnet.cms.base.application.repository.variable.VariableRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeService;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Variable> findAllByTypeAndCode(String type, String code) {
        return variableRepository.findAllByTypeAndCode(type, code);
    }

    @Override
    public List<Variable> findAllByTypeAndValueX(String type, int i, String value) {

        switch (i) {
            case 1:
                return variableRepository.findAllByTypeAndValue1(type, value);

            case 2:
                return variableRepository.findAllByTypeAndValue2(type, value);

            case 3:
                return variableRepository.findAllByTypeAndValue3(type, value);

            case 4:
                return variableRepository.findAllByTypeAndValue4(type, value);

            case 5:
                return variableRepository.findAllByTypeAndValue5(type, value);

            case 6:
                return variableRepository.findAllByTypeAndValue6(type, value);

            case 7:
                return variableRepository.findAllByTypeAndValue7(type, value);

            case 8:
                return variableRepository.findAllByTypeAndValue8(type, value);

            case 9:
                return variableRepository.findAllByTypeAndValue9(type, value);

            case 10:
                return variableRepository.findAllByTypeAndValue10(type, value);

            default:
                throw new IllegalArgumentException("i must 1 - 10.");
        }
    }

    @Override
    public List<Variable> findAllByType(String type) {
        return variableRepository.findAllByType(type);
    }
}
