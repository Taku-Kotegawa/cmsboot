package jp.co.stnet.cms.sales.presentation.controller.document;

import jp.co.stnet.cms.base.application.repository.filemanage.FileManagedRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentIndexRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentMaxRevRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRepository;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRevisionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DocumentControllerのテスト
 *
 * 本テストはコントローラ、サービス、リポジトリ、DBを結合するテストである。
 * テスト実行前に関連する全データを削除するため、多量のデータが保存されている場合パフォーマンスが悪化します。
 *
 * テストに必要な環境は以下のとおり。
 * - ローカルDB
 * - ユーザ
 *
 * 必要なユーザとロール
 *  - user1　・・・documentに関するロールを持つ<
 *  - user2　・・・社員、メンバー相当の権限ロールを持つ
 *  - user3　・・・派遣社員相当の権限ロールを持つ
 *  - user4　・・・外部委託社員相当の権限ロールを持つ
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DocumentControllerTest {

    @Autowired
    DocumentController target;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentIndexRepository documentIndexRepository;

    @Autowired
    DocumentRevisionRepository documentRevisionRepository;

    @Autowired
    DocumentMaxRevRepository documentMaxRevRepository;

    @Autowired
    FileManagedRepository fileManagedRepository;

    /**
     * 関連する全テーブルの全件削除
     */
    private void deleteAll() {
        documentIndexRepository.deleteAll();
        documentMaxRevRepository.deleteAll();
        documentRevisionRepository.deleteAll();
        documentRepository.deleteAll();
        fileManagedRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("[正]DIされるべきオブジェクトが準備できている")
    public void contextLoads() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(documentRepository).isNotNull();
        assertThat(documentMaxRevRepository).isNotNull();
        assertThat(documentRevisionRepository).isNotNull();
        assertThat(documentIndexRepository).isNotNull();
        assertThat(fileManagedRepository).isNotNull();
    }


    @Nested
    class view {

        @Test
        @DisplayName("[正]")
        @WithUserDetails("demo")
        void test001() throws Exception {
            mockMvc.perform(get("/sales/document/list"))
                    .andExpect(status().isOk()) //200
                    .andExpect(view().name("sales/document/list"))
                    .andExpect(model().hasNoErrors());
        }


    }
}