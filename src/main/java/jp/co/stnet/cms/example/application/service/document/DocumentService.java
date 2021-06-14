package jp.co.stnet.cms.example.application.service.document;

import jp.co.stnet.cms.base.application.service.NodeRevIService;
import jp.co.stnet.cms.example.domain.model.document.Document;
import jp.co.stnet.cms.example.domain.model.document.DocumentRevision;


public interface DocumentService extends NodeRevIService<Document, DocumentRevision, Long> {
}
