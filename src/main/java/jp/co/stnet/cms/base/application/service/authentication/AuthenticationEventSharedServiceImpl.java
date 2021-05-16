package jp.co.stnet.cms.base.application.service.authentication;

import jp.co.stnet.cms.base.application.repository.authentication.FailedAuthenticationRepository;
import jp.co.stnet.cms.base.application.repository.authentication.SuccessfulAuthenticationRepository;
import jp.co.stnet.cms.base.domain.model.authentication.FailedAuthentication;
import jp.co.stnet.cms.base.domain.model.authentication.SuccessfulAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class AuthenticationEventSharedServiceImpl implements AuthenticationEventSharedService {

    @Autowired
    SuccessfulAuthenticationRepository successfulAuthenticationRepository;

    @Autowired
    FailedAuthenticationRepository failedAuthenticationRepository;

    @Override
    public List<SuccessfulAuthentication> findLatestSuccessEvents(String username, int count) {
        return successfulAuthenticationRepository.findAll(
                Example.of(SuccessfulAuthentication.builder().username(username).build()),
                PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "authenticationTimestamp"))
        ).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FailedAuthentication> findLatestFailureEvents(String username, int count) {
        return failedAuthenticationRepository.findAll(
                Example.of(FailedAuthentication.builder().username(username).build()),
                PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "authenticationTimestamp"))
        ).getContent();
    }

    @Override
    public void authenticationSuccess(String username) {
        successfulAuthenticationRepository.save(
                SuccessfulAuthentication.builder()
                        .username(username)
                        .build()
        );
        deleteFailureEventByUsername(username);
    }

    @Override
    public void authenticationFailure(String username) {
        failedAuthenticationRepository.save(
                FailedAuthentication.builder()
                        .username(username)
                        .build()
        );
    }

    @Override
    public long deleteFailureEventByUsername(String username) {
        return failedAuthenticationRepository.deleteByUsername(username);
    }
}
