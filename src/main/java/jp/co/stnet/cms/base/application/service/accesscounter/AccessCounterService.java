package jp.co.stnet.cms.base.application.service.accesscounter;

import jp.co.stnet.cms.base.application.service.NodeIService;
import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
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

    /**
     * 権限が有無を確認する。(常にTrueを返す)
     *
     * @param operation 操作
     * @param loggedInUser ログインユーザ情報
     * @return true:権限あり,false:権限なし
     */
    Boolean hasAuthority(String operation, LoggedInUser loggedInUser);

}
