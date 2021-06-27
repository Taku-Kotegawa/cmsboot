package jp.co.stnet.cms.example.config;

import jp.co.stnet.cms.example.domain.model.simpleentity.Agreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.terasoluna.gfw.common.codelist.EnumCodeList;
import org.terasoluna.gfw.common.codelist.JdbcCodeList;

import javax.sql.DataSource;

@Configuration
public class ExampleCodeListConfig {

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

    @Bean("CL_EXAMPLE_AGREEMENT")
    public EnumCodeList clYesNo() {
        return new EnumCodeList(Agreement.class);
    }

}
