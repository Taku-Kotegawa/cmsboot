package jp.co.stnet.cms.sales.domain.model.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.util.StringUtils;
import jp.co.stnet.cms.sales.presentation.controller.document.DocumentCsvDlBean;
import jp.co.stnet.cms.sales.presentation.controller.document.DocumentListBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.terasoluna.gfw.common.codelist.CodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class Documents {

    private List<Document> documents;

    private CodeList useStageCodeList;

    private CodeList docTypeCodeList;

    private Mapper beanMapper;

    /**
     * DataTables用のリストを取得
     *
     * @return DocumentListBeanのリスト
     */
    public List<DocumentListBean> getDocumentListBeans() {
        List<DocumentListBean> list = new ArrayList<>();

        for (Document document : documents) {
            DocumentListBean documentListBean = beanMapper.map(document, DocumentListBean.class);

            // id
            documentListBean.setDT_RowId(document.getId().toString());

            // ボタン
            documentListBean.setOperations(getToggleButton(document.getId().toString()));

            // ステータスラベル
            documentListBean.setStatusLabel(getStatusLabel(document.getStatus()));

            // 活用シーン
            documentListBean.setUseStageLabel(getUseStageLabel(document.getUseStage(), ", "));

            // ファイル名のリスト
            documentListBean.setFilesLabel(getFilesLabel(document.getFiles(), "<br>"));

            // ファイル名(PDF)のリスト
            documentListBean.setPdfFilesLabel(getPdfFilesLabel(document.getFiles(), "<br>"));

            // 公開区分のラベル
            documentListBean.setPublicScopeLabel(getPublicScopeLabel(document.getPublicScope()));

            // 文書の種類
            documentListBean.setFileTypeLabel(getFileTypeLabel(document.getFiles(), "<br>"));

            // 顧客公開区分のラベル
            documentListBean.setCustomerPublicLabel(getCustomerPublicLabel(document.getCustomerPublic()));

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
    public List<DocumentCsvDlBean> getDocumentCsvDlBean() {
        List<DocumentCsvDlBean> list = new ArrayList<>();

        for (Document document : documents) {
            DocumentCsvDlBean documentCsvDlBean = beanMapper.map(document, DocumentCsvDlBean.class);

            // ステータスラベル
            documentCsvDlBean.setStatusLabel(getStatusLabel(document.getStatus()));

            // 活用シーン
//            documentCsvDlBean.setUseStageLabel(getUseStageLabel(document.getUseStage()));

            // ファイル名のリスト
//            documentCsvDlBean.setFilesLabel(getFilesLabel(document.getFiles()));

            // 不要な情報をクリア
//            documentCsvDlBean.setFiles(new ArrayList<>());

            list.add(documentCsvDlBean);
        }


        return null;
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
