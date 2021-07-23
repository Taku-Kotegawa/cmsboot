package jp.co.stnet.cms.common.util;

import jp.co.stnet.cms.base.domain.model.common.Status;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BeanUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class getFields {
        
        @Test
        @DisplayName("[正] フィールドを持たないクラスの場合、空のMapを返す。")
        void test001() {
            // 準備
            Map<String, String> expected = new HashMap<>();

            // 実行
            Map<String, String> actual = BeanUtils.getFields(SampleModel01.class, "");

            // 検証
            assertThat(actual).isEqualTo(expected);
            log.info("actual : " + actual.toString());
        }

        @Test
        @DisplayName("[正] 基本的な型のフィールドを持つクラスは、フィールド名とクラスを格納したMapを返す。")
        void test002() {
            // 準備
            Map<String, String> expected = new HashMap<>();
            expected.put("field1", "int");
            expected.put("field2", "boolean");
            expected.put("field3", "java.lang.String");
            expected.put("field4", "java.lang.Integer");

            // 実行
            Map<String, String> actual = BeanUtils.getFields(SampleModel02.class, "");

            // 検証
            assertThat(actual).isEqualTo(expected);
            log.info("actual : " + actual.toString());
        }

        @Test
        @DisplayName("[正] getterを持たない場合、空のMapを返す。")
        void test003() {
            // 準備
            Map<String, String> expected = new HashMap<>();

            // 実行
            Map<String, String> actual = BeanUtils.getFields(SampleModel03.class, "");

            // 検証
            assertThat(actual).isEqualTo(expected);
            log.info("actual : " + actual.toString());
        }

        @Test
        @DisplayName("[正] LIST, MAP, ENUM のフィールドの場合、フィールド名とクラスを格納したMapを返す。")
        void test004() {
            // 準備
            Map<String, String> expected = new HashMap<>();
            expected.put("field1", "java.util.List");
            expected.put("field2", "java.util.Map");
            expected.put("field3", "jp.co.stnet.cms.base.domain.model.common.Status");

            // 実行
            Map<String, String> actual = BeanUtils.getFields(SampleModel04.class, "");

            // 検証
            assertThat(actual).isEqualTo(expected);
            log.info("actual : " + actual.toString());
        }

        @Test
        @DisplayName("[正] 他のクラスをフィールドに持つ場合、フィールドに指定したクラス内のフィールドとクラスもMapに格納される。")
        void test005() {
            // 準備
            Map<String, String> expected = new HashMap<>();
            expected.put("field1", "jp.co.stnet.cms.common.util.BeanUtilsTest$SampleModel02");
            expected.put("field1_field1", "int");
            expected.put("field1_field2", "boolean");
            expected.put("field1_field3", "java.lang.String");
            expected.put("field1_field4", "java.lang.Integer");

            // 実行
            Map<String, String> actual = BeanUtils.getFields(SampleModel05.class, "");

            // 検証
            assertThat(actual).isEqualTo(expected);
            log.info("actual : " + actual.toString());
        }

        @Test
        @DisplayName("[正] 他のクラスのLIST, MAPのフィールドの場合、他のクラスのフィールドは取得しない。(TODO:他のクラスのフィールドも取得できる様にしたい)")
        void test006() {
            // 準備
            Map<String, String> expected = new HashMap<>();
            expected.put("field1", "java.util.List");
            expected.put("field1_field1", "int");
            expected.put("field1_field2", "boolean");
            expected.put("field1_field3", "java.lang.String");
            expected.put("field1_field4", "java.lang.Integer");

            // 実行
            Map<String, String> actual = BeanUtils.getFields(SampleModel06.class, "");

            // 検証
            assertThat(actual).isEqualTo(expected);
            log.info("actual : " + actual.toString());
        }        
        
    }

    @Nested
    class getFieldList {
        @Test
        @DisplayName("[正] getFieldsのMapのキーがリストに変換される。過不足・重複は認めない。並び順は問わない。")
        void test001() {
            // 準備

            // 実行
            List<String> actual = BeanUtils.getFieldList(SampleModel05.class);

            // 検証
            // 並び順は問わず、期待値が必ず含まれており、重複していないこと。
            assertThat(actual).containsOnlyOnce("field1", "field1_field1", "field1_field2", "field1_field3", "field1_field4");
        }
    }

    @Nested
    class getFieldByAnnotation {

        @Test
        @DisplayName("[正] @NotNullアノテーションを設定したフィールドの一覧が取得できる。(2件)")
        void test001() {
            // 準備

            // 実行
            Map<String, Annotation> actual = BeanUtils
                    .getFieldByAnnotation(SampleModel07.class, "", NotNull.class);

            // 検証
            assertThat(actual).size().isEqualTo(2);
        }

        @Test
        @DisplayName("[正] @Lengthアノテーションを設定したフィールドの一覧が取得できる。(1件)")
        void test002() {
            // 準備

            // 実行
            Map<String, Annotation> actual = BeanUtils
                    .getFieldByAnnotation(SampleModel07.class, "", Length.class);

            // 検証
            assertThat(actual).size().isEqualTo(1);
        }

        @Test
        @DisplayName("[正] 存在しないアノテーションの場合は空のリストが取得できる。(0件)")
        void test003() {
            // 準備

            // 実行
            Map<String, Annotation> actual = BeanUtils
                    .getFieldByAnnotation(SampleModel07.class, "", Max.class);

            // 検証
            assertThat(actual).isEmpty();
        }
        
    }

    @Nested
    class getSignature {
    }

    // -----------------------------------------------------------------------------------------------------------------
    
    /**
     * 準備: フィールドを持たないクラス
     */
    @Data
    class SampleModel01 {
    }

    /**
     * 基本的なクラスのフィールドを持つ
     */
    @Data
    class SampleModel02 {
        private int field1;
        private boolean field2;
        private String field3;
        private Integer field4;
    }

    /**
     * getter を持たない
     */
    class SampleModel03 {
        private int field1;
        private boolean field2;
        private String field3;
        private Integer field4;
    }

    /**
     * LIST, MAP, ENUM など
     */
    @Data
    class SampleModel04 {
        private List<String> field1;
        private Map<String, String> field2;
        private Status field3;
    }

    /**
     * 他のクラスをフィールドに持つ場合
     */
    @Data
    class SampleModel05 {
        private SampleModel02 field1;
    }

    /**
     * 他のモデルの List を持つ場合
     */
    @Data
    class SampleModel06 {
        private List<SampleModel02> field1;
    }
    
    /**
     * Annotation が設定されたクラス
     */
    @Data
    class SampleModel07 {
        @NotNull
        private int field1;
        @NotNull
        private boolean field2;
        @Length(min = 3)
        private String field3;
        private Integer field4;
    }    
    
}