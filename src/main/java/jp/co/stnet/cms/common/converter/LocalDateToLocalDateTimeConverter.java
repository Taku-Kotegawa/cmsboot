package jp.co.stnet.cms.common.converter;

import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * dozer用 java.time.LocalDate <=> java.time.LocalDateTime の変換
 */
public class LocalDateToLocalDateTimeConverter extends DozerConverter<LocalDate, LocalDateTime> {

    public LocalDateToLocalDateTimeConverter() {
        super(LocalDate.class, LocalDateTime.class);
    }

    @Override
    public LocalDateTime convertTo(LocalDate source, LocalDateTime destination) {
        if (source == null) {
            return null;
        }
        return source.atStartOfDay();
    }

    @Override
    public LocalDate convertFrom(LocalDateTime source, LocalDate destination) {
        if (source == null) {
            return null;
        }
        return source.toLocalDate();
    }

}
