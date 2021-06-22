package jp.co.stnet.cms.base.application.service;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.domain.model.AbstractEntity;
import jp.co.stnet.cms.base.domain.model.StatusInterface;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.Column;
import jp.co.stnet.cms.common.datatables.Order;
import jp.co.stnet.cms.common.exception.IllegalStateBusinessException;
import jp.co.stnet.cms.common.exception.NoChangeBusinessException;
import jp.co.stnet.cms.common.exception.OptimisticLockingFailureBusinessException;
import jp.co.stnet.cms.common.message.MessageKeys;
import jp.co.stnet.cms.common.util.BeanUtils;
import jp.co.stnet.cms.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.query.QueryEscapeUtils;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Slf4j
@Transactional
public abstract class AbstractNodeService<T extends AbstractEntity<ID> & StatusInterface, ID> implements NodeIService<T, ID> {

    protected final Class<T> clazz;

    protected final Map<String, String> fieldMap;

    protected final Map<String, Annotation> elementCollectionFieldsMap;

    protected final Map<String, Annotation> relationFieldsMap;


    @Autowired
    public Mapper beanMapper;

    @PersistenceContext
    EntityManager entityManager;

    protected AbstractNodeService(Class<T> clazz) {
        this.clazz = clazz;
        this.fieldMap = BeanUtils.getFields(this.clazz, null);
        this.elementCollectionFieldsMap = BeanUtils.getFieldByAnnotation(clazz, null, ElementCollection.class);
        this.relationFieldsMap = BeanUtils.getFieldByAnnotation(clazz, null, OneToOne.class);
        relationFieldsMap.putAll(BeanUtils.getFieldByAnnotation(clazz, null, ManyToOne.class));
    }

