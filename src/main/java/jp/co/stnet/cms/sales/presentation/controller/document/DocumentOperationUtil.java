package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.common.datatables.OperationsUtil;

public class DocumentOperationUtil extends OperationsUtil {

    public DocumentOperationUtil(String baseUrl) {
        super(baseUrl);
        super.setLABEL_SAVE("保存・有効化");
    }

    public String getDownloadFileUrl(String id, String no, String fileType, String version) {
        if(version == null) {
            // 下書きを含む最新
            return getBaseUrl() + id + "/file/" + fileType + "/" + no;
        } else if("latest".equals(version)) {
            // 有効な最新
            return getBaseUrl() + id + "/latest/file/" + fileType + "/" + no;
        }else {
            // Version指定
            return getBaseUrl() + id + "/file/" + fileType + "/" + no + "?version=" + version;
        }
    }
}
