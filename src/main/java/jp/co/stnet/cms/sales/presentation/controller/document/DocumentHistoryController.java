package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.datatables.DataTablesInputHistory;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.sales.application.service.document.DocumentHistoryService;
import jp.co.stnet.cms.sales.domain.model.document.DocumentHistoryBean;
import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;


@Controller
@RequestMapping(BASE_PATH)
public class DocumentHistoryController {

    @Autowired
    Mapper beanMapper;

    @Autowired
    DocumentHistoryService documentHistoryService;

    @Autowired
    DocumentHelper helper;

    /**
     * 一覧画面の表示
     * DataTablesのAjaxが表示後に起動
     *
     * @param model モデル
     * @return Viewのパス
     */
    @GetMapping(value = "history/{id}")
    public String list(Model model, @PathVariable("id") Long id) {
        model.addAttribute("id", id);
        return BASE_PATH + "/history";
    }

    /**
     * DataTables用のJSONの作成
     * チェックボックスの有無によって、ドキュメントリビジョンDBから以下の項目を取得し、必要な項目を変換する
     * 検索時にはドキュメントID、チェックボックスの有無、公開区分のデータを用いる
     * 取得項目: リビジョンID、Ver、変更理由、最終更新者ID、最終更新日
     * Ver: 対象ドキュメントのリンクに変換する
     * 最終更新者: 最終更新IDから変換する
     * 取得したデータ、変換したデータをJSON形式にして返す
     *
     * @param input        DataTablesから要求
     * @param loggedInUser ユーザ情報
     * @param id           ドキュメントID
     * @return JSON
     */
    @ResponseBody
    @GetMapping(value = "/history/json/{id}")
    public DataTablesOutput<DocumentHistoryBean> listJson(DataTablesInputHistory input,
                                                          @AuthenticationPrincipal LoggedInUser loggedInUser,
                                                          @PathVariable("id") Long id) {

        List<DocumentHistoryBean> list = new ArrayList<>();

        OperationsUtil op = new OperationsUtil(BASE_PATH);

        //Helperから返ってくる公開区分をセットする
        Set<String> publicScope = helper.getPublicScopeSet(loggedInUser);

        Page<DocumentRevision> documentRevisionLPages;

        //全件表示のチェックボックスの有無によって呼び出すメソッドを変更
        if (input.getHistory()) {
            documentRevisionLPages = documentHistoryService.search(id, false, publicScope);
        } else {
            documentRevisionLPages = documentHistoryService.search(id, true, publicScope);
        }

        //BeanにdocumentRevisionListと不足しているデータ(リンク、最終更新者名)を格納する
        for (DocumentRevision documentRevision : documentRevisionLPages.getContent()) {

            //documentRevisionの値を格納
            DocumentHistoryBean documentHistoryBean = beanMapper.map(documentRevision, DocumentHistoryBean.class);

            //最終更新者IDを最終更新者名に変換したものを格納
            documentHistoryBean.setLastModifiedByLabel(helper.getUserName(loggedInUser.getAccount().getId()));

            //Ver項目をリンクにしたものを格納
            documentHistoryBean.setVerLabel("<a href=\"" + op.getViewUrl(id.toString()) + "?version=" + documentRevision.getVersion() + "\" class=\"\" style=\"white-space: nowrap\">" + documentRevision.getVersion().toString() + "</a>");

            //リストに必要項目を追加する
            list.add(documentHistoryBean);
        }

        //JSONを返す
        DataTablesOutput<DocumentHistoryBean> output = new DataTablesOutput<>();
        output.setData(list);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(documentRevisionLPages.getTotalElements());

        return output;
    }

}
