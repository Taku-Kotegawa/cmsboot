package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeRevService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.sales.application.repository.document.DocumentIndexRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRevisionRepository;
import jp.co.stnet.cms.sales.domain.model.document.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

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
        removeNullFile(entity.getFiles());
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
        removeNullFile(entity.getFiles());
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

        if (documentIndexRepository.existsById(id)) {
            documentIndexRepository.deleteById(id);
        }

    }

    @Override
    public boolean equalsEntity(Document entity, Document currentCopy) {
        return false;
    }

    /**
     * フィールドがnullのFileを除去
     * @param files Fileのリスト
     * @return Fileのリスト
     */
    private List<File> removeNullFile(List<File> files) {
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (StringUtils.isAllBlank(file.getType(), file.getFileUuid(), file.getPdfUuid())) {
                iterator.remove();
            }
        }
        return files;
    }

}
