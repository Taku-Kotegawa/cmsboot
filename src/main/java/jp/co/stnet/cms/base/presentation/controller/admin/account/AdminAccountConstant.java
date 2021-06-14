package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.common.constant.Constants;

public class AdminAccountConstant {

    static final String BASE_PATH = "admin/account";
    static final String TEMPLATE_LIST = BASE_PATH + "/list";
    static final String TEMPLATE_ACTIVE_LIST = BASE_PATH + "/activeList";
    static final String TEMPLATE_FORM = BASE_PATH + "/form";
    static final String TEMPLATE_UPLOAD_FORM = BASE_PATH + "/uploadform";
    static final String TEMPLATE_UPLOAD_COMPLETE = "common/upload/complete";

    // CSV/TSVのファイル名(拡張子除く)
    static final String DOWNLOAD_FILENAME = "account";

    // アップロード用のインポートジョブID
    static final String UPLOAD_JOB_ID = Constants.JOBID.IMPORT_ACCOUNT;

}
