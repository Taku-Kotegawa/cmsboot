package jp.co.stnet.cms.sales.application.service.document;

import jp.co.stnet.cms.common.datatables.Column;
import jp.co.stnet.cms.common.datatables.DataTablesInput;
import jp.co.stnet.cms.common.datatables.Order;
import jp.co.stnet.cms.sales.domain.model.document.DocumentIndex;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional(readOnly = true)
public class DocumentSearchServiceImpl implements DocumentSearchService {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public SearchResult<DocumentIndex> searchByInput(DataTablesInput input) {


        SearchSession searchSession = Search.session(entityManager);

        int pageSize = input.getLength();
        long offset = input.getStart();

        SearchScope<DocumentIndex> scope = searchSession.scope(DocumentIndex.class);

        SearchResult<DocumentIndex> result = searchSession.search(DocumentIndex.class)
                .where(
                        f -> {
                            BooleanPredicateClausesStep<?> b = f.bool();
                            int filterFieldNum = 0;

                            for (Column column : input.getColumns()) {
                                String fieldName = column.getData();
                                String convertedName = convertColumnName(fieldName);
                                String value = input.getColumn(fieldName).getSearch().getValue();
                                if (value != null) {
                                    b = b.must(f.wildcard().fields(convertedName).matching("*" + value + "*"));
                                    filterFieldNum++;
                                }
                            }

                            if (filterFieldNum > 0) {
                                return b;
                            } else {
                                return f.matchAll();
                            }
                        }
                )
                .sort(f -> f.composite(b -> {
                    for (Order order : input.getOrder()) {
                        String fieldName = input.getColumns().get(order.getColumn()).getData();
                        String convertedName = convertColumnName(fieldName);
                        SortOrder sortOrder = "asc".equals(order.getDir()) ? SortOrder.ASC : SortOrder.DESC;
                        switch (convertedName) {
                            case "dummyField":
                                b.add(f.field("dummyField").order(sortOrder));
                                break;
                            case "id":
                                b.add(f.field("pk.id").order(sortOrder));
                                break;
                            case "docCategory1.Value1":
                                b.add(f.field("docCategory1").order(sortOrder));
                                break;
                            case "docCategory2.Value1":
                                b.add(f.field("docCategory2").order(sortOrder));
                                break;
                            case "docService1.Value1":
                                b.add(f.field("docService1").order(sortOrder));
                                break;
                            case "docService2.Value1":
                                b.add(f.field("docService2").order(sortOrder));
                                break;
                            case "docService2.Value3":
                                b.add(f.field("docService3").order(sortOrder));
                                break;
                            default:
                                b.add(f.field(convertedName).order(sortOrder));
                                break;
                        }
                    }
                }))
                .fetch((int) offset, pageSize);

        return result;
    }

    /**
     * DataTablesのフィールド名とエンティティのフィールド名の変換
     *
     * @param fieldName 変換前のフィールド名
     * @return 変換後のフォールド名
     */
    protected String convertColumnName(String fieldName) {

        if ("filesLabel".equals(fieldName)) {
            return "fileManaged.originalFilename";

        } else if (StringUtils.endsWith(fieldName, "Label")) {
            return StringUtils.left(fieldName, fieldName.length() - 5);

        } else {
            return fieldName;

        }
    }
}
