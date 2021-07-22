package jp.co.stnet.cms.base.application.service.accesscounter;

import jp.co.stnet.cms.base.application.service.NodeIService;
import jp.co.stnet.cms.base.domain.model.common.AccessCounter;

import java.util.Optional;

/**
 * AccessCounterService
 */
public interface AccessCounterService extends NodeIService<AccessCounter, Long> {

    /**
     * URLで検索する。
     *
     * @param url URL
     * @return ヒットしたデータのリスト
     */
    Optional<AccessCounter> findByUrl(String url);

    /**
     * アクセス数をカウントアップする。
     *
     * @param url URL
     * @return カウントアップ後のアクセス数
     */
    long countUp(String url);

}
