package jp.co.stnet.cms.base.application.repository.accesscounter;


import jp.co.stnet.cms.base.domain.model.common.AccessCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * AccessCounterリポジトリ.
 */
public interface AccessCounterRepository extends JpaRepository<AccessCounter, Long> {

    /**
     * URLで検索する。
     *
     * @param url URL
     * @return ヒットしたデータ
     */
    Optional<AccessCounter> findByUrl(String url);

}