package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.base.application.service.NodeRevIService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;


public interface DocumentService extends NodeRevIService<Document, DocumentRevision, Long> {
}
