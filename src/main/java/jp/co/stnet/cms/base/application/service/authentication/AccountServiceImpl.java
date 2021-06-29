package jp.co.stnet.cms.base.application.service.authentication;



import jp.co.stnet.cms.base.application.repository.authentication.AccountRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.authentication.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    protected AccountServiceImpl() {
        super(Account.class);
    }

    @Override
    protected JpaRepository<Account, String> getRepository() {
        return accountRepository;
    }

    @Override
    @PostAuthorize("returnObject == true")
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority(Permission.ADMIN_USER.name()));
    }

//    @Override
//    public Account save(Account account, String ImageUuid) {
//
//        if (ImageUuid != null) {
//            FileManaged fileManaged = fileManagedSharedService.findByUuid(ImageUuid);
//
//            AccountImage image = AccountImage.builder()
//                    .username(account.getUsername())
//                    .extension(StringUtils.getFilenameExtension(fileManaged.getOriginalFilename()))
//                    .body(fileManagedSharedService.getFile(fileManaged.getFid()))
//                    .build();
//            accountImageRepository.save(image);
//        }
//
//        return getRepository().save(account);
//    }

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

    @Override
    public String getUserFullName(String username) {
        Account account = accountRepository.findById(username).orElse(null);
        if (account != null) {
            return account.getLastName() + " " + account.getFirstName();
        } else {
            return "";
        }
    }
}
