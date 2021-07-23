package jp.co.stnet.cms.common.util;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MimeTypesTest {

    @Nested
    class getMimeType {
        @Test
        @DisplayName("[正] 拡張子からMimeTypeを取得する。")
        void test001() {
            //準備
            String extension = "xlsx";
            String expected = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            //実行
            String actual = MimeTypes.getMimeType(extension);

            //検証
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("[異] 拡張子からMimeTypeを取得する。存在しない場合は'application/octet-stream'が返る")
        void test002() {
            //準備
            String extension = "not exist";
            String expected = "application/octet-stream";

            //実行
            String actual = MimeTypes.getMimeType(extension);

            //検証
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    class getMediaType {
        @Test
        @DisplayName("[正] 拡張子からMediaTypeを取得する。")
        void test001() {
            //準備
            String extension = "xlsx";
            MediaType expected = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            //実行
            MediaType actual = MimeTypes.getMediaType(extension);

            //検証
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("[異] 拡張子からMediaTypeを取得する。存在しない場合は'application/octet-stream'のMediaTypeが返る")
        void test002() {
            //準備
            String extension = "not exist";
            String expectedMimeType = "application/octet-stream";
            MediaType expected = MediaType.parseMediaType(expectedMimeType);

            //実行
            MediaType actual = MimeTypes.getMediaType(extension);

            //検証
            assertThat(actual).isEqualTo(expected);
        }
    }
}