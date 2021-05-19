package jp.co.stnet.cms.config;

import jp.co.stnet.cms.base.domain.model.common.Status;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

@Configuration
public class CodeListConfig {

    @Bean("CL_STATUS")
    public EnumCodeList clStatus(){
        return new EnumCodeList(Status.class);
    }

}
