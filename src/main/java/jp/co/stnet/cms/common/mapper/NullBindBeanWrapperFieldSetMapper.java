package jp.co.stnet.cms.common.mapper;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.DataBinder;

public class NullBindBeanWrapperFieldSetMapper extends BeanWrapperFieldSetMapper {

    @Override
    protected void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
