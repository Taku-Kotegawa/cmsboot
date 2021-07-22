package jp.co.stnet.cms.base.application.service.authentication;


import jp.co.stnet.cms.base.application.repository.authentication.AccountRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.codelist.ReloadableCodeList;

import javax.inject.Named;
import java.util.List;
import java.util.UUID;

/**
 * AccountServiceImpl
 */
@Slf4j
@Service
@Transactional
public class AccountServiceImpl extends AbstractNodeService<Account, String> implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    @Named("CL_ACCOUNT_FULLNAME")
    ReloadableCodeList accountFullNameCodeList;

    protected AccountServiceImpl() {
        super(Account.class);
    }

    @Override
    protected JpaRepository<Account, String> getRepository() {
        return accountRepository;
    }

    @Override
    protected void afterSave(Account entity, Account current) {
        super.afterSave(entity, current);
        accountFullNameCodeList.refresh();
    }

    @Override
    public String generateApiKey(String username) {
        return UUID.randomUUID().toString();
    }

    @Override
    public Account deleteApiKey(String username) {
        Account account = findById(username);
        account.setApiKey(null);
        return save(account);
    }

    @Override
    public Account saveApiKey(String username) {
        Account account = findById(username);
        account.setApiKey(generateApiKey(username));
        return save(account);
    }

    @Override
    public Account findByApiKey(String apiKey) {
        return accountRepository.findByApiKey(apiKey);
    }

    @Override
    public List<Account> findAllById(Iterable<String> ids) {
        return accountRepository.findAllById(ids);
    }

}
