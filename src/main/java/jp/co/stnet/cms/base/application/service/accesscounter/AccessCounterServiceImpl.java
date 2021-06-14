package jp.co.stnet.cms.base.application.service.accesscounter;

import jp.co.stnet.cms.base.application.repository.accesscounter.AccessCounterRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.AccessCounter;
import jp.co.stnet.cms.base.domain.model.common.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AccessCounterServiceImpl extends AbstractNodeService<AccessCounter, Long> implements AccessCounterService {

    @Autowired
    AccessCounterRepository accessCounterRepository;

    @Override
    @Transactional(readOnly = true)
    protected JpaRepository<AccessCounter, Long> getRepository() {
        return accessCounterRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccessCounter> findByUrl(String url) {
        return accessCounterRepository.findByUrl(url);
    }

    @Override
    public long countUp(String url) {
        AccessCounter accessCounter = accessCounterRepository.findByUrl(url)
                .orElse(AccessCounter.builder()
                        .url(url)
                        .count(0L)
                        .status(Status.VALID.getCodeValue())
                        .build());
        accessCounter.setCount(accessCounter.getCount() + 1);
        accessCounterRepository.save(accessCounter);
        return accessCounter.getCount();
    }
}
