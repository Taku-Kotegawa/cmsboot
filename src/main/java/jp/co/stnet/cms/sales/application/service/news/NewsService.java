package jp.co.stnet.cms.sales.application.service.news;

import jp.co.stnet.cms.base.domain.model.variable.Variable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 *NewsService
 */
public interface NewsService {

    /**
     * お知らせに表示するデータの取得
     *
     * @return List Variable
     */
    List<Variable> findOpenNews();


    /**
     * IDからお知らせ詳細に表示するデータの取得
     *
     * @return Optional Variable
     */
    Optional<Variable> newsDetail(Long id);







}
