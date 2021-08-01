package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;

import java.util.Set;

public interface DocumentRevisionService {
    DocumentRevision findLatest(Long id, Set<String> publicScope);

    DocumentRevision findById(Long id);

    DocumentRevision versionSpecification(Long id, Long version, Set<String> publicScope);

    DocumentRevision findByIdAndVersion(Long id, Long version);
}
