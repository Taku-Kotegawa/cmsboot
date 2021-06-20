package jp.co.stnet.cms.example.presentation.controller.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.message.MailSendHistory;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.DataTablesOutput;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.example.application.service.document.DocumentService;
import jp.co.stnet.cms.example.domain.model.document.Document;
import jp.co.stnet.cms.example.domain.model.document.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.terasoluna.gfw.common.codelist.CodeList;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jp.co.stnet.cms.example.presentation.controller.document.DocumentConstant.*;


@Controller
@RequestMapping(BASE_PATH)
public class DocumentListController {

    @Autowired
    Mapper beanMapper;
    
    @Autowired
    DocumentService documentService;

    @Autowired
    @Named("CL_DOC_STAGE")
    CodeList useStageCodeList;

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
    public DataTablesOutput<DocumentListBean> listJson(@Validated DataTablesInput input) {

        OperationsUtil op = new OperationsUtil(null);

        List<DocumentListBean> list = new ArrayList<>();
        Page<Document> documentPage = documentService.findPageByInput(input);


        for (Document document : documentPage.getContent()) {
            DocumentListBean documentListBean = beanMapper.map(document, DocumentListBean.class);
            documentListBean.setOperations(getToggleButton(document.getId().toString(), op));
            documentListBean.setDT_RowId(document.getId().toString());

            // ステータスラベル
            String statusLabel = document.getStatus().equals(Status.VALID.getCodeValue()) ? Status.VALID.getCodeLabel() : Status.INVALID.getCodeLabel();
            documentListBean.setStatusLabel(statusLabel);

            // 利用シーン
            List<String> useStages = new ArrayList<>();
            for (String v : useStageCodeList.asMap().keySet()) {
                if (document.getUseStage().contains(v)) {
                    useStages.add(useStageCodeList.asMap().get(v));
                }
            }
            documentListBean.setUseStageLabel(String.join(", ", useStages));

            // ファイル名のリスト
            List<String> originalFilenames = new ArrayList<>();
            for (File file : document.getFiles()) {
                if (file.getFileManaged() != null) {
                    originalFilenames.add(file.getFileManaged().getOriginalFilename());
                }
            }
            documentListBean.setFilesLabel(String.join("<br>", originalFilenames));

            documentListBean.setFiles(new ArrayList<>());


            list.add(documentListBean);
        }

        DataTablesOutput<DocumentListBean> output = new DataTablesOutput<>();
        output.setData(list);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(0);
        output.setRecordsFiltered(documentPage.getTotalElements());

        return output;
    }

    /**
     * 一覧画面のボタンHTMLの準備
     * @param id
     * @param op
     * @return
     */
    private String getToggleButton(String id, OperationsUtil op) {

        StringBuffer link = new StringBuffer();
        link.append("<a href=\"" + op.getEditUrl(id) + "\" class=\"btn btn-button btn-sm\" style=\"white-space: nowrap\">" + op.getLABEL_EDIT() + "</a>");
        return link.toString();
    }    
    
}
