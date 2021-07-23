package jp.co.stnet.cms.common.util;

import lombok.Data;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StateMapTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Data
    class Example {
        String field1;
        Integer field2;
    }

    @Nested
    class Constructor {

        @Test
        @DisplayName("[正] 全ての引数に値をセットすると例外は発生しない。")
        void test001() {
            //実行
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //検証
            assertThat(actual).isInstanceOf(StateMap.class);
        }

        @Test
        @DisplayName("[異] 第１引数がnullの場合、例外を投げる。")
        void test002() {
            assertThatThrownBy(() -> {
                //実行
                StateMap actual = new StateMap(null, null, null);
            })
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("clazz must not be null.");
        }

        @Test
        @DisplayName("[異] 第２引数がnullの場合、例外を投げる。")
        void test003() {
            assertThatThrownBy(() -> {
                //実行
                StateMap actual = new StateMap(Example.class, null, null);
            })
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("includeKeys must not be null.");
        }

        @Test
        @DisplayName("[異] 第３引数がnullの場合、例外を投げる。")
        void test004() {
            assertThatThrownBy(() -> {
                //実行
                StateMap actual = new StateMap(Example.class, new ArrayList<>(), null);
            })
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("excludeKeys must not be null.");
        }

        @Test
        @DisplayName("[正] 第１引数に指定したクラスのフィールドが読み取られる。")
        void test006() {
            //準備
            String fieldName = "field1";
            List<String> includeKeys = new ArrayList<>();
            includeKeys.add(fieldName);

            //実行
            StateMap actual = new StateMap(Example.class, includeKeys, new ArrayList<>());

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__label", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false)
                    .size().isEqualTo(12);
        }

        @Test
        @DisplayName("[正] 第２引数でフィールドの追加ができる。")
        void test007() {
            //準備
            String fieldName = "addField";
            List<String> includeKeys = new ArrayList<>();
            includeKeys.add(fieldName);

            //実行
            StateMap actual = new StateMap(Example.class, includeKeys, new ArrayList<>());

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__label", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);

        }

        @Test
        @DisplayName("[正] 第３引数でフィールドの削除ができる。")
        void test008() {
            //準備
            String fieldName = "field1";
            List<String> excludeKeys = new ArrayList<>();
            excludeKeys.add(fieldName);

            //実行
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), excludeKeys);

            //検証
            assertThat(actual.asMap()).size().isEqualTo(6);

        }

    }


    @Nested
    class setInputTrue {

        @Test
        @DisplayName("[正] 指定したフィールドの__inputがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";

            //実行
            actual = actual.setInputTrue(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", true) //<- true に変更される。
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            assertThatThrownBy(() -> {
                //実行
                actual.setInputTrue("notExist");
            })
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__input not found");
        }

    }

    @Nested
    class setInputFalse {

        @Test
        @DisplayName("[正] 指定したフィールドの__inputがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";
            actual = actual.setInputTrue(fieldName);

            //実行
            actual = actual.setInputFalse(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false) //<- false に変更される。
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setInputFalse("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__input not found");
        }

    }

    @Nested
    class setDisabledTrue {

        @Test
        @DisplayName("[正] 指定したフィールドの__disabledがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";

            //実行
            actual = actual.setDisabledTrue(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", true) //<- true に変更される。
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setDisabledTrue("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__disabled not found");
        }
    }

    @Nested
    class setDisabledFalse {

        @Test
        @DisplayName("[正] 指定したフィールドの__disabledがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";
            actual = actual.setDisabledTrue(fieldName);

            //実行
            actual = actual.setDisabledFalse(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false) //<- false に変更される。
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setDisabledFalse("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__disabled not found");
        }

    }

    @Nested
    class setReadOnlyTrue {
        @Test
        @DisplayName("[正] 指定したフィールドの__readonlyがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";

            //実行
            actual = actual.setReadOnlyTrue(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", true) //<- true に変更される。
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setReadOnlyTrue("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__readonly not found");
        }
    }

    @Nested
    class setReadOnlyFalse {
        @Test
        @DisplayName("[正] 指定したフィールドの__readonlyがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";
            actual = actual.setReadOnlyTrue(fieldName);

            //実行
            actual = actual.setReadOnlyFalse(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false) //<- false に変更される。
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setReadOnlyFalse("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__readonly not found");
        }

    }

    @Nested
    class setHiddenTrue {
        @Test
        @DisplayName("[正] 指定したフィールドの__hiddenがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";

            //実行
            actual = actual.setHiddenTrue(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", true);  //<- true に変更される。
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            assertThatThrownBy(() -> {
                //実行
                actual.setHiddenTrue("notExist");
            })
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__hidden not found");
        }
    }

    @Nested
    class setHiddenFalse {
        @Test
        @DisplayName("[正] 指定したフィールドの__hiddenがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";
            actual = actual.setHiddenTrue(fieldName);

            //実行
            actual = actual.setHiddenFalse(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false)
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false); //<- false に変更される。
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setHiddenFalse("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__hidden not found");
        }
    }

    @Nested
    class setViewTrue {
        @Test
        @DisplayName("[正] 指定したフィールドの__viewがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";

            //実行
            actual = actual.setViewTrue(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", true) //<- true に変更される。
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            assertThatThrownBy(() -> {
                //実行
                actual.setViewTrue("notExist");
            })
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__view not found");
        }
    }

    @Nested
    class setViewFalse {
        @Test
        @DisplayName("[正] 指定したフィールドの__viewがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            String fieldName = "field1";
            actual = actual.setViewTrue(fieldName);

            //実行
            actual = actual.setViewFalse(fieldName);

            //検証
            assertThat(actual.asMap())
                    .containsEntry(fieldName + "__view", false) //<- false に変更される。
                    .containsEntry(fieldName + "__input", false)
                    .containsEntry(fieldName + "__disabled", false)
                    .containsEntry(fieldName + "__readonly", false)
                    .containsEntry(fieldName + "__hidden", false);
        }

        @Test
        @DisplayName("[異] 登録されていないフィールド名を指定すると例外をスローする。")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            assertThatThrownBy(() -> actual.setViewFalse("notExist"))
                    //検証
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("notExist__view not found");
        }
    }

    @Nested
    class setDisabledTrueAll {
        @Test
        @DisplayName("[正] 全フィールドの__disabledがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            actual = actual.setDisabledTrueAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", false)
                    .containsEntry("field1__input", false)
                    .containsEntry("field1__disabled", true)  //<-変更される箇所
                    .containsEntry("field1__readonly", false)
                    .containsEntry("field1__hidden", false)
                    .containsEntry("field2__view", false)
                    .containsEntry("field2__input", false)
                    .containsEntry("field2__disabled", true) //<-変更される箇所
                    .containsEntry("field2__readonly", false)
                    .containsEntry("field2__hidden", false);
        }
    }

    @Nested
    class setDisabledFalseAll {
        @Test
        @DisplayName("[正] 全フィールドの__disabledがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            actual = actual.setDisabledTrueAll();
            actual = actual.setReadOnlyTrueAll();
            actual = actual.setInputTrueAll();
            actual = actual.setHiddenTrueAll();
            actual = actual.setViewTrueAll();

            //実行
            actual = actual.setDisabledFalseAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", true)
                    .containsEntry("field1__input", true)
                    .containsEntry("field1__disabled", false)  //<-変更される箇所
                    .containsEntry("field1__readonly", true)
                    .containsEntry("field1__hidden", true)
                    .containsEntry("field2__view", true)
                    .containsEntry("field2__input", true)
                    .containsEntry("field2__disabled", false) //<-変更される箇所
                    .containsEntry("field2__readonly", true)
                    .containsEntry("field2__hidden", true);
        }
    }

    @Nested
    class setReadOnlyTrueAll {
        @Test
        @DisplayName("[正] 全フィールドの__readonlyがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            actual = actual.setReadOnlyTrueAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", false)
                    .containsEntry("field1__input", false)
                    .containsEntry("field1__disabled", false)
                    .containsEntry("field1__readonly", true) //<-変更される箇所
                    .containsEntry("field1__hidden", false)
                    .containsEntry("field2__view", false)
                    .containsEntry("field2__input", false)
                    .containsEntry("field2__disabled", false)
                    .containsEntry("field2__readonly", true) //<-変更される箇所
                    .containsEntry("field2__hidden", false);
        }
    }

    @Nested
    class setReadOnlyFalseAll {
        @Test
        @DisplayName("[正] 全フィールドの__readonlyがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            actual = actual.setDisabledTrueAll();
            actual = actual.setReadOnlyTrueAll();
            actual = actual.setInputTrueAll();
            actual = actual.setHiddenTrueAll();
            actual = actual.setViewTrueAll();

            //実行
            actual = actual.setReadOnlyFalseAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", true)
                    .containsEntry("field1__input", true)
                    .containsEntry("field1__disabled", true)
                    .containsEntry("field1__readonly", false) //<-変更される箇所
                    .containsEntry("field1__hidden", true)
                    .containsEntry("field2__view", true)
                    .containsEntry("field2__input", true)
                    .containsEntry("field2__disabled", true)
                    .containsEntry("field2__readonly", false) //<-変更される箇所
                    .containsEntry("field2__hidden", true);
        }
    }

    @Nested
    class setHiddenTrueAll {
        @Test
        @DisplayName("[正] 全フィールドの__hiddenがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            actual = actual.setHiddenTrueAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", false)
                    .containsEntry("field1__input", false)
                    .containsEntry("field1__disabled", false)
                    .containsEntry("field1__readonly", false)
                    .containsEntry("field1__hidden", true) //<-変更される箇所
                    .containsEntry("field2__view", false)
                    .containsEntry("field2__input", false)
                    .containsEntry("field2__disabled", false)
                    .containsEntry("field2__readonly", false)
                    .containsEntry("field2__hidden", true); //<-変更される箇所
        }
    }

    @Nested
    class setHiddenFalseAll {
        @Test
        @DisplayName("[正] 全フィールドの__hiddenがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            actual = actual.setDisabledTrueAll();
            actual = actual.setReadOnlyTrueAll();
            actual = actual.setInputTrueAll();
            actual = actual.setHiddenTrueAll();
            actual = actual.setViewTrueAll();

            //実行
            actual = actual.setHiddenFalseAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", true)
                    .containsEntry("field1__input", true)
                    .containsEntry("field1__disabled", true)
                    .containsEntry("field1__readonly", true)
                    .containsEntry("field1__hidden", false) //<-変更される箇所
                    .containsEntry("field2__view", true)
                    .containsEntry("field2__input", true)
                    .containsEntry("field2__disabled", true)
                    .containsEntry("field2__readonly", true)
                    .containsEntry("field2__hidden", false); //<-変更される箇所
        }
    }

    @Nested
    class setInputTrueAll {
        @Test
        @DisplayName("[正] 全フィールドの__inputがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            actual = actual.setInputTrueAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", false)
                    .containsEntry("field1__input", true) //<-変更される箇所
                    .containsEntry("field1__disabled", false)
                    .containsEntry("field1__readonly", false)
                    .containsEntry("field1__hidden", false)
                    .containsEntry("field2__view", false)
                    .containsEntry("field2__input", true) //<-変更される箇所
                    .containsEntry("field2__disabled", false)
                    .containsEntry("field2__readonly", false)
                    .containsEntry("field2__hidden", false);
        }
    }

    @Nested
    class setInputFalseAll {
        @Test
        @DisplayName("[正] 全フィールドの__inputがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            actual = actual.setDisabledTrueAll();
            actual = actual.setReadOnlyTrueAll();
            actual = actual.setInputTrueAll();
            actual = actual.setHiddenTrueAll();
            actual = actual.setViewTrueAll();

            //実行
            actual = actual.setInputFalseAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", true)
                    .containsEntry("field1__input", false) //<-変更される箇所
                    .containsEntry("field1__disabled", true)
                    .containsEntry("field1__readonly", true)
                    .containsEntry("field1__hidden", true)
                    .containsEntry("field2__view", true)
                    .containsEntry("field2__input", false) //<-変更される箇所
                    .containsEntry("field2__disabled", true)
                    .containsEntry("field2__readonly", true)
                    .containsEntry("field2__hidden", true);
        }
    }

    @Nested
    class setViewTrueAll {
        @Test
        @DisplayName("[正] 全フィールドの__viewがtrueに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            actual = actual.setViewTrueAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", true)//<-変更される箇所
                    .containsEntry("field1__input", false)
                    .containsEntry("field1__disabled", false)
                    .containsEntry("field1__readonly", false)
                    .containsEntry("field1__hidden", false)
                    .containsEntry("field2__view", true) //<-変更される箇所
                    .containsEntry("field2__input", false)
                    .containsEntry("field2__disabled", false)
                    .containsEntry("field2__readonly", false)
                    .containsEntry("field2__hidden", false);
        }
    }

    @Nested
    class setViewFalseAll {
        @Test
        @DisplayName("[正] 全フィールドの__viewがfalseに変更する。")
        void test001() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());
            actual = actual.setDisabledTrueAll();
            actual = actual.setReadOnlyTrueAll();
            actual = actual.setInputTrueAll();
            actual = actual.setHiddenTrueAll();
            actual = actual.setViewTrueAll();

            //実行
            actual = actual.setViewFalseAll();

            //検証
            assertThat(actual.asMap())
                    .containsEntry("field1__view", false) //<-変更される箇所
                    .containsEntry("field1__input", true)
                    .containsEntry("field1__disabled", true)
                    .containsEntry("field1__readonly", true)
                    .containsEntry("field1__hidden", true)
                    .containsEntry("field2__view", false) //<-変更される箇所
                    .containsEntry("field2__input", true)
                    .containsEntry("field2__disabled", true)
                    .containsEntry("field2__readonly", true)
                    .containsEntry("field2__hidden", true);
        }
    }

    @Nested
    class addKey {
        @Test
        @DisplayName("[正] フィールドを追加できる。(1)")
        void test001() {
            //準備
            List<String> excludekeys = new ArrayList<>();
            excludekeys.add("field1");
            excludekeys.add("field2");
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), excludekeys);

            //実行
            actual = actual.addField("addField");

            //検証
            assertThat(actual.asMap())
                    .containsEntry("addField__view", false)
                    .containsEntry("addField__label", false)
                    .containsEntry("addField__input", false)
                    .containsEntry("addField__disabled", false)
                    .containsEntry("addField__readonly", false)
                    .containsEntry("addField__hidden", false)
                    .size().isEqualTo(6);
        }

        @Test
        @DisplayName("[正] フィールドを追加できる。(2)")
        void test002() {
            //準備
            StateMap actual = new StateMap(Example.class, new ArrayList<>(), new ArrayList<>());

            //実行
            actual = actual.addField("addField");

            //検証
            assertThat(actual.asMap())
                    .containsEntry("addField__view", false)
                    .containsEntry("addField__label", false)
                    .containsEntry("addField__input", false)
                    .containsEntry("addField__disabled", false)
                    .containsEntry("addField__readonly", false)
                    .containsEntry("addField__hidden", false)
                    .size().isEqualTo(18);
        }

    }

}