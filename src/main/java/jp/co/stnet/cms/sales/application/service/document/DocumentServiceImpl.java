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
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

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

    @PersistenceContext
    EntityManager entityManager;


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
    public Document save(Document document) {
        removeNullFile(document.getFiles());
        Document saved = super.save(document);

        if (saved.getFiles() != null) {
            for (File file : saved.getFiles()) {
                fileManagedSharedService.permanent(file.getFileUuid());
                fileManagedSharedService.permanent(file.getPdfUuid());
            }
        }

        List<DocumentIndex> documentIndices = mapDocumentIndex(saved);
        documentIndexRepository.deleteById(saved.getId());
        documentIndexRepository.saveAll(documentIndices);

        return saved;
    }

    @Override
    public Document saveDraft(Document document) {
        removeNullFile(document.getFiles());
        Document saved = super.saveDraft(document);

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
        List<DocumentIndex> documentIndices = mapDocumentIndex(saved);
        documentIndexRepository.deleteById(saved.getId());
        documentIndexRepository.saveAll(documentIndices);
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
    public boolean equalsEntity(Document document, Document currentCopy) {
        return false;
    }

    /**
     * フィールドがnullのFileを除去
     *
     * @param files Fileのリスト
     * @return Fileのリスト
     */
    private List<File> removeNullFile(List<File> files) {
        if (files != null) {
            Iterator<File> iterator = files.iterator();
            while (iterator.hasNext()) {
                File file = iterator.next();
                if (StringUtils.isAllBlank(file.getType(), file.getFileUuid(), file.getPdfUuid())) {
                    iterator.remove();
                }
            }
            return files;
        }

        return new ArrayList<>();
    }

    @Override
    protected boolean isFilterINClause(String fieldName) {

        if ("status".equals(convertColumnName(fieldName))) {
            return true;
        }
        else if ("publicScope".equals(convertColumnName(fieldName))) {
            return true;
        }
        else if ("customerPublic".equals(convertColumnName(fieldName))) {
            return true;
        }

        return super.isFilterINClause(fieldName);
    }

    /**
     * Document -> DocumentIdx にマップ
     *
     * @param document Documentエンティティ
     * @return DocumentIndexエンティティ
     */
    private List<DocumentIndex> mapDocumentIndex(Document document) {

        if (document == null) {
            throw new IllegalArgumentException("document must not be null.");
        }

        List<DocumentIndex> documentIndices = new ArrayList<>();

        for (int i = 0; i < document.getFiles().size(); i++) {
            File file = document.getFiles().get(i);
            DocumentIndex documentIndex = beanMapper.map(file, DocumentIndex.class);
            beanMapper.map(document, documentIndex);
            documentIndex.setNo(i);

            documentIndex.setContent(getContent(documentIndex.getFileUuid()));

            documentIndices.add(documentIndex);
        }

        return documentIndices;
    }


    /**
     * uuidに基づくファイルの中身を取得。既に取得済みの場合は以前取得した値を再利用。
     * @param uuid 中身を取得したいファイルUuid
     * @return ファイルの中身
     */
    private String getContent(String uuid) {

        if (uuid == null) {
            return null;
        }

        DocumentIndex before = documentIndexRepository.findByFileUuid(uuid).orElse(null);
        if (before != null && StringUtils.isNotBlank(before.getContent())) {
            return before.getContent();
        }


        try {
            String content = fileManagedSharedService.getContent(uuid);

            // 連続する空白文字を除去
            content = content.replaceAll("[ |　|\t|\\n|\\r\\n|\\r]+", " ");

            return content;

        } catch (IOException | TikaException e) {
            e.printStackTrace();

        }

        return null;
    }

}
