package jp.co.stnet.cms.common.converter;

import com.github.dozermapper.core.DozerConverter;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.MapperAware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListToStringConverter extends DozerConverter<List, String> implements MapperAware {

    private Mapper mapper;

    public ListToStringConverter() {
        super(List.class, String.class);
    }

    @Override
    public String convertTo(List source, String destination) {
        if (source == null) {
            return null;
        }

        if (source.size() > 0 && source.get(0).getClass() == Integer.class) {
            List<String> s = new ArrayList<>();
            List<Integer> copy = (List<Integer>) source;
            for (Integer i : copy) {
                s.add(i.toString());
            }
            return String.join(",", s);
        }

        return String.join(",", (List<String>) source);
    }

    @Override
    public List convertFrom(String source, List destination) {
        if (source == null) {
            return null;
        }
        return Arrays.asList(source.split(",", 0));
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }
}
