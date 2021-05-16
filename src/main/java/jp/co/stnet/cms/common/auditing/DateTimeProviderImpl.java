package jp.co.stnet.cms.common.auditing;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Spring Data JPA の @CreatedDate, @LastModifiedDate のシステム時刻を制御できる様にする設定
 */
public class DateTimeProviderImpl implements DateTimeProvider {

    @Autowired
    CustomDateFactory dateFactory;

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(dateFactory.newLocalDateTime());
    }
}
