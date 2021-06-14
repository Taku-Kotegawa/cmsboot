package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.util.CsvUtils;
import lombok.extern.slf4j.Slf4j;
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

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.DOWNLOAD_FILENAME;

@Slf4j
@Controller
@RequestMapping(BASE_PATH)
public class AdminAccountDownloadController {

    @Autowired
    AccountService accountService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Autowired
    Mapper beanMapper;


    /**
     * CSVファイルダウンロード
     */
    @GetMapping(value = "/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        accountService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

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

        accountService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

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

        List<AccountCsvDlBean> csvList = new ArrayList<>();
        List<Account> list = new ArrayList<>();

        Page<Account> page = accountService.findPageByInput(input);
        list.addAll(page.getContent());

        for (Account account : list) {
            AccountCsvDlBean row = beanMapper.map(account, AccountCsvDlBean.class);
            row.setStatusLabel(Status.getByValue(account.getStatus()).getCodeLabel());
            csvList.add(row);
        }

        model.addAttribute("exportCsvData", csvList);
        model.addAttribute("class", AccountCsvDlBean.class);
    }

    /**
     * ファイルダウンロード
     */
    @GetMapping("{uuid}/download")
    public String download(
            Model model,
            @PathVariable("uuid") String uuid,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        accountService.hasAuthority(Constants.OPERATION.UPDATE, loggedInUser);

        FileManaged fileManaged = fileManagedSharedService.findByUuid(uuid);
        model.addAttribute(fileManaged);
        return "fileManagedDownloadView";
    }

}
