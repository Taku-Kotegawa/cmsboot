package jp.co.stnet.cms.example.application.service;


import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeRevService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.example.application.repository.simpleentity.SimpleEntityRepository;
import jp.co.stnet.cms.example.application.repository.simpleentity.SimpleEntityRevisionRepository;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityMaxRev;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityRevision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional
public class SimpleEntityServiceImpl extends AbstractNodeRevService<SimpleEntity, SimpleEntityRevision, SimpleEntityMaxRev, Long> implements SimpleEntityService {

    @Autowired
    SimpleEntityRepository simpleEntityRepository;

    @Autowired
    SimpleEntityRevisionRepository simpleEntityRevisionRepository;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    protected SimpleEntityServiceImpl() {
        super(SimpleEntity.class, SimpleEntityRevision.class, SimpleEntityMaxRev.class);
    }

    @Override
    protected JpaRepository<SimpleEntity, Long> getRepository() {
        return this.simpleEntityRepository;
    }

    @Override
    protected NodeRevRepository<SimpleEntityRevision, Long> getRevisionRepository() {
        return this.simpleEntityRevisionRepository;
    }

    @Override
    public SimpleEntity save(SimpleEntity entity) {

        SimpleEntity simpleEntity = super.save(entity);

        // 添付ファイル確定
        fileManagedSharedService.permanent(entity.getAttachedFile01Uuid());

        return simpleEntity;
    }

    @Override
    public SimpleEntity saveDraft(SimpleEntity entity) {
        SimpleEntity simpleEntity =  super.saveDraft(entity);

        // 添付ファイル確定
        fileManagedSharedService.permanent(entity.getAttachedFile01Uuid());

        return simpleEntity;
    }

    @Override
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return true;
    }

    @Override
    public boolean equalsEntity(SimpleEntity entity, SimpleEntity currentCopy) {
        currentCopy.setAttachedFile01Managed(null);
        return Objects.equals(entity, currentCopy);
    }
}
