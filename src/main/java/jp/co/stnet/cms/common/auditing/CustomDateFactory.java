package jp.co.stnet.cms.common.auditing;

import org.springframework.beans.factory.annotation.Autowired;
import org.terasoluna.gfw.common.date.ClassicDateFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * システム時刻を操作するTerasolunaのdateFactoryライブラリをLocalDateTimeに変換する。
 * <br>
 * (前提条件)「org.terasoluna.gfw.common.date.DefaultClassicDateFactory」を継承したクラスをdateFactoryとしてBean定義する。<br>
 * (参照) TERASOLUNA開発ガイド「システム時刻」参照
 */
public class CustomDateFactory {

    @Autowired
    ClassicDateFactory dateFactory;

    /**
     * dateFactoryを用いてシステム時刻(LocalDateTime)を取得する。
     *
     * @return システム時刻
     */
    public LocalDateTime newLocalDateTime() {
        return LocalDateTime.ofInstant(dateFactory.newDate().toInstant(), ZoneId.systemDefault());
    }

    /**
     * dateFactoryを用いてシステム日付(LocalDate)を取得する。
     *
     * @return システム日付
     */
    public LocalDate newLocalDate() {
        return newLocalDateTime().toLocalDate();
    }

    /**
     * dateFactoryを用いてシステム時刻(LocalTime)を取得する。
     *
     * @return システム時刻
     */
    public LocalTime newLocalTime() {
        return newLocalDateTime().toLocalTime();
    }

}
