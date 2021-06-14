package jp.co.stnet.cms.base.presentation.controller.admin.index;


import jp.co.stnet.cms.base.application.service.index.IndexSharedService;
import jp.co.stnet.cms.example.application.service.PersonService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("admin/index")
public class ManageController {

    @Autowired
    IndexSharedService indexSharedService;

    @GetMapping("manage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manage(Model model) {
        return "admin/index/manage";
    }

    @GetMapping("view")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String view(Model model, @RequestParam String term) {
        return "admin/index/manage";
    }

    @GetMapping("{entityName}/reindexing")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String reindexing(Model model, @PathVariable() String entityName) {

        try {
            indexSharedService.reindexing(entityName);
        } catch (InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
            // TODO 例外処理
        }

        return "redirect:/admin/index/manage";
    }

}
