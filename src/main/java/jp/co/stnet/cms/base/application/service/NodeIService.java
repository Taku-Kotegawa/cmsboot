package jp.co.stnet.cms.base.application.service;

import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.exception.InvalidArgumentBusinessException;
import jp.co.stnet.cms.common.exception.NoChangeBusinessException;
import jp.co.stnet.cms.common.exception.OptimisticLockingFailureBusinessException;
import org.springframework.data.domain.Page;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;

/**
 * エンティティ(リビジョン管理なし)用の抽象クラスのインタフェース
 *
 * @param <T>  エンティティのクラス(AbstractEntityのサブクラス)
 * @param <ID> 主キーのクラス
 */
public interface NodeIService<T extends AbstractEntity<ID>, ID> {

    /**
     * IDで検索
     */
    T findById(ID id);

    /**
     * DataTables用の検索(Page)
     */
    Page<T> findPageByInput(DataTablesInput input);

    /**
     * １件の保存
     *
     * @param entity 更新するエンティティ
     * @return 更新後のエンティティ
     * @throws NoChangeBusinessException                 既に登録されている内容と変更する箇所がない場合
     * @throws OptimisticLockingFailureBusinessException 楽観的排他チェックで更新に失敗した場合
     * @throws InvalidArgumentBusinessException          入力に不備がある場合
     * @throws ResourceNotFoundException                 検索条件に合致するエンティティが存在しない場合
     */
    T save(T entity);

    /**
     * 複数件の保存
     */
    Iterable<T> save(Iterable<T> entities);

    /**
     * １件の無効化
     */
    T invalid(ID id);

    /**
     * 複数件の無効化
     */
    Iterable<T> invalid(Iterable<ID> ids);

    /**
     * 1件の有効化
     */
    T valid(ID id);

    /**
     * 複数件の有効化
     */
    Iterable<T> valid(Iterable<ID> ids);

    /**
     * １件の削除
     */
    void delete(ID id);

    /**
     * 複数件の削除
     */
    void delete(Iterable<T> entities);

    /**
     * エンティティの比較
     *
     * @param entity 比較対象
     * @param other  比較対象
     * @return true:差異なし, false:差異あり
     */
    boolean equalsEntity(T entity, T other);
}
