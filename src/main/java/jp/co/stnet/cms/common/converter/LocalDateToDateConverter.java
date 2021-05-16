package jp.co.stnet.cms.common.converter;

import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * dozer用 java.time.LocalDateTime <=> java.util.Date の変換
 */
public class LocalDateToDateConverter extends DozerConverter<LocalDate, Date> {

    public LocalDateToDateConverter() {
        super(LocalDate.class, Date.class);
    }

    @Override
    public Date convertTo(LocalDate source, Date destination) {
        if (source == null) {
            return null;
        }
        return Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public LocalDate convertFrom(Date source, LocalDate destination) {
        if (source == null) {
            return null;
        }
        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
