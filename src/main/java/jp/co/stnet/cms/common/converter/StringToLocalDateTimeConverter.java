package jp.co.stnet.cms.common.converter;


import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * dozer用 java.lang.String <=> java.time.LocalDateTime の変換
 */
public class StringToLocalDateTimeConverter extends DozerConverter<String, LocalDateTime> {

    static final private String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public StringToLocalDateTimeConverter() {
        super(String.class, LocalDateTime.class);
    }

    @Override
    public LocalDateTime convertTo(String source, LocalDateTime destination) {
        if (source == null) {
            return null;
        }
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    @Override
    public String convertFrom(LocalDateTime source, String destination) {
        if (source == null) {
            return null;
        }
        return source.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
