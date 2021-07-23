package jp.co.stnet.cms.base.application.service.authentication;


import jp.co.stnet.cms.base.application.repository.authentication.FailedPasswordReissueRepository;
import jp.co.stnet.cms.base.domain.model.authentication.FailedPasswordReissue;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class PasswordReissueFailureServiceImpl implements PasswordReissueFailureService {

    @Autowired
    FailedPasswordReissueRepository failedPasswordReissueRepository;

    @Autowired
    CustomDateFactory dateFactory;

    @Override
    public void resetFailure(String username, String token) {
        failedPasswordReissueRepository.save(
                FailedPasswordReissue.builder().token(token).attemptDate(dateFactory.newLocalDateTime()).build()
        );
    }
}
