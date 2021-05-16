package jp.co.stnet.cms.base.application.service;


import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.AbstractRevisionEntity;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import org.springframework.data.domain.Page;

/**
 * NodeRevIService.
 * <p>
 * リビジョン管理機能を備えたエンティティ用の抽象クラスのためインタフェース
 *
 * @param <T>  エンティティのクラス(AbstractEntityのサブクラス)
 * @param <U>  リビジョンのエンティティのクラス(AbstractRevisionEntityのサブクラス)
 * @param <ID> エンティティの主キーのクラス
 */
public interface NodeRevIService<T extends AbstractEntity<ID>, U extends AbstractRevisionEntity, ID> extends NodeIService<T, ID> {

    /**
     * 1件の下書き保存
     *
     * @param entity entity
     * @return 保存後のエンティティ
     */
    T saveDraft(T entity);

    /**
     * 下書き削除
     *
     * @param id ID
     * @return 保存後のエンティティ
     */
    T cancelDraft(ID id);

    /**
     * @param input
     * @return
     */
    Page<U> findMaxRevPageByInput(DataTablesInput input);

    /**
     * 　有効な最新リビジョンを取得
     *
     * @param id
     * @return
     */
    U findByIdLatestRev(ID id);

    /**
     * リビジョン番号で取得
     *
     * @param rid
     * @return
     */
    U findByRid(Long rid);

}
