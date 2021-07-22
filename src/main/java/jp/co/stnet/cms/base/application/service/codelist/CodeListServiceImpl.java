package jp.co.stnet.cms.base.application.service.codelist;

import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.codelist.ReloadableCodeList;

import javax.inject.Named;

@Slf4j
@Service
@Transactional
public class CodeListServiceImpl implements CodeListService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    @Named("CL_SAMPLE")
    ReloadableCodeList clSample;

    @Override
    public void refresh(String codeListName) {

        try {
            String CodeListBeanName = VariableType.valueOf(codeListName).getCodeListBeanName();
            if (CodeListBeanName != null && !CodeListBeanName.isEmpty()) {
                ReloadableCodeList codeList = (ReloadableCodeList) applicationContext.getBean(CodeListBeanName);
                codeList.refresh();
            }
        } catch (Exception e) {

        }

    }
}
