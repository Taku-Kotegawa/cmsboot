package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.sales.application.repository.document.DocumentAccessRepository;
import jp.co.stnet.cms.sales.domain.model.document.DocumentAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@Service
public class DocumentAccessServiceImpl implements DocumentAccessService {

    @Autowired
    DocumentAccessRepository documentAccessRepository;

    @Autowired
    CustomDateFactory dateFactory;

    @Override
    public DocumentAccess save(Long documentId, String username) {

        LocalDate today = dateFactory.newLocalDate();

        DocumentAccess documentAccess = documentAccessRepository
                .findByAccessDateAndDocumentIdAndUsername(today, documentId, username);

        if (documentAccess != null) {
            documentAccess.setLastModifiedDate(dateFactory.newLocalDateTime());
            return documentAccessRepository.saveAndFlush(documentAccess);
        } else {
            return documentAccessRepository.saveAndFlush(
                    DocumentAccess.builder()
                            .accessDate(today)
                            .documentId(documentId)
                            .username(username)
                            .build());
        }
    }
}
