package jp.co.stnet.cms.sales.presentation.controller.news;

import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.sales.application.service.news.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class NewsController {

    @Autowired
    NewsService newsService;

    /**
     *
     * お知らせから検索されたデータをお知らせ詳細に表示する
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "sales/news/{id}")
    public String newsDetail(Model model, @PathVariable("id") Long id) {

        //Idから検索結果を取得する
        Optional <Variable> optionalVariable = newsService.newsDetail(id);

        //Optionalから値を取得
        Variable variableDetail = optionalVariable.get();

        //モデルに属性を追加する
        model.addAttribute("variableDetail",variableDetail);

        return "/sales/news/newsDetail";
    }

}

