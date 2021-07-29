package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.common.datatables.OperationsUtil;

public class DocumentOperationUtil extends OperationsUtil {

    public DocumentOperationUtil(String baseUrl) {
        super(baseUrl);
        super.setLABEL_SAVE("保存・有効化");
    }

}