    protected AbstractNodeService() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.fieldMap = BeanUtils.getFields(this.clazz, null);
        this.elementCollectionFieldsMap = BeanUtils.getFieldByAnnotation(clazz, null, ElementCollection.class);
        this.relationFieldsMap = BeanUtils.getFieldByAnnotation(clazz, null, OneToOne.class);
        relationFieldsMap.putAll(BeanUtils.getFieldByAnnotation(clazz, null, ManyToOne.class));
    }

    abstract protected JpaRepository<T, ID> getRepository();

    @Override
    public T findById(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResultMessages.error().add(MessageKeys.E_SL_FW_5001, id)));
    }

    /**
     * 保存前処理
     */
    protected void beforeSave(T entity, T current) {
    }

    @Override
    public T save(T entity) {

        T currentCopy = null;

        if (!entity.isNew()) {
            T current = findById(entity.getId());
            currentCopy = beanMapper.map(current, clazz);

            // 下書き保存時、本保存から変更がなければ保存しない(ステータスを変更チェックの対象から除外)
            if (current.getStatus().equals(Status.VALID.getCodeValue())
                    && entity.getStatus().equals(Status.DRAFT.getCodeValue())) {
                currentCopy.setStatus(Status.DRAFT.getCodeValue());
            }

            if (equalsEntity(entity, currentCopy)) {
                throw new NoChangeBusinessException(ResultMessages.warning().add((MessageKeys.W_CM_FW_2001)));
            }
        }

        try {
            beforeSave(entity, currentCopy);
            entity = getRepository().saveAndFlush(entity);
            afterSave(entity, currentCopy);

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureBusinessException(ResultMessages.error().add(MessageKeys.E_CM_FW_8001));
        } catch (DataIntegrityViolationException e) {
            throw new OptimisticLockingFailureBusinessException(ResultMessages.error().add(MessageKeys.E_CM_FW_8002, e.getMessage()));
        }

        entityManager.detach(entity);
        return getRepository().findById(Objects.requireNonNull(entity.getId())).orElse(null);
    }

    /**
     * 保存後処理
     */
    protected void afterSave(T entity, T current) {
    }


    /**
     * エンティティの比較(オーバライトして利用する)
     * @param entity
     * @param currentCopy
     * @return
     */
    public boolean equalsEntity(T entity, T currentCopy) {
        return Objects.equals(entity, currentCopy);
    }

    @Override
    public Iterable<T> save(Iterable<T> entities) {
        List<T> saved = new ArrayList<>();
        for (T entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }

    @Override
    public T invalid(ID id) {
//        T entity = beanMapper.map(findById(id), clazz);
        T entity = findById(id);
        entityManager.detach(entity);
        if (!entity.getStatus().equals(Status.VALID.getCodeValue())) {
            throw new IllegalStateBusinessException(ResultMessages.warning().add((MessageKeys.W_CM_FW_2003)));
        }
        entity.setStatus(Status.INVALID.getCodeValue());
        return save(entity);
    }

    @Override
    public Iterable<T> invalid(Iterable<ID> ids) {
        List<T> saveds = new ArrayList<>();
        for (ID id : ids) {
            T entity = findById(id);

            // ステータスがVALID以外のデータは処理をスキップする
            if (entity.getStatus().equals(Status.VALID.getCodeValue())) {
                saveds.add(invalid(id));
            }
        }
        return saveds;
    }

    @Override
    public T valid(ID id) {
//        T entity = beanMapper.map(findById(id), clazz);
        T entity = findById(id);
        entityManager.detach(entity);
        if (!entity.getStatus().equals(Status.INVALID.getCodeValue())) {
            throw new IllegalStateBusinessException(ResultMessages.warning().add((MessageKeys.W_CM_FW_2004)));
        }
        entity.setStatus(Status.VALID.getCodeValue());
        return save(entity);
    }

    @Override
    public Iterable<T> valid(Iterable<ID> ids) {
        List<T> saveds = new ArrayList<>();
        for (ID id : ids) {
            T entity = findById(id);
            if (entity.getStatus().equals(Status.INVALID.getCodeValue())) {
                saveds.add(valid(id));
            }
        }
        return saveds;
    }

    @Override
    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    @Override
    public void delete(Iterable<T> entities) {
        for (T entity : entities) {
            delete(entity.getId());
        }
    }

    @Override
    public Page<T> findPageByInput(DataTablesInput input) {
        return new PageImpl<T>(
                getJPQLQuery(input, false, clazz).getResultList(),
                PageRequest.of(input.getStart() / input.getLength(), input.getLength()),
                (Long) getJPQLQuery(input, true, clazz).getSingleResult());
    }

    /**
     * JPQLQueryを作成する。
     * @param input DataTablesInput
     * @param count true: 件数を取得する, false: しない
     * @param clazz エンティティクラス
     * @return
     */
    protected Query getJPQLQuery(DataTablesInput input, boolean count, Class clazz) {

        String sql = getJPQL(input, count, clazz);

        // パラメータ値設定(データ取得範囲)Limit
        TypedQuery typedQuery;
        if (!count) {
            // 通常は１ページに必要な範囲を抽出する。
            typedQuery = entityManager.createQuery(sql, clazz);
            typedQuery.setFirstResult(input.getStart()); //開始行数
            typedQuery.setMaxResults(input.getLength()); //取得件数
        } else {
            // 件数を取得する場合は、データの範囲を指定しない。
            typedQuery = entityManager.createQuery(sql, Long.class);
        }

        // パラメータ値設定(フィールドフィルタ)
        for (Column column : input.getColumns()) {
            String originalColumnName = column.getData();
            String convertColumnName = convertColumnName(originalColumnName);
            String replacedColumnName = replacedColumnName(convertColumnName);
            if (column.getSearchable() && !StringUtils.isEmpty(column.getSearch().getValue())) {

                if (isIdLong(convertColumnName)) {
                    try {
                        typedQuery.setParameter(replacedColumnName, Long.valueOf(column.getSearch().getValue()));
                    } catch (Exception e) {
                        typedQuery.setParameter(replacedColumnName, null);
                    }

                } else if (isIdLong(convertColumnName)) {
                    try {
                        typedQuery.setParameter(replacedColumnName, Integer.valueOf(column.getSearch().getValue()));
                    } catch (Exception e) {
                        typedQuery.setParameter(replacedColumnName, null);
                    }

                } else if (isIdString(convertColumnName)) {
                    typedQuery.setParameter(replacedColumnName, column.getSearch().getValue());

                } else if (isFilterINClause(convertColumnName)) {
                    typedQuery.setParameter(replacedColumnName, Arrays.asList(StringUtils.split(column.getSearch().getValue(), ",")));

                } else if (isBoolean(convertColumnName)) {
                    typedQuery.setParameter(replacedColumnName, "1".equals(column.getSearch().getValue()));

                } else if (isEnum(convertColumnName)) {
                    typedQuery.setParameter(
                            replacedColumnName,
                            getEnumListByName(convertColumnName, Arrays.asList(StringUtils.split(column.getSearch().getValue(), ","))));

                } else {
                    // 検索文字列を%で囲む
                    typedQuery.setParameter(replacedColumnName, QueryEscapeUtils.toContainingCondition(column.getSearch().getValue()));

                }
            }
        }

        // パラメータ値設定(グローバルフィルタ)
        if (!hasFieldFilter(input) && !StringUtils.isEmpty(input.getSearch().getValue())) {
            typedQuery.setParameter("globalSearch", QueryEscapeUtils.toContainingCondition(input.getSearch().getValue()));
        }

        return typedQuery;
    }

    /**
     * SQL(JPQL)文を作成する。
     * @param input DataTablesInput
     * @param count true: 件数を取得する, false: しない
     * @param clazz エンティティクラス
     * @return SQL(JPQL)文字列
     */
    protected String getJPQL(DataTablesInput input, boolean count, Class clazz) {

        // SQL(JPQL)を格納する変数
        StringBuilder sql = new StringBuilder();

        // SELECT句 FROM句
        sql.append(getSelectFromClause(clazz, count));

        // LEFT OUTER JOIN の追加
        sql.append(getLeftOuterJoinClause(input));

        // WHERE句の追加
        sql.append(getWhereClause(input));

        // OrderBY句
        if (!count) {
            sql.append(getOrderClause(input));
        }

        return sql.toString();
    }

    /**
     * SELECT c FROM エンティティ を作成する
     *
     * @param clazz エンティティ
     * @param count true: 件数を取得する, false: しない
     * @return 文字列
     */
    protected StringBuilder getSelectFromClause(Class clazz, boolean count) {
        StringBuilder sql = new StringBuilder();
        // SELECT句
        if (!count) {
            sql.append("SELECT distinct c");
        } else {
            sql.append("SELECT count(distinct c)");
        }

        // FROM句 主テーブル
        sql.append(" FROM ");
        sql.append(clazz.getSimpleName());
        sql.append(" c ");

        return sql;
    }

    /**
     * 外部結合(LEFT OUTER文)を作成する。
     * @param input DataTablesInput
     * @return 外部結合文字列
     */
    protected StringBuilder getLeftOuterJoinClause(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        // @OneToOne etc, @ElementCollection フィールドのための結合
        for (Column column : input.getColumns()) {
            String originalColumnName = column.getData();
            String convertedColumnName = convertColumnName(originalColumnName);

            if (isCollectionElement(convertedColumnName)) {
                sql.append(" LEFT JOIN c.");
                sql.append(convertedColumnName);
            } else if (getRelationEntity(originalColumnName) != null) {
                sql.append(" LEFT JOIN c.");
                sql.append(getRelationEntity(originalColumnName));
            }
        }
        return sql;
    }

    /**
     * OrderBy句文字列を作成する。
     *
     * @param input DataTablesInput
     * @return OrderBy句文字列
     */
    protected StringBuilder getOrderClause(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        List<String> orderClauses = new ArrayList<>();
        for (Order order : input.getOrder()) {
            String originalFiledName = input.getColumns().get(order.getColumn()).getData();
            String convertColumnName = convertColumnName(originalFiledName);

            String orderClause;
            if (isCollection(convertColumnName)) {
                orderClause = convertColumnName + " " + order.getDir();
            } else if (isRelation(originalFiledName)) {
                orderClause = "c." + originalFiledName + " " + order.getDir();
            } else {
                orderClause = "c." + convertColumnName + " " + order.getDir();
            }
            orderClauses.add(orderClause);
        }

        if (!orderClauses.isEmpty()) {
            sql.append(" ORDER BY ");
            sql.append(StringUtils.join(orderClauses, ','));
        }

        return sql;
    }

    /**
     * Where句を作成する。
     * @param input DataTablesInput
     * @return Where句文字列
     */
    protected StringBuilder getWhereClause(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        // Where句
        if (hasFieldFilter(input)) {
            // フィールドフィルタ
            sql.append(" WHERE 1 = 1 ");
            for (Column column : input.getColumns()) {
                sql.append(getFieldFilterWhereClause(column));
            }

        } else {
            // グローバルフィルタ
            if (!StringUtils.isEmpty(input.getSearch().getValue())) {
                sql.append(" WHERE 1 = 2 ");
                for (Column column : input.getColumns()) {
                    sql.append(getGlobalFilterWhereClause(column));
                }
            }
        }
        return sql;
    }

    /**
     * グローバルフィルターのためのフィールド毎の等号文字列を作成する。
     * @param column Column
     * @return 等号文字列
     */
    protected StringBuilder getGlobalFilterWhereClause(Column column) {
        StringBuilder sql = new StringBuilder();
        if (column.getSearchable()) {
            sql.append(" OR ");
            String originalColumnName = column.getData();
            String convertedColumnName = convertColumnName(originalColumnName);
            if (isLocalDate(convertedColumnName)) {
                sql.append("function('date_format', c.");
                sql.append(convertedColumnName);
                sql.append(", '%Y/%m/%d') LIKE :globalSearch ESCAPE '~'");
            } else if (isLocalDateTime(convertedColumnName)) {
                sql.append("function('date_format', c.");
                sql.append(convertedColumnName);
                sql.append(", '%Y/%m/%d %h:%i:%s') LIKE :globalSearch ESCAPE '~'");
            } else if (isNumber(convertedColumnName)) {
                sql.append("function('to_char', c.");
                sql.append(convertedColumnName);
                sql.append(", 'FM999999999') LIKE :globalSearch ESCAPE '~'");
            } else if (isCollection(convertedColumnName)) {
                sql.append(convertedColumnName);
                sql.append(" LIKE :globalSearch ESCAPE '~'");
            } else if (isBoolean(convertedColumnName)) {

            } else if (isRelation(originalColumnName)) {
                sql.append("c." + originalColumnName);
                sql.append(" LIKE :globalSearch ESCAPE '~'");
            } else {
                sql.append("c.");
                sql.append(convertedColumnName);
                sql.append(" LIKE :globalSearch ESCAPE '~'");
            }
        }
        return sql;
    }

    /**
     * フィールドフィルターのためのフィールド毎の等号文字列を作成する。
     * @param column Column
     * @return 等号文字列
     */
    protected StringBuilder getFieldFilterWhereClause(Column column) {
        StringBuilder sql = new StringBuilder();

        if (!StringUtils.isEmpty(column.getSearch().getValue())) {

            String originalColumnName = column.getData();
            String convertedColumnName = convertColumnName(originalColumnName);
            String replacedColumnName = replacedColumnName(convertedColumnName);

            sql.append(" AND ");
            if (isId(convertedColumnName)) {
                sql.append("c." + convertedColumnName);
                sql.append(" = :");
                sql.append(replacedColumnName);
            } else if (isFilterINClause(convertedColumnName)) {
                sql.append("c." + convertedColumnName);
                sql.append(" IN (:");
                sql.append(replacedColumnName);
                sql.append(")");
            } else if (isEnum(convertedColumnName)) {
                sql.append("c." + convertedColumnName);
                sql.append(" IN (:");
                sql.append(replacedColumnName);
                sql.append(")");
            } else if (isLocalDate(convertedColumnName)) {
                sql.append("function('date_format', c.");
                sql.append(convertedColumnName);
                sql.append(", '%Y/%m/%d') LIKE :");
                sql.append(replacedColumnName);
                sql.append(" ESCAPE '~'");
            } else if (isLocalDateTime(convertedColumnName)) {
                sql.append("function('date_format', c.");
                sql.append(convertedColumnName);
                sql.append(", '%Y/%m/%d %h:%i:%s') LIKE :");
                sql.append(replacedColumnName);
                sql.append(" ESCAPE '~'");
            } else if (isNumber(convertedColumnName)) {
                sql.append("function('to_char', c.");
                sql.append(convertedColumnName);
                sql.append(", 'FM999999999') LIKE :");
                sql.append(replacedColumnName);
                sql.append(" ESCAPE '~'");
            } else if (isCollection(convertedColumnName)) {
                sql.append(convertedColumnName);
                sql.append(" LIKE :"); // TODO 何かおかしい
                sql.append(replacedColumnName);
                sql.append(" ESCAPE '~'");
            } else if (isRelation(originalColumnName)) {
                sql.append("c." + originalColumnName);
                sql.append(" LIKE :");
                sql.append(replacedColumnName);
                sql.append(" ESCAPE '~'");
            } else if (isBoolean(convertedColumnName)) {
                sql.append("c.");
                sql.append(convertedColumnName);
                sql.append(" = :");
                sql.append(replacedColumnName);
//                sql.append(" ESCAPE '~'");
            } else {
                sql.append("c.");
                sql.append(convertedColumnName);
                sql.append(" LIKE :");
                sql.append(replacedColumnName);
                sql.append(" ESCAPE '~'");
            }
        }
        return sql;
    }

    /**
     * フィールドフィルターの設定されている
     * @param input DataTablesInput
     * @return true:フィールドフィルタを設定あり, false:なし
     */
    protected boolean hasFieldFilter(DataTablesInput input) {
        for (Column column : input.getColumns()) {
            if (column.getSearchable() && !StringUtils.isEmpty(column.getSearch().getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定されたフィールド名がLocalDate型かどうか
     * @param fieldName フィールド名
     * @return true:LocalDate型である, false:ない
     */
    protected boolean isLocalDate(String fieldName) {
        return "java.time.LocalDate".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名がLocalDateTIme型かどうか
     * @param fieldName フィールド名
     * @return true:LocalDateTIme型である, false:ない
     */
    protected boolean isLocalDateTime(String fieldName) {
        return "java.time.LocalDateTime".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名がNumber型かどうか
     * @param fieldName フィールド名
     * @return true:Number型である, false:ない
     */
    protected boolean isNumber(String fieldName) {
        return "java.lang.Integer".equals(fieldMap.get(fieldName))
                || "java.lang.Long".equals(fieldMap.get(fieldName))
                || "java.lang.BigDecimal".equals(fieldMap.get(fieldName))
                || "java.lang.Float".equals(fieldMap.get(fieldName))
                || "java.lang.Double".equals(fieldMap.get(fieldName))
                || "java.lang.Short".equals(fieldMap.get(fieldName))
                || "java.lang.Byte".equals(fieldMap.get(fieldName))
                || "java.math.BigInteger".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名がCollection型 or List型 かどうか
     * @param fieldName フィールド名
     * @return true:Collection型である, false:ない
     */
    protected boolean isCollection(String fieldName) {
        return "java.util.Collection".equals(fieldMap.get(fieldName))
                || "java.util.Set".equals(fieldMap.get(fieldName))
                || "java.util.List".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名がBooleanかどうか
     * @param fieldName フィールド名
     * @return true:Booleanである, false:ない
     */
    protected boolean isBoolean(String fieldName) {
        return "java.lang.Boolean".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名はEnumかどうか
     * @param fieldName フィールド名
     * @return true:Enumである, false:ない
     */
    protected boolean isEnum(String fieldName) {
        try {
            Class<?> c = Class.forName(fieldMap.get(fieldName));
            return c.isEnum();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 指定されたフィールド名に@IDが設定されている。
     * @param fieldName フィールド名
     * @return true:IDである, false:ない
     */
    protected boolean isId(String fieldName) {
        Map<String, Annotation> map = BeanUtils.getFieldByAnnotation(clazz, null, Id.class);
        return map.containsKey(fieldName);
    }

    /**
     * 指定されたフィールド名に@IDが設定され、Integer型である。
     * @param fieldName フィールド名
     * @return true:Integer型のIDである, false:ない
     */
    protected boolean isIdInteger(String fieldName) {
        return isId(fieldName) && "java.lang.Integer".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名に@IDが設定され、Long型である。
     * @param fieldName フィールド名
     * @return true:Long型のIDである, false:ない
     */
    protected boolean isIdLong(String fieldName) {
        return isId(fieldName) && "java.lang.Long".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名に@IDが設定され、String型である。
     * @param fieldName フィールド名
     * @return true:String型のIDである, false:ない
     */
    protected boolean isIdString(String fieldName) {
        return isId(fieldName) && "java.lang.String".equals(fieldMap.get(fieldName));
    }

    /**
     * 指定されたフィールド名がCollectionElementかどうか
     * @param fieldName フィールド名
     * @return true:CollectionElementである, false:ない
     */
    protected boolean isCollectionElement(String fieldName) {
        return elementCollectionFieldsMap.containsKey(fieldName);
    }

    /**
     * 指定したフィールドがリレーションかどうか
     * @param fieldName フィールド名
     * @return true:リレーションである, false:ない
     */
    protected boolean isRelation(String fieldName) {
        return getRelationEntity(fieldName) != null;
    }

    /**
     * LIKEでなくINで検索するフィールドを設定する。(オーバーライトする前提)
     *
     * @param fieldName フォールド名
     * @return true:INで検索する, false:しない
     */
    protected boolean isFilterINClause(String fieldName) {
        return isCollectionElement(fieldName);
    }

    /**
     * 検索文字列のリストをEnumのリストに変換する(サブクラスでオーバーライトして利用する)
     *
     * @param fieldName フィールド名
     * @param values    検索文字列のリスト
     * @return Enumのリスト
     */
    protected List<Object> getEnumListByName(String fieldName, List<String> values) {
        // オーバーライトしないと何も検索しない。
        return new ArrayList<>();
    }

    /**
     * DataTablesのフィールド名とエンティティのフィールド名の変換
     *
     * @param org 変換前のフィールド名
     * @return 変換後のフォールド名
     */
    protected String convertColumnName(String org) {
        if (StringUtils.endsWith(org, "Label")) {
            return StringUtils.left(org, org.length() - 5);
        } else {
            return org;
        }
    }

    /**
     * フィールド名の変換(プレースフォルダ用にフィールド名に . が使えないことへの対応)
     *
     * @param fieldName . を含むフィールド名
     * @return . を _ に置換したフィールド名
     */
    protected String replacedColumnName(String fieldName) {
        return fieldName.replace('.', '_');
    }

    /**
     * 指定したフィールドが外部結合の場合、結合するエンティテイ名を取得する。
     *
     * @param fieldName フィールド名
     * @return 結合するエンティティ名, 存在しない場合はnull.
     */
    protected String getRelationEntity(String fieldName) {

        String entityName = null;
        if (fieldName != null && fieldName.contains(".")) {
            entityName = fieldName.substring(0, fieldName.indexOf("."));
        }

        if (relationFieldsMap.containsKey(entityName)) {
            return entityName;
        } else {
            return null;
        }
    }


}
