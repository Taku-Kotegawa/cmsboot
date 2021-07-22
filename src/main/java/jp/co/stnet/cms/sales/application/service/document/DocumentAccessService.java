package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.sales.domain.model.document.DocumentAccess;

public interface DocumentAccessService {

    /**
     * ドキュメント参照画面へのアクセスを記録
     *
     * @param documentId ドキュメントのID
     * @param username   ユーザ名
     * @return 保存したエンティティ
     */
    DocumentAccess save(Long documentId, String username);

}
