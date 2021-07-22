package jp.co.stnet.cms.common.converter;

import com.github.dozermapper.core.DozerConverter;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MapperAware;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetToStringConverter extends DozerConverter<Set, String> implements MapperAware {

    private Mapper mapper;

    public SetToStringConverter() {
        super(Set.class, String.class);
    }


    @Override
    public String convertTo(Set source, String destination) {
        if (source == null) {
            return null;
        }

        return String.join(",", (Set<String>) source);
    }

    @Override
    public Set convertFrom(String source, Set destination) {
        if (source == null) {
            return null;
        }
        return new HashSet(Arrays.asList(source.split(",", 0)));
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
