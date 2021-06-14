package jp.co.stnet.cms.example.application.service.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeRevService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.example.application.repository.document.DocumentIndexRepository;
import jp.co.stnet.cms.example.application.repository.document.DocumentRepository;
import jp.co.stnet.cms.example.application.repository.document.DocumentRevisionRepository;
import jp.co.stnet.cms.example.domain.model.document.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentServiceImpl extends AbstractNodeRevService<Document, DocumentRevision, DocumentMaxRev, Long> implements DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentRevisionRepository documentRevisionRepository;

    @Autowired
    DocumentIndexRepository documentIndexRepository;

    @Autowired
    FileManagedSharedService fileManagedSharedService;


    protected DocumentServiceImpl() {
        super(Document.class, DocumentRevision.class, DocumentMaxRev.class);
    }

    @Override
    protected NodeRevRepository<DocumentRevision, Long> getRevisionRepository() {
        return this.documentRevisionRepository;
    }

    @Override
    protected JpaRepository<Document, Long> getRepository() {
        return this.documentRepository;
    }

    @Override
    public Boolean hasAuthority(String operation, LoggedInUser loggedInUser) {
        return true;
    }


    // FileManagedの永続化処理を追加

    @Override
    public Document save(Document entity) {
        Document saved = super.save(entity);

        if (saved.getFiles() != null) {
            for (File file : saved.getFiles()) {
                fileManagedSharedService.permanent(file.getFileUuid());
                fileManagedSharedService.permanent(file.getPdfUuid());
            }
        }

        documentIndexRepository.save(beanMapper.map(saved, DocumentIndex.class));

        return saved;
    }

    @Override
    public Document saveDraft(Document entity) {
        Document saved = super.saveDraft(entity);

        if (saved.getFiles() != null) {
            for (File file : saved.getFiles()) {
                fileManagedSharedService.permanent(file.getFileUuid());
                fileManagedSharedService.permanent(file.getPdfUuid());
            }
        }

        return saved;
    }

    @Override
    public Document invalid(Long id) {
        Document saved = super.invalid(id);
        documentIndexRepository.deleteById(id);
        return saved;
    }

    @Override
    public Document valid(Long id) {
        Document saved = super.valid(id);
        documentIndexRepository.save(beanMapper.map(saved, DocumentIndex.class));
        return saved;
    }

    @Override
    public void delete(Long id) {

        Document document = documentRepository.findById(id).orElse(null);
        if (document != null && document.getFiles() != null) {
            for (File file : document.getFiles()) {
                fileManagedSharedService.permanent(file.getFileUuid());
                fileManagedSharedService.permanent(file.getPdfUuid());
            }
        }

        super.delete(id);

        documentIndexRepository.deleteById(id);

    }

    @Override
    public boolean equalsEntity(Document entity, Document currentCopy) {
        return false;
    }
}
