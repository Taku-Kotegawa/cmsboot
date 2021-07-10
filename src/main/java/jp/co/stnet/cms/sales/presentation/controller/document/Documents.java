package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.util.StringUtils;
import jp.co.stnet.cms.sales.domain.model.document.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.common.codelist.CodeList;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;

@Component
public class Documents {

    @Autowired
    AccountService accountService;

    @Autowired
    @Named("CL_DOC_STAGE")
    private CodeList useStageCodeList;

    @Autowired
    @Named("CL_DOC_TYPE")
    private CodeList docTypeCodeList;

    @Autowired
    @Named("CL_ACCOUNT_FULLNAME")
    private CodeList accountFullNameCodeList;

    @Autowired
    private Mapper beanMapper;

    private static final String CSV_DELIMITER = ",";

    private static final String BRAKE_LINE = "<br>";


    /**
     * DataTables用のリストを取得
     *
     * @return DocumentListBeanのリスト
     */
    public List<DocumentListBean> getDocumentListBeans(List<Document> documents) {
        List<DocumentListBean> list = new ArrayList<>();

        for (Document document : documents) {
            DocumentListBean documentListBean = beanMapper.map(document, DocumentListBean.class);

            // id
            documentListBean.setDT_RowId(document.getId().toString());

            // ボタン
            documentListBean.setOperations(getToggleButton(document.getId().toString()));

            // タイトル(リンク)
            documentListBean.setTitle(getTitleLink(document.getId(), document.getTitle()));

            // ステータスラベル
            documentListBean.setStatusLabel(getStatusLabel(document.getStatus()));

            // 活用シーン
            documentListBean.setUseStageLabel(getUseStageLabel(document.getUseStage(), CSV_DELIMITER));

            // ファイル名のリスト
            documentListBean.setFilesLabel(getFilesLabel(document.getFiles(), BRAKE_LINE));

            // ファイル名(PDF)のリスト
            documentListBean.setPdfFilesLabel(getPdfFilesLabel(document.getFiles(), BRAKE_LINE));

            // 公開区分のラベル
            documentListBean.setPublicScopeLabel(getPublicScopeLabel(document.getPublicScope()));

            // 文書の種類
            documentListBean.setFileTypeLabel(getFileTypeLabel(document.getFiles(), BRAKE_LINE));

            // 顧客公開区分のラベル
            documentListBean.setCustomerPublicLabel(getCustomerPublicLabel(document.getCustomerPublic()));

            // 最終更新者の氏名
            documentListBean.setLastModifiedByLabel(getLastModifiedByLabel(document.getLastModifiedBy()));

            // 不要な情報をクリア
            documentListBean.setFiles(new ArrayList<>());

            // null対策
            if (documentListBean.getDocServiceVariable() == null) {
                documentListBean.setDocServiceVariable(new Variable());
            }

            list.add(documentListBean);
        }

        return list;
    }

    /**
     * CSVダウンロード用のリストを取得
     *
     * @return
     */
    public List<DocumentCsvBean> getDocumentCsvDlBean(List<Document> documents) {
        List<DocumentCsvBean> list = new ArrayList<>();

        for (Document document : documents) {
            DocumentCsvBean documentCsvBean = beanMapper.map(document, DocumentCsvBean.class);

            // ステータスラベル
            documentCsvBean.setStatusLabel(getStatusLabel(document.getStatus()));

            // 区分
            if (document.getDocCategoryVariable() != null) {
                documentCsvBean.setDocCategory(document.getDocCategoryVariable().getCode());
                documentCsvBean.setDocCategoryValue1(document.getDocCategoryVariable().getValue1());
                documentCsvBean.setDocCategoryValue2(document.getDocCategoryVariable().getValue2());
            }

            // サービス
            if (document.getDocServiceVariable() != null) {
                documentCsvBean.setDocService(document.getDocServiceVariable().getCode());
                documentCsvBean.setDocServiceValue1(document.getDocServiceVariable().getValue1());
                documentCsvBean.setDocServiceValue2(document.getDocServiceVariable().getValue2());
                documentCsvBean.setDocServiceValue3(document.getDocServiceVariable().getValue3());
            }

            // 活用シーン
            documentCsvBean.setUseStageLabel(getUseStageLabel(document.getUseStage(), CSV_DELIMITER));

            // ファイル名のリスト
            documentCsvBean.setFilesLabel(getFilesLabel(document.getFiles(), CSV_DELIMITER));

            // ファイル名(PDF)のリスト
            documentCsvBean.setPdfFilesLabel(getPdfFilesLabel(document.getFiles(), CSV_DELIMITER));

            // 公開区分のラベル
            documentCsvBean.setPublicScopeLabel(getPublicScopeLabel(document.getPublicScope()));

            // 文書の種類
            documentCsvBean.setFileTypeLabel(getFileTypeLabel(document.getFiles(), CSV_DELIMITER));

            // 顧客公開区分のラベル
            documentCsvBean.setCustomerPublicLabel(getCustomerPublicLabel(document.getCustomerPublic()));

            // 最終更新者(氏名)
            documentCsvBean.setLastModifiedByLabel(getLastModifiedByLabel(document.getLastModifiedBy()));

            list.add(documentCsvBean);
        }

        return list;
    }

