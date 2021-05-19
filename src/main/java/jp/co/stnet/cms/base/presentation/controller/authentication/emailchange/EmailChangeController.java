package jp.co.stnet.cms.base.presentation.controller.authentication.emailchange;

import jp.co.stnet.cms.base.application.service.authentication.AccountSharedService;
import jp.co.stnet.cms.base.application.service.authentication.EmailChangeService;
import jp.co.stnet.cms.base.domain.model.authentication.EmailChangeRequest;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

import java.util.Objects;

@Controller
@RequestMapping("emailchange")
public class EmailChangeController {

    @Autowired
    EmailChangeService emailChangeService;

    @Autowired
    AccountSharedService accountSharedService;

    @ModelAttribute
    public EmailChangeForm setUpForm() {
        return new EmailChangeForm();
    }

    @ModelAttribute
    public EmailChangeTokenForm setUpTokenForm() {
        return new EmailChangeTokenForm();
    }

    @GetMapping(params = "form")
    public String showForm() {
        return "emailchange/form";
    }

    @PostMapping
    public String request(@Validated EmailChangeForm form, BindingResult bindingResult, Model model,
                          RedirectAttributes attributes, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        if (bindingResult.hasErrors()) {
            return showForm();
        }
        String token = emailChangeService.createAndSendMailChangeRequest(loggedInUser.getUsername(), form.getNewEmail());
        attributes.addAttribute("token", token);
        return "redirect:/emailchange/formToken";
    }

    @GetMapping("formToken")
    public String showFormToken(EmailChangeTokenForm form, Model model, @RequestParam("token") String token) {
        form.setToken(token);
        return "emailchange/formToken";
    }

    @PostMapping(params = "change")
    public String change(@Validated EmailChangeTokenForm form, BindingResult bindingResult, Model model,
                         RedirectAttributes attributes, @AuthenticationPrincipal LoggedInUser loggedInUser) {

        if (bindingResult.hasErrors()) {
            return showFormToken(form, model, form.getToken());
        }

        EmailChangeRequest emailChangeRequest = null;
        try {
            emailChangeRequest = emailChangeService.findOne(form.getToken());
        } catch (ResourceNotFoundException | BusinessException e) {
            model.addAttribute(e.getResultMessages());
            return showFormToken(form, model, form.getToken());
        }

        if (Objects.equals(emailChangeRequest.getSecret(), form.getSecret())) {
            emailChangeService.changeEmail(form.getToken(), form.getSecret());
            return "redirect:/emailchange?complete";

        } else {

            emailChangeService.fail(form.getToken());
            attributes.addAttribute("token", form.getToken());
            return "redirect:/emailchange/formToken";
        }

    }

    @GetMapping(params = "complete")
    public String complete() {
        return "emailchange/complete";
    }


}
