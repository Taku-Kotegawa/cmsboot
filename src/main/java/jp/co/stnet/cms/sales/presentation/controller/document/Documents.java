package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.sales.domain.model.document.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.terasoluna.gfw.common.codelist.CodeList;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

@Component
public class Documents {

    private static final String CSV_DELIMITER = ",";
    private static final String BRAKE_LINE = "<br>";

    @Autowired
    @Named("CL_DOC_STAGE")
    CodeList useStageCodeList;

    @Autowired
    @Named("CL_ACCOUNT_FULLNAME")
    CodeList accountFullNameCodeList;

    @Autowired
    Mapper beanMapper;

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
            documentListBean.setDT_RowId(Objects.requireNonNull(document.getId()).toString());

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

            // ファイルメモ
            documentListBean.setFileMemo(getMemo(document.getFiles(), BRAKE_LINE));

            // 顧客公開区分のラベル
            documentListBean.setCustomerPublicLabel(getCustomerPublicLabel(document.getCustomerPublic()));

            // 最終更新者の氏名
            documentListBean.setLastModifiedByLabel(getLastModifiedByLabel(document.getLastModifiedBy()));

            // 不要な情報をクリア
            documentListBean.setFiles(new ArrayList<>());

            list.add(documentListBean);
        }

        return list;
    }

    /**
     * CSVダウンロード用のリストを取得
     *
     * @param documents 元ネタ
     * @return ダウンロードするデータのリスト
     */
    public List<DocumentCsvBean> getDocumentCsvDlBean(List<Document> documents) {
        List<DocumentCsvBean> list = new ArrayList<>();

        for (Document document : documents) {
            DocumentCsvBean documentCsvBean = beanMapper.map(document, DocumentCsvBean.class);

            // ステータスラベル
            documentCsvBean.setStatusLabel(getStatusLabel(document.getStatus()));

            // 区分
            if (document.getDocCategoryVariable2() != null) {

                documentCsvBean.setDocCategoryValue1(document.getDocCategoryVariable1().getValue1());
                documentCsvBean.setDocCategoryValue2(document.getDocCategoryVariable2().getValue1());
            }

            // サービス
            if (document.getDocServiceVariable1() != null) {
                documentCsvBean.setDocServiceValue1(document.getDocServiceVariable1().getValue1());
            }

            if (document.getDocServiceVariable2() != null) {
                documentCsvBean.setDocServiceValue2(document.getDocServiceVariable2().getValue1());
            }

            if (document.getDocServiceVariable3() != null) {
                documentCsvBean.setDocServiceValue3(document.getDocServiceVariable3().getValue1());
            }

            // 活用シーン
            documentCsvBean.setUseStageLabel(getUseStageLabel(document.getUseStage(), CSV_DELIMITER));

            // ファイル名のリスト
            documentCsvBean.setFilesLabel(getFilesLabel(document.getFiles(), CSV_DELIMITER));

            // ファイル名(PDF)のリスト
            documentCsvBean.setPdfFilesLabel(getPdfFilesLabel(document.getFiles(), CSV_DELIMITER));

            // 公開区分のラベル
            documentCsvBean.setPublicScopeLabel(getPublicScopeLabel(document.getPublicScope()));

            // ファイルメモ
            documentCsvBean.setFileMemo(getMemo(document.getFiles(), CSV_DELIMITER));

            // 顧客公開区分のラベル
            documentCsvBean.setCustomerPublicLabel(getCustomerPublicLabel(document.getCustomerPublic()));

            // 最終更新者(氏名)
            documentCsvBean.setLastModifiedByLabel(getLastModifiedByLabel(document.getLastModifiedBy()));

            list.add(documentCsvBean);
        }

        return list;
    }

    /**
     * ドキュメント検索一覧 - DataTables用のリストを取得
     *
     * @return DocumentListBeanのリスト
     */
    public List<DocumentListBean> getDocumentListBeansFromDocumentIndex(List<DocumentIndex> documentIndexes) {
        List<DocumentListBean> list = new ArrayList<>();

        for (DocumentIndex documentIndex : documentIndexes) {
            DocumentListBean documentListBean = beanMapper.map(documentIndex, DocumentListBean.class);

            // id
            documentListBean.setDT_RowId(documentIndex.getPk().getId().toString());
            documentListBean.setId(documentIndex.getPk().getId());

            // ボタン
            documentListBean.setOperations(getToggleButton(documentIndex.getPk().getId().toString()));

            // タイトル(リンク)
            documentListBean.setTitle(getTitleLink(documentIndex.getPk().getId(), documentIndex.getTitle()));

            // ステータスラベル
            documentListBean.setStatusLabel(getStatusLabel(documentIndex.getStatus()));

            // 活用シーン
            documentListBean.setUseStageLabel(getUseStageLabel(documentIndex.getUseStage(), CSV_DELIMITER));

            // ファイル名
            if (documentIndex.getFileManaged() != null) {
                documentListBean.setFilesLabel(getFileDownloadLink(documentIndex));
            }

            // ファイル名(PDF)
            if (documentIndex.getPdfManaged() != null) {
                documentListBean.setPdfFilesLabel(Objects.requireNonNull(documentIndex.getPdfManaged().getOriginalFilename()));
            }

            // 公開区分のラベル
            documentListBean.setPublicScopeLabel(getPublicScopeLabel(documentIndex.getPublicScope()));

            // ファイルメモ
            documentListBean.setFileMemo(documentIndex.getFileMemo());

            // 顧客公開区分のラベル
            documentListBean.setCustomerPublicLabel(getCustomerPublicLabel(documentIndex.getCustomerPublic()));

            // 最終更新者の氏名
            documentListBean.setLastModifiedByLabel(getLastModifiedByLabel(documentIndex.getLastModifiedBy()));

            // 不要な情報をクリア
            documentListBean.setFiles(new ArrayList<>());

            list.add(documentListBean);
        }

        return list;
    }

    protected String getFileDownloadLink(DocumentIndex documentIndex) {
       OperationsUtil op = new OperationsUtil(BASE_PATH);
       return "<a href=\"" + op.getViewUrl(documentIndex.getPk().getId().toString()) + "/file/files_file/" + documentIndex.getPk().getNo() + "\">" + documentIndex.getFileManaged().getOriginalFilename() + "</a>";
    }

    protected String getTitleLink(Long id, String value) {
        OperationsUtil op = new OperationsUtil(BASE_PATH);
        return "<a href=\"" + op.getViewUrl(id.toString()) + "\">" + value + "</a>";
    }


    /**
     * 公開区分を取得する
     *
     * @param value 公開区分のコード
     * @return 公開区分のラベル
     */
    protected String getPublicScopeLabel(String value) {
        if (value != null && DocPublicScope.getByValue(value) != null) {
            return Objects.requireNonNull(DocPublicScope.getByValue(value)).getCodeLabel();
        }
        return "";
    }

    /**
     * ファイルメモのリストを取得する
     *
     * @param files     Fileのリスト
     * @param delimiter 区切り文字
     * @return ファイルメモ
     */
    protected String getMemo(List<File> files, String delimiter) {
        List<String> memo = new ArrayList<>();
        for (File file : files) {
            if (file.getFileMemo() != null) {
                memo.add(escapeHtml4(file.getFileMemo()));
            }
        }
        return String.join(delimiter, memo);
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
            return Objects.requireNonNull(Status.getByValue(value)).getCodeLabel();
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
            return Objects.requireNonNull(CustomerPublic.getByValue(value)).getCodeLabel();
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
        var op = new OperationsUtil(null);
        return "<a href=\"" +
                op.getEditUrl(id) +
                "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" +
                op.getLABEL_EDIT() +
                "</a>";
    }

}
