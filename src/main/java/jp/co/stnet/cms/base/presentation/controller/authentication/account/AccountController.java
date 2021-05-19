package jp.co.stnet.cms.base.presentation.controller.authentication.account;


import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.service.authentication.AccountSharedService;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.application.service.filemanage.FileUploadSharedService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import jp.co.stnet.cms.base.domain.model.filemanage.TempFile;
import jp.co.stnet.cms.common.message.MessageKeys;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Controller
@RequestMapping("account")
public final class AccountController {

    @Autowired
    FileUploadSharedService fileUploadSharedService;

    @Autowired
    AccountSharedService accountSharedService;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @Value(value = "classpath:images/nobody.png")
    Resource nobodyImage;

    @Autowired
    Mapper beanMapper;

    @ModelAttribute
    public AccountCreateForm setUpAccountCreateForm() {
        return new AccountCreateForm();
    }

    @GetMapping
    public String view(@AuthenticationPrincipal LoggedInUser userDetails,
                       Model model) {
        Account account = userDetails.getAccount();
        model.addAttribute("account", account);
        return "account/view";
    }

    @GetMapping("/image")
    @ResponseBody
    public ResponseEntity<byte[]> showImage(
            @AuthenticationPrincipal LoggedInUser loggedInUser, final HttpServletResponse response)
            throws IOException {

        response.addHeader("Cache-Control", "max-age=60, must-revalidate, no-transform");

        FileManaged fileManaged = accountSharedService.getImage(loggedInUser.getUsername());

        HttpHeaders headers = new HttpHeaders();

        if (fileManaged != null) {
            headers.setContentType(fileManaged.getMediaType());
            return new ResponseEntity<byte[]>(
                    fileManagedSharedService.getFile(fileManaged.getId()),
                    headers,
                    HttpStatus.OK);
        } else {
            InputStream ins = nobodyImage.getInputStream();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<byte[]>(
                    IOUtils.toByteArray(ins),
                    headers,
                    HttpStatus.OK);
        }

    }

    @GetMapping(value = "/create", params = "form")
    public String createForm() {
        return "account/accountCreateForm";
    }

    @PostMapping(value = "/create", params = "redo")
    public String redoCreateForm(AccountCreateForm form) {
        return "account/accountCreateForm";
    }

    @PostMapping(value = "/create", params = "confirm")
    public String createConfirm(
            @Validated({AccountCreateForm.Confirm.class, Default.class}) AccountCreateForm form,
            BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return createForm();
        }
        if (accountSharedService.exists(form.getUsername())) {
            model.addAttribute(ResultMessages.error().add(
                    MessageKeys.E_SL_AC_5001));
            return createForm();
        }
        try {
            TempFile tempFile = new TempFile();
            tempFile.setBody(form.getImage().getBytes());
            tempFile.setOriginalName(form.getImage().getOriginalFilename());
            String fileId = fileUploadSharedService.uploadTempFile(tempFile);
            form.setImageId(fileId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        redirectAttributes.addFlashAttribute("accountCreateForm", form);
        return "account/accountConfirm";
    }

    @PostMapping("/create")
    public String create(
            @Validated({AccountCreateForm.CreateAccount.class, Default.class}) AccountCreateForm form,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return createForm();
        }
        Account accountRoles = beanMapper.map(form, Account.class);
        accountRoles.setRoles(Arrays.asList("USER"));

        String password = accountSharedService.create(accountRoles,
                form.getImageId());
        redirectAttributes.addFlashAttribute("firstName", form.getFirstName());
        redirectAttributes.addFlashAttribute("lastName", form.getLastName());
        redirectAttributes.addFlashAttribute("password", password);
        return "redirect:/account/create?complete";
    }

    @GetMapping(value = "/create", params = "complete")
    public String createComplete() {
        return "account/createComplete";
    }


}
