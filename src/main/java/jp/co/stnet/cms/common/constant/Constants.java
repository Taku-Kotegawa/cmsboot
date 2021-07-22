package jp.co.stnet.cms.common.constant;

/**
 * 共通定数
 */
public final class Constants {

    /**
     * リダイレクト
     */
    public static final String REDIRECT = "redirect:";

    /**
     * 操作
     */
    public static final class OPERATION {
        public static final String CREATE = "create";
        public static final String UPDATE = "update";
        public static final String DELETE = "delete";
        public static final String INVALID = "invalid";
        public static final String VALID = "valid";
        public static final String SAVE = "save";
        public static final String SAVE_DRAFT = "saveDraft";
        public static final String CANCEL_DRAFT = "cancelDraft";
        public static final String LIST = "list";
        public static final String VIEW = "view";
        public static final String UNLOCK = "unlock";
        public static final String DOWNLOAD = "download";
        public static final String UPLOAD = "upload";
        public static final String COPY = "copy";
        public static final String BULK_DELETE = "bulkDelete";
        public static final String BULK_INVALID = "bulkInvalid";
        public static final String BULK_VALID = "bulkValid";
        public static final String SET_APIKEY = "setApiKey";
        public static final String UNSET_APIKEY = "unsetApiKey";
        public static final String GENERATE_APIKEY = "generateApiKey";
        public static final String DELETE_APIKEY = "deleteApiKey";
        public static final String SAVE_APIKEY = "saveApiKey";
    }

    /**
     * ボタン
     */
    public static final class BUTTON {
        public static final String GOTOLIST = "gotoList";
        public static final String GOTOUPDATE = "gotoUpdate";
        public static final String CREATE = "create";
        public static final String SAVE_DRAFT = "saveDraft";
        public static final String CANCEL_DRAFT = "cancelDraft";
        public static final String SAVE = "save";
        public static final String INVALID = "invalid";
        public static final String VALID = "valid";
        public static final String DELETE = "delete";
        public static final String VIEW = "view";
        public static final String UNLOCK = "unlock";
        public static final String DOWNLOAD = "download";
        public static final String UPLOAD = "upload";
        public static final String CONFIRM = "confirm";
        public static final String VALIDATE = "validate";
        public static final String GOHOME = "goHome";
        public static final String COPY = "copy";
        public static final String BULK_DELETE = "bulkDelete";
        public static final String BULK_INVALID = "bulkInvalid";
        public static final String BULK_VALID = "bulkValid";
        public static final String SET_APIKEY = "setApiKey";
        public static final String UNSET_APIKEY = "unsetApiKey";
        public static final String GENERATE_APIKEY = "generateApiKey";
        public static final String DELETE_APIKEY = "deleteApiKey";
        public static final String SAVE_APIKEY = "saveApiKey";
        public static final String SWITCH_USER = "switchUser";
        public static final String ADD_ITEM = "addItem";
    }

    /**
     * CSV関連
     */
    public static final class CSV {
        public static final Integer MAX_LENGTH = 99999;
    }

    /**
     * Excel関連
     */
    public static final class EXCEL {
        public static final Integer MAX_LENGTH = 9999;
    }

    /**
     * メッセージ(プロパティファイルに記載できないもの)
     */
    public static final class MSG {
        public static final String VALIDATION_ERROR_STOP = "入力チェックでエラーを検出したため、処理を中断しました。";
        public static final String DB_ACCESS_ERROR_STOP = "データベース更新時にエラーが発生したため、処理を中断しました。";
    }

    /**
     * JOB_ID
     */
    public static final class JOBID {
        public static final String IMPORT_VARIABLE = "importVariable";
        public static final String IMPORT_ACCOUNT = "importAccount";
        public static final String IMPORT_DOCUMENT = "importDocument";

    }


}
