package jp.co.stnet.cms.base.application.repository.filemanage;


import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * FileManagedリポジトリ.
 */
@Repository
public interface FileManagedRepository extends JpaRepository<FileManaged, Long> {

    /**
     * Uuidで検索する。
     *
     * @param uuid UUID
     * @return ヒットしたデータ
     */
    Optional<FileManaged> findByUuid(String uuid);

    /**
     * Uuidとステータスで検索する。
     *
     * @param uuid   UUID
     * @param status ステータス
     * @return ヒットしたデータ
     */
    Optional<FileManaged> findByUuidAndStatus(String uuid, String status);

    /**
     * 作成日時以前かつステータスで検索する。
     *
     * @param deleteTo 作成日時
     * @param status   ステータス
     * @return ヒットしたデータのリスト
     */
    List<FileManaged> findAllByCreatedDateLessThanAndStatus(LocalDateTime deleteTo, String status);

}
