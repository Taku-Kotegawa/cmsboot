package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;


import static jp.co.stnet.cms.common.constant.Constants.*;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.util.CsvUtils;
import jp.co.stnet.cms.sales.application.service.document.DocumentService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import jp.co.stnet.cms.sales.domain.model.document.DocumentCsvBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.DOWNLOAD_FILENAME;


@Controller
@RequestMapping(BASE_PATH)
public class DocumentDownloadController {

    @Autowired
    DocumentService documentService;

    @Autowired
    Mapper beanMapper;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    Documents documents;



    /**
     * CSVファイルダウンロード
     */
    @GetMapping(value = "/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

//        documentService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        setModelForCsv(input, model);

        model.addAttribute("csvConfig", CsvUtils.getCsvDefault());
        model.addAttribute("csvFileName", DOWNLOAD_FILENAME + ".csv");
        return "csvDownloadView";
    }

    /**
     * TSVファイルダウンロード
     */
    @GetMapping(value = "/list/tsv")
    public String listTsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        documentService.hasAuthority(OPERATION.UPDATE, loggedInUser);

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
    @GetMapping("{id}/download/{no}")
    public String download(
            Model model,
            @PathVariable("id") Long id,
            @PathVariable("no") Integer no,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        documentService.hasAuthority(OPERATION.UPDATE, loggedInUser);

        Document document = documentService.findById(id);
        if (!document.getFiles().isEmpty()) {
            String uuid = document.getFiles().get(no).getFileUuid();
            if (uuid != null) {
                model.addAttribute(fileManagedSharedService.findByUuid(uuid));
            }
        }

        return "fileManagedDownloadView";
    }

}
