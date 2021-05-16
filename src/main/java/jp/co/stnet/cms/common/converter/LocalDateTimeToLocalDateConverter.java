package jp.co.stnet.cms.common.converter;

import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * dozer用 java.time.LocalDateTime <=> java.time.LocalDate の変換
 */
public class LocalDateTimeToLocalDateConverter extends DozerConverter<LocalDateTime, LocalDate> {

    public LocalDateTimeToLocalDateConverter() {
        super(LocalDateTime.class, LocalDate.class);
    }

    @Override
    public LocalDate convertTo(LocalDateTime source, LocalDate destination) {
        if (source == null) {
            return null;
        }
        return source.toLocalDate();
    }

    @Override
    public LocalDateTime convertFrom(LocalDate source, LocalDateTime destination) {
        if (source == null) {
            return null;
        }
        return source.atStartOfDay();
    }

}
