package jp.co.stnet.cms.base.presentation.controller.uploadfile;

import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("file")
public class UploadFileController {

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @ModelAttribute
    public UploadFileForm setUp() {
        return new UploadFileForm();
    }

    @GetMapping(value = "upload", params = "form")
    public String form(Model model, UploadFileForm form) {

        return "example/fileuploadForm";
    }

    @PostMapping("save")
    public String save(Model model, @Validated UploadFileForm form, BindingResult bindingResult,
                       @AuthenticationPrincipal LoggedInUser loggedInUser, HttpServletRequest request) {

        return "redirect:/file/upload?form";
    }

    @GetMapping("{uuid}/download")
    public String download(
            Model model,
            @PathVariable("uuid") String uuid,
            @AuthenticationPrincipal LoggedInUser loggedInUser) {

        FileManaged fileManaged = fileManagedSharedService.findByUuid(uuid);
        model.addAttribute(fileManaged);
        return "fileManagedDownloadView";
    }

}
