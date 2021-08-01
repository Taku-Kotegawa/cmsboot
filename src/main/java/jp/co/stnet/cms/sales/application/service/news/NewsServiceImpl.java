package jp.co.stnet.cms.sales.application.service.news;

import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.sales.application.repository.news.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

@Autowired
    NewsRepository newsRepository;

@Autowired
    CustomDateFactory customDateFactory;

    private String TYPE="NEWS";
    private String STATUS = "1";

    /**
     * お知らせに表示するデータの取得
     *
     * @return List Variable
     */
    public List<Variable> findOpenNews(){
        //検索日の取得
        LocalDate localDate = customDateFactory.newLocalDate();

        //TYPE,STATUS,検索日から
        // お知らせ一覧に表示するVariableをListで取得
        List<Variable> variableList = newsRepository.findByTypeAndStatusAndDate1LessThanEqualAndDate2GreaterThanEqual(TYPE,STATUS,localDate,localDate) ;
        return variableList;
    }

    /**
     * IDからお知らせ詳細に表示するデータの取得
     *
     * @return Optional Variable
     */
    public Optional<Variable> newsDetail(Long id){

        //ID検索からお知らせ詳細に表示する
        Optional<Variable> variableList = newsRepository.findById(id) ;
        return variableList;
    }


}
