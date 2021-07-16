package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.util.CsvUtils;
import jp.co.stnet.cms.sales.application.service.document.DocumentAccessService;
import jp.co.stnet.cms.sales.application.service.document.DocumentService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import jp.co.stnet.cms.sales.domain.model.document.DocumentCsvBean;
import jp.co.stnet.cms.sales.domain.model.document.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import static jp.co.stnet.cms.common.constant.Constants.CSV;
import static jp.co.stnet.cms.common.constant.Constants.OPERATION;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.DOWNLOAD_FILENAME;


@Controller
@RequestMapping(BASE_PATH)
public class DocumentDownloadController {

    @Autowired
    DocumentAuthority authority;

    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentAccessService documentAccessService;

    @Autowired
    Documents documents;


    /**
     * CSVファイルダウンロード
     */
    @GetMapping("/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(OPERATION.LIST, loggedInUser);

        setModelForCsv(input, model);

        model.addAttribute("csvConfig", CsvUtils.getCsvDefault());
        model.addAttribute("csvFileName", DOWNLOAD_FILENAME + ".csv");
        return "csvDownloadView";
    }

    /**
     * TSVファイルダウンロード
     */
    @GetMapping("/list/tsv")
    public String listTsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(OPERATION.LIST, loggedInUser);

        setModelForCsv(input, model);

        model.addAttribute("csvConfig", CsvUtils.getTsvDefault());
        model.addAttribute("csvFileName", DOWNLOAD_FILENAME + ".tsv");
        return "csvDownloadView";
    }

    /**
     * CSVファイル用のデータ準備(モデルにセット)
     *
     * @param input input
     * @param model model
     */
    private void setModelForCsv(DataTablesInputDraft input, Model model) {
        input.setStart(0);
        input.setLength(CSV.MAX_LENGTH);

        Page<Document> page = documentService.findPageByInput(input);

        model.addAttribute("exportCsvData", documents.getDocumentCsvDlBean(page.getContent()));
        model.addAttribute("class", DocumentCsvBean.class);
    }

    /**
     * ファイルダウンロード
     */
    @GetMapping("{id}/file/{fileType}/{no}")
    public String download(
            Model model,
            @PathVariable("id") Long id,
            @PathVariable("fileType") String fileType,
            @PathVariable("no") Integer no,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        Document document = documentService.findById(id);

        authority.hasAuthority(OPERATION.VIEW, loggedInUser, document);

        File file = document.getFiles().get(no);
        if (file != null) {
            if ("files_file".equals(fileType)) {
                if (file.getFileManaged() != null) {
                    model.addAttribute(file.getFileManaged());
                }
            } else {
                if (file.getPdfManaged() != null) {
                    model.addAttribute(file.getPdfManaged());
                }
            }
        } else {
            throw new ResourceNotFoundException("file not found.");
        }

        documentAccessService.save(id, loggedInUser.getUsername());

        return "fileManagedDownloadView";
    }

}
