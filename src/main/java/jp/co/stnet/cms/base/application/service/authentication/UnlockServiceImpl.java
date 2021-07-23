package jp.co.stnet.cms.base.application.service.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UnlockServiceImpl implements UnlockService {

    @Autowired
    AuthenticationEventService authenticationEventService;

    @Override
    public void unlock(String username) {
        authenticationEventService.deleteFailureEventByUsername(username);
    }

}
