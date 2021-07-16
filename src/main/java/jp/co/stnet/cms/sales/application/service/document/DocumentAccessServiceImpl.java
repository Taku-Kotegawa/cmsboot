package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.sales.application.repository.document.DocumentAccessRepository;
import jp.co.stnet.cms.sales.domain.model.document.DocumentAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@Service
public class DocumentAccessServiceImpl implements DocumentAccessService {

    @Autowired
    DocumentAccessRepository documentAccessRepository;

    @Autowired
    CustomDateFactory dateFactory;

    @Override
    public DocumentAccess save(Long documentId, String username) {

        DocumentAccess documentAccess = documentAccessRepository
                .findByAccessDateAndDocumentIdAndUsername(dateFactory.newLocalDate(), documentId, username);

        return Objects.requireNonNullElseGet(documentAccess,
                () -> documentAccessRepository.save(
                        DocumentAccess.builder()
                                .accessDate(dateFactory.newLocalDate())
                                .documentId(documentId)
                                .username(username)
                                .build()
                ));
    }
}
