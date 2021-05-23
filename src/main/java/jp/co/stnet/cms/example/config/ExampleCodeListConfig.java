package jp.co.stnet.cms.example.config;

import jp.co.stnet.cms.example.domain.model.simpleentity.Agreement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

@Configuration
public class ExampleCodeListConfig {

    @Bean("CL_EXAMPLE_AGREEMENT")
    public EnumCodeList clYesNo() {
        return new EnumCodeList(Agreement.class);
    }

}
