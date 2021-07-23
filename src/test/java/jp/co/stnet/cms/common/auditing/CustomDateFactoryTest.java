package jp.co.stnet.cms.common.auditing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.terasoluna.gfw.common.date.ClassicDateFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomDateFactoryTest {


    @Mock
    ClassicDateFactory dateFactory;

    @InjectMocks
    CustomDateFactory target;

    private final LocalDateTime expectedLocalDateTime = LocalDateTime.of(2001, 12, 31, 23, 59, 59);

    @BeforeEach
    void setUp() {
        // システム時刻の代わりにexpectedLocalDateTimeにセットした時刻を返す。
        when(dateFactory.newDate()).thenReturn(Date.from(expectedLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class newLocalDateTime {

        @Test
        @DisplayName("[正] dateFactoryからLocalDateTimeを取得する。")
        void test001() {
            //準備 -> setUp()参照

            //実行
            LocalDateTime actual = target.newLocalDateTime();

            //検証
            assertThat(actual).isEqualTo(expectedLocalDateTime);
        }
    }

    @Nested
    class newLocalDate {
        @Test
        @DisplayName("[正] dateFactoryからLocalDateを取得する。")
        void test001() {
            //準備 -> setUp()参照

            //実行
            LocalDate actual = target.newLocalDate();

            //検証
            assertThat(actual).isEqualTo(expectedLocalDateTime.toLocalDate());
        }
    }

    @Nested
    class newLocalTime {
        @Test
        @DisplayName("[正] dateFactoryからLocalTimeを取得する。")
        void test001() {
            //準備 -> setUp()参照

            //実行
            LocalTime actual = target.newLocalTime();

            //検証
            assertThat(actual).isEqualTo(expectedLocalDateTime.toLocalTime());
        }
    }

}