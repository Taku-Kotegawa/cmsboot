package jp.co.stnet.cms.sales.domain.model.document;


import org.hibernate.search.mapper.pojo.bridge.IdentifierBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeFromDocumentIdentifierContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.IdentifierBridgeToDocumentIdentifierContext;

public class DocumentIndexPKBridge implements IdentifierBridge<DocumentIndexPK> {
    @Override
    public String toDocumentIdentifier(DocumentIndexPK propertyValue, IdentifierBridgeToDocumentIdentifierContext context) {
        if (propertyValue == null) {
            return "";
        }
        return propertyValue.getId() + "/" + propertyValue.getNo();
    }

    @Override
    public DocumentIndexPK fromDocumentIdentifier(String documentIdentifier, IdentifierBridgeFromDocumentIdentifierContext context) {
        String[] split = documentIdentifier.split("/");
        return new DocumentIndexPK(Long.parseLong(split[0]), Integer.parseInt(split[1]));
    }
}
