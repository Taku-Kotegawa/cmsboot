package jp.co.stnet.cms.common.util;

import com.orangesignal.csv.CsvConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CsvUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class getCsvDefault {
        @Test
        @DisplayName("[正] Csvのデフォルト設定が取得できること。設定する値は問わない。")
        void test001() {
            //実行
            CsvConfig actual = CsvUtils.getCsvDefault();

            //検証
            assertThat(actual)
                    .isInstanceOf(CsvConfig.class)
                    .isNotNull();
        }
    }

    @Nested
    class getTsvDefault {
        @Test
        @DisplayName("[正] Tsvのデフォルト設定が取得できること。設定する値は問わない。")
        void test001() {
            //実行
            CsvConfig actual = CsvUtils.getTsvDefault();

            //検証
            assertThat(actual)
                    .isInstanceOf(CsvConfig.class)
                    .isNotNull();
        }
    }

}