package jp.co.stnet.cms.base.presentation.controller.admin.account;

import jp.co.stnet.cms.base.application.service.account.AccountService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.presentation.controller.admin.upload.UploadForm;
import jp.co.stnet.cms.base.presentation.controller.job.JobStarter;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.datatables.OperationsUtil;
import jp.co.stnet.cms.common.util.StateMap;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Map;

import static jp.co.stnet.cms.base.presentation.controller.admin.account.AdminAccountConstant.*;

@Controller
@RequestMapping(BASE_PATH)
public class AdminAccountUploadController {

    @Autowired
    AccountService accountService;

    @Autowired
    AdminAccountAuthority authority;

    @Autowired
    FileManagedService fileManagedService;

    @Autowired
    JobStarter jobStarter;

    /**
     * アップロードファイル指定画面の表示
     */
    @GetMapping(value = "upload", params = "form")
    public String uploadForm(@ModelAttribute UploadForm form, Model model,
                             @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.UPLOAD, loggedInUser);

        form.setJobName(UPLOAD_JOB_ID);

        if (form.getUploadFileUuid() != null) {
            form.setUploadFileManaged(fileManagedService.findByUuid(form.getUploadFileUuid()));
        }

        model.addAttribute("pageTitle", "Import Account");
        model.addAttribute("referer", "list");
        model.addAttribute("fieldState", new StateMap(UploadForm.class, new ArrayList<>(), new ArrayList<>()).setInputTrueAll().asMap());
        model.addAttribute("op", new OperationsUtil(BASE_PATH));

        return TEMPLATE_UPLOAD_FORM;
    }

    /**
     * アップロード処理(バッチ実行)
     */
    @PostMapping(value = "upload")
    public String upload(@Validated UploadForm form, BindingResult result, Model model,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        authority.hasAuthority(Constants.OPERATION.UPLOAD, loggedInUser);

        final String jobName = UPLOAD_JOB_ID;

        Long jobExecutionId = null;

        if (!jobName.equals(form.getJobName()) || result.hasErrors()) {
            return uploadForm(form, model, loggedInUser);
        }

        FileManaged uploadFile = fileManagedService.findByUuid(form.getUploadFileUuid());
        String uploadFileAbsolutePath = fileManagedService.getFileStoreBaseDir() + uploadFile.getUri();
        String jobParams = "inputFile=" + uploadFileAbsolutePath;
        jobParams += ", encoding=" + form.getEncoding();
        jobParams += ", filetype=" + form.getFileType();

        try {
            jobExecutionId = jobStarter.start(jobName, jobParams);

        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();

            // メッセージをセットして、フォーム画面に戻る。

        }

        redirectAttributes.addAttribute("jobName", jobName);
        redirectAttributes.addAttribute("jobExecutionId", jobExecutionId);

        return "redirect:upload?complete";
    }

    /**
     * アップロード完了画面
     */
    @GetMapping(value = "upload", params = "complete")
    public String uploadComplete(Model model, @RequestParam Map<String, String> params, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        model.addAttribute("returnBackBtn", "一覧画面に戻る");
        model.addAttribute("returnBackUrl", "/admin/account/list");
        model.addAttribute("jobName", params.get("jobName"));
        model.addAttribute("jobExecutionId", params.get("jobExecutionId"));
        return TEMPLATE_UPLOAD_COMPLETE;
    }

}
