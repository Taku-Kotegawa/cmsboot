package jp.co.stnet.cms.base.presentation.controller.dialog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("common/dialog")
@Controller
public class DialogController {

    @GetMapping("/test")
    public String test(Model model) {
        return "common/dialog/test";
    }
}