    protected String getTitleLink(Long id, String value) {
        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "<a href=\"" + op.getViewUrl(id.toString()) +  "\">" + value + "</a>";
    }


    /**
     * 公開区分を取得する
     *
     * @param value 公開区分のコード
     * @return 公開区分のラベル
     */
    protected String getPublicScopeLabel(String value) {
        if (value != null && DocPublicScope.getByValue(value) != null) {
            return DocPublicScope.getByValue(value).getCodeLabel();
        }
        return "";
    }

    /**
     * 文書の種類のリストを取得する
     *
     * @param files     Fileのリスト
     * @param delimiter 区切り文字
     * @return 文書の書類のラベル
     */
    protected String getFileTypeLabel(List<File> files, String delimiter) {
        List<String> fileTypeLabel = new ArrayList<>();
        for (File file : files) {
            if (file.getType() != null) {
                for (String v : docTypeCodeList.asMap().keySet()) {
                    if (file.getType().equals(v)) {
                        fileTypeLabel.add(docTypeCodeList.asMap().get(v));
                    }
                }
            }
        }
        return String.join(delimiter, fileTypeLabel);
    }

    /**
     * PDFファイル名のリスト
     *
     * @param files     Fileのリスト
     * @param delimiter 区切り文字
     * @return 文字列
     */
    protected String getPdfFilesLabel(List<File> files, String delimiter) {
        List<String> originalFilenames = new ArrayList<>();
        for (File file : files) {
            if (file.getPdfManaged() != null) {
                originalFilenames.add(file.getPdfManaged().getOriginalFilename());
            }
        }
        return String.join(delimiter, originalFilenames);
    }

    /**
     * ファイル名のリスト
     *
     * @param files     Fileのリスト
     * @param delimiter 区切り文字
     * @return 文字列
     */
    protected String getFilesLabel(List<File> files, String delimiter) {
        List<String> originalFilenames = new ArrayList<>();
        for (File file : files) {
            if (file.getFileManaged() != null) {
                originalFilenames.add(file.getFileManaged().getOriginalFilename());
            }
        }
        return String.join(delimiter, originalFilenames);
    }

    /**
     * 活用シーンのラベルを取得
     *
     * @param useStages 活用シーンのセット
     * @return 活用シーンのラベル
     */
    protected String getUseStageLabel(Set<String> useStages, String delimiter) {
        List<String> useStageLabels = new ArrayList<>();
        for (String v : useStageCodeList.asMap().keySet()) {
            for (String l : useStages) {
                if (l.equals(v)) {
                    useStageLabels.add(useStageCodeList.asMap().get(v));
                }
            }
        }
        return String.join(delimiter, useStageLabels);
    }

    /**
     * ステータスのラベルを取得
     *
     * @param value Statusのコード
     * @return ラベル, 一致するものがなければnull
     */
    protected String getStatusLabel(String value) {
        if (StringUtils.isNotBlank(value)) {
            return Status.getByValue(value).getCodeLabel();
        }
        return null;
    }

    /**
     * 顧客公開区分のラベルを取得
     *
     * @param value CustomerPublicのコード
     * @return ラベル, 一致するものがなければnull
     */
    protected String getCustomerPublicLabel(String value) {
        if (StringUtils.isNotBlank(value)) {
            return CustomerPublic.getByValue(value).getCodeLabel();
        }
        return null;
    }

    /**
     * 最終更新者のフルネームのを取得
     *
     * @param lastModifiedBy 最終更新者のユーザ名
     * @return 最終更新者のフルネーム(取得できない場合は空白文字列)
     */
    protected String getLastModifiedByLabel(String lastModifiedBy) {
        return accountFullNameCodeList.asMap().get(lastModifiedBy);
    }

    /**
     * 一覧画面の編集ボタンHTMLの準備
     *
     * @param id ドキュメントの内部ID
     * @return 編集ボタンを表示するHTML
     */
    protected String getToggleButton(String id) {
        OperationsUtil op = new OperationsUtil(null);
        StringBuffer link = new StringBuffer();
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
        return link.toString();
    }

}
