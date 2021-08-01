package jp.co.stnet.cms.sales.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MappingException;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.util.StateMap;
import jp.co.stnet.cms.sales.application.service.document.DocumentRevisionService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import jp.co.stnet.cms.sales.domain.model.document.DocumentRevision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.servlet.http.HttpSession;
import java.util.Set;

import static jp.co.stnet.cms.sales.presentation.controller.document.DocumentConstant.BASE_PATH;

@Controller
@RequestMapping(BASE_PATH)
public class DocumentSpecifiedController {

    @Autowired
    DocumentRevisionService documentRevisionService;

    @Autowired
    Mapper beanMapper;

    @Autowired
    DocumentHelper helper;

    @Autowired
    HttpSession session;

    @Autowired
    DocumentAuthority authority;

    @ModelAttribute
    DocumentForm setUp() {
        return new DocumentForm();
    }

    /**
     * Ver指定したドキュメント情報を表示する
     * versionパラメータの値によって表示するドキュメントを変更する
     * last: 最新ドキュメント
     * 数値: 数値のドキュメント
     *
     * @param model        モデル
     * @param loggedInUser ユーザ情報
     * @param id           ドキュメントID
     * @param referer      遷移元URL
     * @param version      ドキュメントVer
     * @return VIEWのパス
     */
    @GetMapping(value = "{id}", params = "version")
    public String viewVersion(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                              @PathVariable("id") Long id, @RequestHeader(value = "referer", required = false) final String referer,
                              @RequestParam(value = "version", required = false) String version) {

        // 公開区分を格納
        Set<String> publicScope = helper.getPublicScopeSet(loggedInUser);

        DocumentRevision documentRevision;
        Document documentRecord = null;
        DocumentOperationUtil op = new DocumentOperationUtil(BASE_PATH);

        // 指定した公開区分でドキュメントを検索　閲覧可能なものがない場合null
        if (version.equals("latest")) {
            documentRevision = documentRevisionService.findLatest(id, publicScope);
        } else {
            documentRevision = documentRevisionService.versionSpecification(id, Long.parseLong(version), publicScope);
        }

        try {
            // Document型に変換
            documentRecord = beanMapper.map(documentRevision, Document.class);

            // 権限チェック
            authority.hasAuthority(Constants.OPERATION.VIEW, loggedInUser, documentRecord);

            // Modelに値を格納
            model.addAttribute("document", documentRevision);

        } catch (BusinessException e) {
            model.addAttribute(e.getResultMessages());
        } catch (AccessDeniedException | MappingException e) {
            // 空のドキュメント情報を生成
            Document document = new Document();

            // エラーメッセージを生成
            ResultMessages messages = ResultMessages.error().add("e.sl.fw.7003");

            model.addAttribute("document", document);
            model.addAttribute(messages);
        }

        //
        op.setURL_LIST("searchList");

        // セッション管理
        if (referer != null && helper.isReferer(referer)) {
            session.setAttribute("referer", referer);
        }

        // セッションに値が格納されている場合、セッションに格納されたURLへ遷移する
        if (session.getAttribute("referer") != null) {
            op.setBaseUrl("");
            op.setURL_LIST((String) session.getAttribute("referer"));
        }

        StateMap buttonState = helper.getButtonStateMap(Constants.OPERATION.VIEW, documentRecord, null, loggedInUser);
        buttonState.setViewFalse(Constants.BUTTON.GOTOUPDATE);

        model.addAttribute("buttonState", buttonState.asMap());
        model.addAttribute("fieldState",  helper.getFiledStateMap(Constants.OPERATION.VIEW, documentRecord, null).asMap());
        model.addAttribute("op", op);

        return BASE_PATH + "/form";
    }

    /**
     * 最新のドキュメント情報を表示する
     *
     * @param model        モデル
     * @param loggedInUser ユーザ情報
     * @param id           ドキュメントID
     * @param referer      遷移元URL
     * @return VIEWのパス
     */
    @GetMapping(value = "{id}/latest")
    public String viewLatest(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser,
                             @PathVariable("id") Long id, @RequestHeader(value = "referer", required = false) final String referer) {

        return viewVersion(model, loggedInUser, id, referer, "latest");

    }
}
