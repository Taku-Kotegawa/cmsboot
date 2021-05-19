package jp.co.stnet.cms.base.application.service.variable;


import jp.co.stnet.cms.base.application.repository.variable.VariableRepository;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class VariableSharedServiceImpl implements VariableSharedService {

    @Autowired
    VariableRepository variableRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Variable> findAllByType(String type) {
        return variableRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Variable> findAllByTypeAndCode(String type, String code) {
        return variableRepository.findAllByTypeAndCode(type, code);
    }

}
