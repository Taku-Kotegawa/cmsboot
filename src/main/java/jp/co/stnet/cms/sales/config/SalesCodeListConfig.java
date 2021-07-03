package jp.co.stnet.cms.sales.config;

import jp.co.stnet.cms.sales.domain.model.document.CustomerPublic;
import jp.co.stnet.cms.sales.domain.model.document.DocPublicScope;
import jp.co.stnet.cms.sales.domain.model.document.SaveRevision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.terasoluna.gfw.common.codelist.EnumCodeList;
import org.terasoluna.gfw.common.codelist.JdbcCodeList;

import javax.sql.DataSource;

@Configuration
public class SalesCodeListConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private JdbcCodeList getJdbcCodeListBase() {
        JdbcCodeList jdbcCodeList = new JdbcCodeList();
        jdbcCodeList.setDataSource(dataSource);
        jdbcCodeList.setJdbcTemplate(jdbcTemplate);
        jdbcCodeList.setLazyInit(true);
        return jdbcCodeList;
    }

    @Bean("CL_DOC_PUBLIC_SCOPE")
    public EnumCodeList clDocPublicScope() {
        return new EnumCodeList(DocPublicScope.class);
    }

    @Bean("CL_CUSTOMER_PUBLIC")
    public EnumCodeList clCustomerPublic() {
        return new EnumCodeList(CustomerPublic.class);
    }

    @Bean("CL_SAVE_REVISION")
    public EnumCodeList clSaveRevision() {
        return new EnumCodeList(SaveRevision.class);
    }

    @Bean("CL_DOC_TYPE")
    public JdbcCodeList docType() {
        JdbcCodeList jdbcCodeList = getJdbcCodeListBase();
        jdbcCodeList.setQuerySql("SELECT CODE, CONCAT(VALUE1, case when STATUS = '2' then '(無効)' else '' end) as VALUE1 FROM VARIABLE WHERE TYPE = 'DOC_TYPE' ORDER BY STATUS, VALINT1, CODE");
        jdbcCodeList.setValueColumn("CODE");
        jdbcCodeList.setLabelColumn("VALUE1");
        return jdbcCodeList;
    }

    @Bean("CL_DOC_CATEGORY")
    public JdbcCodeList docCategory() {
        JdbcCodeList jdbcCodeList = getJdbcCodeListBase();
        jdbcCodeList.setQuerySql("SELECT CODE, CONCAT(VALUE10, case when STATUS = '2' then '(無効)' else '' end) as VALUE10 FROM VARIABLE WHERE TYPE = 'DOC_CATEGORY' ORDER BY STATUS, VALINT1, CODE");
        jdbcCodeList.setValueColumn("CODE");
        jdbcCodeList.setLabelColumn("VALUE10");
        return jdbcCodeList;
    }

    @Bean("CL_DOC_SERVICE")
    public JdbcCodeList docService() {
        JdbcCodeList jdbcCodeList = getJdbcCodeListBase();
        jdbcCodeList.setQuerySql("SELECT CODE, CONCAT(VALUE10, case when STATUS = '2' then '(無効)' else '' end) as VALUE10 FROM VARIABLE WHERE TYPE = 'DOC_SERVICE' ORDER BY STATUS, VALINT1, CODE");
        jdbcCodeList.setValueColumn("CODE");
        jdbcCodeList.setLabelColumn("VALUE10");
        return jdbcCodeList;
    }

    @Bean("CL_DOC_STAGE")
    public JdbcCodeList docStage() {
        JdbcCodeList jdbcCodeList = getJdbcCodeListBase();
        jdbcCodeList.setQuerySql("SELECT CODE, CONCAT(VALUE1, case when STATUS = '2' then '(無効)' else '' end) as VALUE1 FROM VARIABLE WHERE TYPE = 'DOC_STAGE' ORDER BY STATUS, VALINT1, CODE");
        jdbcCodeList.setValueColumn("CODE");
        jdbcCodeList.setLabelColumn("VALUE1");
        return jdbcCodeList;
    }

}
