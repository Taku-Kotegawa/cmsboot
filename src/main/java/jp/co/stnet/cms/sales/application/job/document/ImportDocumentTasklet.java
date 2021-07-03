package jp.co.stnet.cms.sales.application.job.document;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.common.batch.ReaderFactory;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.sales.application.repository.document.DocumentRepository;
import jp.co.stnet.cms.sales.application.service.document.DocumentService;
import jp.co.stnet.cms.sales.domain.model.document.Document;
import jp.co.stnet.cms.sales.domain.model.document.DocumentCsvBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import javax.validation.ValidationException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import static java.lang.String.format;

@Component
public class ImportDocumentTasklet implements Tasklet {

    // 専用のJOBログ用のlogger
    private static final Logger log = LoggerFactory.getLogger("JobLogger");

    // インポートファイルのカラム定義
    private final String[] columns = {"id","status","statusLabel","title","docCategory","docCategoryValue1","docCategoryValue2","docCategoryValue3","docService","docServiceValue1","docServiceValue2","docServiceValue3","body","documentNumber", "chargeDepartment","chargePerson","enactmentDate","lastRevisedDate","implementationDate","enactmentDepartment","reasonForChange","useStage","useStageLabel","intendedReader","summary","fileTypeLabel","filesLabel","pdfFilesLabel","publicScope","publicScopeLabel","customerPublic","customerPublicLabel","lastModifiedBy","lastModifiedByLabel","lastModifiedDate"};

    @Autowired
    SmartValidator smartValidator;

    @Autowired
    Mapper beanMapper;

    @Autowired
    CustomDateFactory dateFactory;

    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentRepository documentRepository;


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        Long jobInstanceId = chunkContext.getStepContext().getJobInstanceId();
        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();
        String jobName = chunkContext.getStepContext().getJobName();
        String fileType = stepContribution.getStepExecution().getJobParameters().getString("filetype");
        String inputFile = stepContribution.getStepExecution().getJobParameters().getString("inputFile");
        String encoding = stepContribution.getStepExecution().getJobParameters().getString("encoding");

        MDC.put("jobInstanceId", jobInstanceId.toString());
        MDC.put("jobName", jobName);
        MDC.put("jobExecutionId", jobExecutionId.toString());
        MDC.put("jobName_jobExecutionId", jobName + "_" + jobExecutionId.toString());


        // DB操作時の例外発生の有無を記録する
        boolean hasDBAccessException = false;

        // CSVファイルを１行を格納するBean
        DocumentCsvBean csvLine = null;

        // CSVファイルの入力チェック結果を格納するBindingResult
        BindingResult result = new BeanPropertyBindingResult(csvLine, "csvLine");

        // 選択したフォーマット用のReader取得
        ItemStreamReader<DocumentCsvBean> fileReader
                = new ReaderFactory<DocumentCsvBean>(columns, DocumentCsvBean.class).getItemStreamReader(fileType, inputFile, encoding);

        // 初期化
        int countRead = 0; // 読み込み件数　読み込み件数 = 新規登録 + 更新 + 削除 + スキップ
        int countInsert = 0; // 新規登録件数
        int countUpdate = 0; // 更新件数
        int countDelete = 0; // 削除件数
        int countSkip = 0; // スキップ件数
        int countError = 0; // エラー件数

        try {

            log.info("Start");

            // 入力チェック
            validateInputFile(chunkContext, result, fileReader);

            // データ更新
            fileReader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
            while ((csvLine = fileReader.read()) != null) {
                countRead++;

                try {
                    // CSVの値をPOJOに格納する
                    Document input = map(csvLine);

                    // キーでデータベースを検索
                    Document current = findByKey(input.getId());

                    if (current == null) {
                        // データベースに存在しない場合、新規登録
                        input.setId(null);
                        input.setVersion(null);
                        documentService.save(input);
                        countInsert++;

                    } else {

                        // CSVの値で更新しない項目を復元
                        input.setVersion(current.getVersion());
                        input.setFiles(current.getFiles());

                        if ("9".equals(input.getStatus())) {
                            // ステータス=9の場合は削除
                            documentService.delete(input.getId());
                            countDelete++;

                        } else if (!equals(input, current)) {
                            // データベースに存在し、差異があれば更新
                            documentService.save(input);
                            countUpdate++;

                        } else {
                            // 差異がなければスキップ
                            countSkip++;
                        }
                    }

                } catch (Exception e) {
                    countError++;
                    log.error(format("%d 行目: Exception: %s", countRead, e.getMessage()));
                    hasDBAccessException = true;
                }
            }

            if (hasDBAccessException) {
                log.error(format("%d 件のエラーが発生しました。", countError));
                log.error(Constants.MSG.DB_ACCESS_ERROR_STOP);
                throw new Exception(Constants.MSG.DB_ACCESS_ERROR_STOP);
            }

        } catch (Exception e) {
            log.error(format("Exception: %s", e.getMessage()));
            throw e;

        } finally {
            fileReader.close();
        }

        log.info(format("読込件数:    %d", countRead));
        log.info(format("挿入件数:    %d", countInsert));
        log.info(format("更新件数:    %d", countUpdate));
        log.info(format("削除件数:    %d", countDelete));
        log.info(format("スキップ件数: %d", countSkip));
        log.info("End");

        MDC.clear();

        return RepeatStatus.FINISHED;
    }

    /*
     * 入力チェック(全件チェック)
     */
    private void validateInputFile(ChunkContext chunkContext, BindingResult result, ItemStreamReader<DocumentCsvBean> fileReader) throws Exception {
        DocumentCsvBean csvLine;
        fileReader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
        while ((csvLine = fileReader.read()) != null) {
            smartValidator.validate(csvLine, result);
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(r -> log.error(r.toString()));
            fileReader.close();
            log.error(Constants.MSG.VALIDATION_ERROR_STOP);
            throw new ValidationException(Constants.MSG.VALIDATION_ERROR_STOP);
        }
        fileReader.close();
    }


    private boolean equals(Document input, Document current) {
        return Objects.equals(input, current);
    }

    private Document findByKey(Long id) {
        if (id == null) {
            return null;
        }

            return documentRepository.findById(id).orElse(null);
    }

    private Document map(DocumentCsvBean csv) {
        final String jobUser = "job_user";

        Document v = beanMapper.map(csv, Document.class);
        v.setUseStage(new HashSet( Arrays.asList( csv.getUseStage().split(",", 0) ) ));
        v.setCreatedDate(dateFactory.newLocalDateTime());
        v.setLastModifiedDate(dateFactory.newLocalDateTime());
        v.setCreatedBy(jobUser);
        v.setLastModifiedBy(jobUser);
        return v;
    }



}
