package jp.co.stnet.cms.base.application.service.index;

/**
 * Lucene索引を操作する
 */
public interface IndexService {

    /**
     * Lucene索引を更新する(非同期処理)
     *
     * @param entityName エンティティ名
     * @throws InterruptedException   ?
     * @throws ClassNotFoundException エンティティ名のクラスが存在しない場合
     */
    void reindexing(String entityName) throws InterruptedException, ClassNotFoundException;

    /**
     * Lucene索引を更新する(同期処理)
     *
     * @param entityName エンティティ名
     * @return true:成功
     * @throws InterruptedException   ?
     * @throws ClassNotFoundException エンティティ名のクラスが存在しない場合
     */
    boolean reindexingSync(String entityName) throws InterruptedException, ClassNotFoundException;

}
