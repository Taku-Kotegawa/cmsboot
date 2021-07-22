package jp.co.stnet.cms.example.application.service;

import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeRevService;
import jp.co.stnet.cms.example.application.repository.select.SelectRepository;
import jp.co.stnet.cms.example.application.repository.select.SelectRevisionRepository;
import jp.co.stnet.cms.example.domain.model.select.Select;
import jp.co.stnet.cms.example.domain.model.select.SelectMaxRev;
import jp.co.stnet.cms.example.domain.model.select.SelectRevision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SelectServiceImpl extends AbstractNodeRevService<Select, SelectRevision, SelectMaxRev, Long> implements SelectService {

    @Autowired
    SelectRepository selectRepository;

    @Autowired
    SelectRevisionRepository selectRevisionRepository;

    protected SelectServiceImpl() {
        super(Select.class, SelectRevision.class, SelectMaxRev.class);
    }

    @Override
    protected NodeRevRepository<SelectRevision, Long> getRevisionRepository() {
        return this.selectRevisionRepository;
    }

    @Override
    protected JpaRepository<Select, Long> getRepository() {
        return this.selectRepository;
    }

}
