package jp.co.stnet.cms.common.util;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static jp.co.stnet.cms.common.util.StringUtils.*;

class StringUtilsTest {


    private final String thisIsAPen = "thisIsAPen";
    private final String ThisIsAPen = "ThisIsAPen";
    private final String this_is_a_pen = "this_is_a_pen";
    private final String THIS_IS_A_PEN = "THIS_IS_A_PEN";
    private final String WHITE_SPACE = "";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class toLowerSnakeCase {
        @Test
        @DisplayName("[正] キャメルケースをスネークケース(小文字)に変換する。(1)")
        void test001() {
            //実行
            String actual = toLowerSnakeCase(thisIsAPen);

            //検証
            assertThat(actual).isEqualTo(this_is_a_pen);
        }

        @Test
        @DisplayName("[正] キャメルケースをスネークケース(小文字)に変換する。(2)")
        void test002() {
            //実行
            String actual = toLowerSnakeCase(ThisIsAPen);

            //検証
            assertThat(actual).isEqualTo(this_is_a_pen);
        }

        @Test
        @DisplayName("[正] nullを代入するとnullが返る。")
        void test003() {
            //実行
            String actual = toLowerSnakeCase(null);

            //検証
            assertThat(actual).isNull();
        }
    }

    @Nested
    class toUpperSnakeCase {
        @Test
        @DisplayName("[正] キャメルケースをスネークケース(大文字)に変換する。(1)")
        void test001() {
            //実行
            String actual = toUpperSnakeCase(thisIsAPen);

            //検証
            assertThat(actual).isEqualTo(THIS_IS_A_PEN);
        }

        @Test
        @DisplayName("[正] キャメルケースをスネークケース(大文字)に変換する。(2)")
        void test002() {
            //実行
            String actual = toUpperSnakeCase(ThisIsAPen);

            //検証
            assertThat(actual).isEqualTo(THIS_IS_A_PEN);
        }

        @Test
        @DisplayName("[正] nullを代入するとnullが返る。")
        void test003() {
            //実行
            String actual = toUpperSnakeCase(null);

            //検証
            assertThat(actual).isNull();
        }
    }

    @Nested
    class toLowerCamelCase {
        @Test
        @DisplayName("[正] スネークケースをキャメルケース(小文字)に変換する。(1)")
        void test001() {
            //実行
            String actual = toLowerCamelCase(this_is_a_pen);

            //検証
            assertThat(actual).isEqualTo(thisIsAPen);
        }

        @Test
        @DisplayName("[正] スネークケースをキャメルケース(小文字)に変換する。(2)")
        void test002() {
            //実行
            String actual = toLowerCamelCase(THIS_IS_A_PEN);

            //検証
            assertThat(actual).isEqualTo(thisIsAPen);
        }

        @Test
        @DisplayName("[正] nullを代入するとnullが返る。")
        void test003() {
            //実行
            String actual = toLowerCamelCase(null);

            //検証
            assertThat(actual).isNull();
        }
    }

    @Nested
    class toUpperCamelCase {
        @Test
        @DisplayName("[正] スネークケースをキャメルケース(大文字)に変換する。(1)")
        void test001() {
            //実行
            String actual = toUpperCamelCase(this_is_a_pen);

            //検証
            assertThat(actual).isEqualTo(ThisIsAPen);
        }

        @Test
        @DisplayName("[正] スネークケースをキャメルケース(大文字)に変換する。(2)")
        void test002() {
            //実行
            String actual = toUpperCamelCase(THIS_IS_A_PEN);

            //検証
            assertThat(actual).isEqualTo(ThisIsAPen);
        }

        @Test
        @DisplayName("[正] nullを代入するとnullが返る。")
        void test003() {
            //実行
            String actual = toUpperCamelCase(null);

            //検証
            assertThat(actual).isNull();
        }
    }

    @Nested
    class nvl {
        @Test
        @DisplayName("[正] 文字列を文字列をで返す。")
        void test001() {
            //実行
            String actual = nvl(THIS_IS_A_PEN);

            //検証
            assertThat(actual).isEqualTo(THIS_IS_A_PEN);
        }

        @Test
        @DisplayName("[正] nullを空白文字で返す。")
        void test002() {
            //準備

            //実行
            String actual = nvl(null);

            //検証
            assertThat(actual).isEqualTo(WHITE_SPACE);
        }
    }

}