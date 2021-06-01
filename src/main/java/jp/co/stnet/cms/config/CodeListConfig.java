package jp.co.stnet.cms.config;

import jp.co.stnet.cms.base.domain.model.authentication.Permission;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import jp.co.stnet.cms.base.domain.model.common.Status;
import jp.co.stnet.cms.base.domain.model.common.YesNo;
import jp.co.stnet.cms.base.domain.model.filemanage.FileStatus;
import jp.co.stnet.cms.base.domain.model.filemanage.FileType;
import jp.co.stnet.cms.base.domain.model.variable.VariableType;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.terasoluna.gfw.common.codelist.EnumCodeList;

import javax.sql.DataSource;

@Configuration
public class CodeListConfig {

    @Bean("CL_STATUS")
    public EnumCodeList status() {
        return new EnumCodeList(Status.class);
    }

    @Bean("CL_YESNO")
    public EnumCodeList yesNo() {
        return new EnumCodeList(YesNo.class);
    }

    @Bean("CL_FILESTATUS")
    public EnumCodeList fileStatus() { return new EnumCodeList(FileStatus.class); }

    @Bean("CL_VARIABLETYPE")
    public EnumCodeList variableType() { return new EnumCodeList(VariableType.class); }

    @Bean("CL_ROLE")
    public EnumCodeList role() { return new EnumCodeList(Role.class); }

    @Bean("CL_PERMISSION")
    public EnumCodeList permission() { return new EnumCodeList(Permission.class); }

    @Bean("CL_FILETYPE")
    public EnumCodeList fileType() { return new EnumCodeList(FileType.class); }

}
