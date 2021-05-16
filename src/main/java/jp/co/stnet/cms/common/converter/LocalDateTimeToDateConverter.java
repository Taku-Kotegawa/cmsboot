package jp.co.stnet.cms.common.converter;

import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * dozer用 java.time.LocalDateTime <=> java.util.Date の変換
 */
public class LocalDateTimeToDateConverter extends DozerConverter<LocalDateTime, Date> {

    public LocalDateTimeToDateConverter() {
        super(LocalDateTime.class, Date.class);
    }

    @Override
    public Date convertTo(LocalDateTime source, Date destination) {
        if (source == null) {
            return null;
        }
        ZonedDateTime zdt = source.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    @Override
    public LocalDateTime convertFrom(Date source, LocalDateTime destination) {
        if (source == null) {
            return null;
        }
        return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }

}
