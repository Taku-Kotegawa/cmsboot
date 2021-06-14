package jp.co.stnet.cms.base.presentation.controller.admin.account;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.application.service.authentication.AccountSharedService;
import jp.co.stnet.cms.base.application.service.message.MailSendService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.BASE_PATH;
import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.TEMPLATE_LIST;

@Controller
@RequestMapping(BASE_PATH)
public class AdminAccountListController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    MailSendService mailSendService;

    @Autowired
    Mapper beanMapper;

    /**
     * 一覧画面の表示
     */
    @GetMapping(value = "list")
    public String list(Model model) {
        return TEMPLATE_LIST;
    }

    /**
     * DataTables用のJSONの作成
     *
     * @param input DataTablesから要求
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "/list/json")
    public DataTablesOutput<AccountListBean> listJson(@Validated DataTablesInput input) {

        OperationsUtil op = new OperationsUtil(null);

        List<AccountListBean> list = new ArrayList<>();
        Page<Account> accountPage = accountService.findPageByInput(input);

        // Welcomeメールの最新の送信履歴を取得
        List<MailSendHistory> mailSendHistories = mailSendService.getNewest();

        for (Account account : accountPage.getContent()) {
            AccountListBean accountListBean = beanMapper.map(account, AccountListBean.class);
            accountListBean.setOperations(getToggleButton(account.getUsername(), op));
            accountListBean.setDT_RowId(account.getUsername());

            // ステータスラベル
            String statusLabel = account.getStatus().equals(Status.VALID.getCodeValue()) ? Status.VALID.getCodeLabel() : Status.INVALID.getCodeLabel();
            if (accountSharedService.isLocked(account.getUsername())) statusLabel = statusLabel + "(ロック)";
            accountListBean.setStatusLabel(statusLabel);

            accountListBean.setWelcomeMailSendDate(getWelcomeMailSendDate(mailSendHistories, accountListBean.getUsername()));

            list.add(accountListBean);
        }

        DataTablesOutput<AccountListBean> output = new DataTablesOutput<>();
        output.setData(list);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(accountPage.getTotalElements());

        return output;
    }

    /**
     * 案内メール最終送信日時の取得
     * @param mailSendHistories
     * @param username
     * @return
     */
    private LocalDateTime getWelcomeMailSendDate(List<MailSendHistory> mailSendHistories, String username) {
        for (MailSendHistory mailSendHistory : mailSendHistories) {
            if (username.equals(mailSendHistory.getReceiver().getUsername())) {
                return mailSendHistory.getSendTime();
            }
        }
        return null;
    }

    /**
     * 一覧画面のボタンHTMLの準備
     * @param id
     * @param op
     * @return
     */
    private String getToggleButton(String id, OperationsUtil op) {

        StringBuffer link = new StringBuffer();
        link.append("<div class=\"btn-group\">");
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
        link.append("<button type=\"button\" class=\"btn btn-button btn-sm dropdown-toggle dropdown-toggle-split\"data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">");
        link.append("</button>");
        link.append("<ul class=\"dropdown-menu\">");
        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getViewUrl(id) + "\">" + op.getLABEL_VIEW() + "</a></li>");
        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getCopyUrl(id) + "\">" + op.getLABEL_COPY() + "</a></li>");
        link.append("<li><a class=\"dropdown-item\" href=\"" + op.getInvalidUrl(id) + "\">" + op.getLABEL_INVALID() + "</a></li>");
        link.append("</ul>");
        link.append("</div>");

        return link.toString();
    }
}
