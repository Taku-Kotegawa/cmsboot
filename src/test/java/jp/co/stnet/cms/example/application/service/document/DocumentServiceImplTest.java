package jp.co.stnet.cms.example.application.service.document;
import jp.co.stnet.cms.base.application.service.filemanage.FileManagedSharedService;
import jp.co.stnet.cms.base.domain.model.filemanage.FileManaged;

import java.util.*;

import jp.co.stnet.cms.example.domain.model.document.File;

import jp.co.stnet.cms.example.domain.model.document.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.FILE;

@SpringBootTest
@Transactional
class DocumentServiceImplTest {

    @Autowired
    DocumentService target;

    @Autowired
    FileManagedSharedService fileManagedSharedService;

    @PersistenceContext
    EntityManager entityManager;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * エンティティ生成
     * @return
     */
    private Document createEntity() {

        Document document = new Document();

        List<File> files = new ArrayList<>();
        files.add(createFile(1));
        files.add(createFile(2));

        document.setFiles(files);

        return document;
    }

    private File createFile(int i) {
        File file = new File();
        file.setType("typeA");
        file.setFileUuid("fileUuid_" + i);
        file.setPdfUuid("pdfUuid_" + i);

        return file;
    }


    @Test
    void test001() {

        // 準備
        Document expected = createEntity();

        // 実行
        Document saved = target.save(expected);

        // 検証
        assertThat(saved).isNotNull();

    }

    @Test
    void test002() {

        // 準備
        Document expected = target.findById(1L);
        entityManager.detach(expected);
        expected.getFiles().add(createFile(3));

        // 実行
        Document saved = target.save(expected);

        // 検証
        assertThat(saved.getFiles()).size().isEqualTo(3);

    }

    @Test
    void test003() {

        // 準備
        Document expected = target.findById(1L);
        entityManager.detach(expected);
        expected.setFiles(new ArrayList<>());

        // 実行
        Document saved = target.save(expected);

        // 検証
        assertThat(saved.getFiles()).size().isEqualTo(0);

    }

}