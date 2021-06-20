package jp.co.stnet.cms.example.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.util.CsvUtils;
import jp.co.stnet.cms.example.application.service.document.DocumentService;
import jp.co.stnet.cms.example.domain.model.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static jp.co.stnet.cms.example.presentation.controller.document.DocumentConstant.BASE_PATH;
import static jp.co.stnet.cms.example.presentation.controller.document.DocumentConstant.DOWNLOAD_FILENAME;


@Controller
@RequestMapping(BASE_PATH)
public class DocumentDownloadController {

    @Autowired
    DocumentService documentService;

    @Autowired
    Mapper beanMapper;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    /**
     * CSVファイルダウンロード
     */
    @GetMapping(value = "/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        documentService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

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

        documentService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

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
        input.setLength(Constants.CSV.MAX_LENGTH);

        List<DocumentCsvDlBean> csvList = new ArrayList<>();
        List<Document> list = new ArrayList<>();

        Page<Document> page = documentService.findPageByInput(input);
        list.addAll(page.getContent());

        for (Document document : list) {
            DocumentCsvDlBean row = beanMapper.map(document, DocumentCsvDlBean.class);
            row.setStatusLabel(Status.getByValue(document.getStatus()).getCodeLabel());
            csvList.add(row);
        }

        model.addAttribute("exportCsvData", csvList);
        model.addAttribute("class", DocumentCsvDlBean.class);
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

        documentService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

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
