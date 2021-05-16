package jp.co.stnet.cms.base.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * リビジョン管理エンティティ用のリポジトリ.
 *
 * @param <T>  データを格納するクラス
 * @param <ID> 主キーのクラス
 */
@NoRepositoryBean
public interface NodeRevRepository<T, ID> extends JpaRepository<T, Long> {

    /**
     * 最新リビジョンのデータを取得
     *
     * @param id 内部ID
     * @return エンティティ
     */
    T findByIdLatestRev(ID id);

}
