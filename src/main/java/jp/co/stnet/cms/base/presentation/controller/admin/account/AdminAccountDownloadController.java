package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.presentation.controller.admin.variable.VariableCsvBean;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.DataTablesInputDraft;
import jp.co.stnet.cms.common.util.CsvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("admin/account")
public class AdminAccountDownloadController {

    @Autowired
    AccountService accountService;

    @Autowired
    Mapper beanMapper;

    // CSV/Excelのファイル名(拡張子除く)
    private final String downloadFilename = "account";

    @GetMapping(value = "/list/csv")
    public String listCsv(@Validated DataTablesInputDraft input, Model model) {
        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getCsvDefault());
        model.addAttribute("csvFileName", downloadFilename + ".csv");
        return "csvDownloadView";
    }

    @GetMapping(value = "/list/tsv")
    public String listTsv(@Validated DataTablesInputDraft input, Model model) {
        setModelForCsv(input, model);
        model.addAttribute("csvConfig", CsvUtils.getTsvDefault());
        model.addAttribute("csvFileName", downloadFilename + ".tsv");
        return "csvDownloadView";
    }

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

}
