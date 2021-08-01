package jp.co.stnet.cms.common.dialect;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

public class PageInfoDialect implements IExpressionObjectDialect {

    // (1)
    private static final String PAGE_INFO_DIALECT_NAME = "pageInfo";
    private static final Set<String> EXPRESSION_OBJECT_NAMES = Collections
            .singleton(PAGE_INFO_DIALECT_NAME);

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {

            // (1)
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return EXPRESSION_OBJECT_NAMES;
            }

            // (2)
            @Override
            public Object buildObject(IExpressionContext context,
                                      String expressionObjectName) {
                if (PAGE_INFO_DIALECT_NAME.equals(expressionObjectName)) {
                    return new PageInfo();
                }
                return null;
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }

        };
    }

    @Override
    public String getName() {
        return "PageInfoDialect";
    }

}