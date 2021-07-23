package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.application.repository.NodeRevRepository;
import jp.co.stnet.cms.base.application.service.AbstractNodeRevService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedService;
import jp.co.stnet.cms.sales.application.repository.document.DocumentIndexRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRevisionRepository;
import jp.co.stnet.cms.sales.domain.model.document.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    FileManagedService fileManagedService;

    protected DocumentServiceImpl() {
        super(Document.class, DocumentRevision.class, DocumentMaxRev.class);
    }

    @Override
    protected String orderByFieldName(String fieldName) {

        if ("docCategoryVariable1.value1".equals(fieldName)) {
            return "docCategoryVariable1.code";
        } else if ("docCategoryVariable2.value1".equals(fieldName)) {
            return "docCategoryVariable2.code";
        } else if ("docServiceVariable1.value1".equals(fieldName)) {
            return "docServiceVariable1.code";
        } else if ("docServiceVariable2.value1".equals(fieldName)) {
            return "docServiceVariable2.code";
        } else if ("docServiceVariable3.value1".equals(fieldName)) {
            return "docServiceVariable3.code";
        }

        return super.orderByFieldName(fieldName);
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
    public Document save(Document document) {
        removeNullFile(document.getFiles());
        Document saved = super.save(document);

        if (saved.getFiles() != null) {
            for (File file : saved.getFiles()) {
                fileManagedService.permanent(file.getFileUuid());
                fileManagedService.permanent(file.getPdfUuid());
            }
        }

        List<DocumentIndex> documentIndices = mapDocumentIndex(saved);
        documentIndexRepository.deleteByPkId(saved.getId());
        documentIndexRepository.saveAll(documentIndices);

        return saved;
    }

    @Override
    public Document saveDraft(Document document) {
        removeNullFile(document.getFiles());
        Document saved = super.saveDraft(document);

        if (saved.getFiles() != null) {
            for (File file : saved.getFiles()) {
                fileManagedService.permanent(file.getFileUuid());
                fileManagedService.permanent(file.getPdfUuid());
            }
        }

        return saved;
    }

    @Override
    public Document invalid(Long id) {
        Document saved = super.invalid(id);
        documentIndexRepository.deleteByPkId(id);
        return saved;
    }

    @Override
    public Document valid(Long id) {
        Document saved = super.valid(id);
        List<DocumentIndex> documentIndices = mapDocumentIndex(saved);
        documentIndexRepository.deleteByPkId(saved.getId());
        documentIndexRepository.saveAll(documentIndices);
        return saved;
    }

    @Override
    public void delete(Long id) {
        Document document = documentRepository.findById(id).orElse(null);
        if (document != null && document.getFiles() != null) {
            for (File file : document.getFiles()) {
                fileManagedService.permanent(file.getFileUuid());
                fileManagedService.permanent(file.getPdfUuid());
            }
        }

        super.delete(id);
        documentIndexRepository.deleteByPkId(id);
    }

    @Override
    public boolean equalsEntity(Document document, Document currentCopy) {
        return false;
    }

    /**
     * フィールドがnullのFileを除去
     *
     * @param files Fileのリスト
     */
    private void removeNullFile(List<File> files) {
        if (files != null) {
            files.removeIf(file -> StringUtils.isAllBlank(file.getType(), file.getFileUuid(), file.getPdfUuid()));
        }
    }

    @Override
    protected boolean isFilterINClause(String fieldName) {
        if ("status".equals(convertColumnName(fieldName))) {
            return true;
        } else if ("publicScope".equals(convertColumnName(fieldName))) {
            return true;
        } else if ("customerPublic".equals(convertColumnName(fieldName))) {
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

        final int NO_CASE_NOFILE = 9999;

        if (document == null) {
            throw new IllegalArgumentException("document must not be null.");
        }

        List<DocumentIndex> documentIndices = new ArrayList<>();


        if (document.getFiles().isEmpty()) {
            DocumentIndex documentIndex = beanMapper.map(document, DocumentIndex.class);
            documentIndex.setBodyPlain(getBodyPlane(document.getBody()));
            documentIndex.setPk(new DocumentIndexPK(document.getId(), NO_CASE_NOFILE));
            documentIndices.add(documentIndex);
        } else {
            for (int i = 0; i < document.getFiles().size(); i++) {
                File file = document.getFiles().get(i);
                DocumentIndex documentIndex = beanMapper.map(document, DocumentIndex.class);
                documentIndex.setBodyPlain(getBodyPlane(document.getBody()));
                beanMapper.map(file, documentIndex);
                documentIndex.setPk(new DocumentIndexPK(document.getId(), i));
                documentIndex.setContent(getContent(documentIndex.getFileUuid()));
                documentIndices.add(documentIndex);
            }
        }

        return documentIndices;
    }

    /**
     * HTMLをテキストに変換
     *
     * @param html HTML
     * @return テキスト
     */
    private String getBodyPlane(String html) {
        if (html != null) {
            return Jsoup.parse(html).text();
        }
        return null;
    }

    /**
     * uuidに基づくファイルの中身を取得。既に取得済みの場合は以前取得した値を再利用。
     *
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
            return fileManagedService.escapeContent(fileManagedService.getContent(uuid));

        } catch (IOException | TikaException e) {
            e.printStackTrace();

        }

        return null;
    }

}
