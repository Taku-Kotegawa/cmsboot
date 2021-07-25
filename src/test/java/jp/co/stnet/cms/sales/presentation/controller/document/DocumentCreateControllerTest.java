package jp.co.stnet.cms.sales.presentation.controller.document;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DocumentCreateControllerTest {

    @Autowired
    DocumentCreateController target;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * POSTデータの準備
     *
     * @param id 主キーに設定する値
     * @return Map
     */
    private MultiValueMap makePostData(String id) {
        MultiValueMap<String, String> post = new LinkedMultiValueMap<>();
        post.add("title", rightPad(id, 255, "0"));
        post.add("docCategory1", rightPad(id, 255, "0"));
        post.add("docCategory2", rightPad(id, 255, "0"));
        post.add("docService1", rightPad(id, 255, "0"));
        post.add("docService2", rightPad(id, 255, "0"));
        post.add("departmentForCreation", rightPad(id, 255, "0"));
        post.add("chargePersonForCreation", rightPad(id, 255, "0"));
        return post;
    }


    private MultiValueMap makePostDataFull(String id) {
        MultiValueMap<String, String> post = makePostData(id);
        post.add("saveDraft","TRUE");
        post.add("reasonForChange",rightPad(id, 255, "0"));
        post.add("documentNumber",rightPad(id, 255, "0"));
        post.add("versionNumber",rightPad(id, 255, "0"));
        post.add("docService3",rightPad(id, 255, "0"));
        post.add("customerPublic","0");
        post.add("useStage","100");
        post.add("_useStage","on");
        post.add("files[0].fileMemo",rightPad(id, 255, "0"));
        post.add("body",rightPad(id, 255, "0"));
        post.add("summernote-files","");
        post.add("remark",rightPad(id, 255, "0"));
        post.add("departmentForPublish",rightPad(id, 255, "0"));
        post.add("chargePersonForPublish",rightPad(id, 255, "0"));
        post.add("responsiblePersonForPublish",rightPad(id, 255, "0"));
        post.add("publishedDate","2021/12/31");
        post.add("lastRevisedDate","2021/12/31");
        post.add("invalidationDate","2021/12/31");
        post.add("announceDate","2021/12/31");
        post.add("publicScope","99");
        return post;
    }


    @Test
    @DisplayName("[正]DIされるべきオブジェクトが準備できている")
    public void contextLoads() throws Exception {
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Nested
    class createForm {

        @Test
        @DisplayName("[正]")
        @WithUserDetails("demo")
        void test001() throws Exception {
            mockMvc.perform(get("/sales/document/create?form"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("sales/document/form"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("[異]")
        @WithUserDetails("1234")
        void test002() throws Exception {
            mockMvc.perform(get("/sales/document/create?form"))
                    .andExpect(status().is4xxClientError());
        }

    }

    @Nested
    class create {

        @Test
        @DisplayName("[正] 必要最低限の項目を設定してデータを登録する")
        @WithUserDetails("demo")
        void test001() throws Exception {
            // 実行
            MvcResult result = mockMvc.perform(post("/sales/document/create")
                    .with(csrf()).params(makePostData("1")))

                    // 検証
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("/sales/document/*/update?form"))
                    .andExpect(model().hasNoErrors())
                    .andReturn();
        }

        @Test
        @DisplayName("[正] 全項目を設定してデータを登録する")
        @WithUserDetails("demo")
        void test002() throws Exception {
            // 実行
            MvcResult result = mockMvc.perform(post("/sales/document/create")
                    .with(csrf()).params(makePostData("1")))

                    // 検証
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrlPattern("/sales/document/*/update?form"))
                    .andExpect(model().hasNoErrors())
                    .andReturn();
        }

        @Test
        @DisplayName("[異] 必須チェックが動作する")
        @WithUserDetails("demo")
        void test101() throws Exception {
            // 準備
            MultiValueMap<String, String> post = new LinkedMultiValueMap<>();

            // 実行
            MvcResult result = mockMvc.perform(post("/sales/document/create")
                    .with(csrf()).params(post))

                    // 検証
                    .andExpect(status().isOk())
                    .andExpect(model().hasErrors())
                    .andExpect(view().name("sales/document/form"))
                    .andExpect(model().attributeHasFieldErrors("documentForm",
                            "title", "docCategory1", "docCategory2", "docService1",
                            "docService2", "departmentForCreation", "chargePersonForCreation"))
                    .andReturn();

// エラーが発生したフィールド、エラーメッセージを取り出すサンプルコード
//            BindingResult bindingResult = (BindingResult) result.getModelAndView().getModel().get(BindingResult.MODEL_KEY_PREFIX + "documentForm");
//            System.out.println(bindingResult.getErrorCount());
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                System.out.println(((DefaultMessageSourceResolvable) error.getArguments()[0]).getDefaultMessage() + " " + error.getDefaultMessage());
//            }
//            System.out.println(bindingResult.getFieldError("docService2").getField());
//            System.out.println(bindingResult.getFieldError("docService2").getDefaultMessage());
        }




    }

    @Nested
    class createAddLineItem {
    }

    @Nested
    class docCategory2Json {
    }

    @Nested
    class docService2Json {
    }

    @Nested
    class docService3Json {
    }
}