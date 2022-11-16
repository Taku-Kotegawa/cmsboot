package jp.co.stnet.cms.base.application.batch.variable;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.repository.variable.VariableRepository;
import jp.co.stnet.cms.base.application.service.variable.VariableService;
import jp.co.stnet.cms.base.domain.model.variable.Variable;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.common.batch.ReaderFactory;
import jp.co.stnet.cms.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import java.util.List;

import static java.lang.String.format;

@Component
public class ImportVariableTasklet implements Tasklet {

    // 専用のJOBログ用のlogger
    private static final Logger log = LoggerFactory.getLogger("JobLogger");
    // インポートファイルのカラム定義
    private final String[] columns = {"id", "version", "status", "statusLabel", "type", "code", "value1", "value2", "value3", "value4", "value5", "value6", "value7", "value8", "value9", "value10", "date1", "date2", "date3", "date4", "date5", "valint1", "valint2", "valint3", "valint4", "valint5", "textarea", "file1Uuid", "remark"};
    @Autowired
    VariableService variableService;
    @Autowired
    VariableRepository variableRepository;
    @Autowired
    SmartValidator smartValidator;
    @Autowired
    Mapper beanMapper;
    @Autowired
    CustomDateFactory dateFactory;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = null;

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
        VariableCsv csvLine = null;

        // CSVファイルの入力チェック結果を格納するBindingResult
        BindingResult result = new BeanPropertyBindingResult(csvLine, "csvLine");

        // 選択したフォーマット用のReader取得
        ItemStreamReader<VariableCsv> fileReader = new ReaderFactory<VariableCsv>(columns, VariableCsv.class).getItemStreamReader(fileType, inputFile, encoding);

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

                    status = transactionManager.getTransaction(definition);

                    // CSVの値をPOJOに格納する
                    Variable input = map(csvLine);

                    // キーでデータベースを検索
                    Variable current = findByKey(input.getType(), input.getCode());

                    if (current == null) {
                        // データベースに存在しない場合、新規登録
                        input.setVersion(0L);
                        input.setId(null);
                        variableService.save(input);
                        countInsert++;

                    } else {

                        input.setId(current.getId());
                        input.setVersion(current.getVersion());
                        if (StringUtils.isNotBlank(input.getFile1Uuid())) {
                            input.setFile1Uuid(current.getFile1Uuid());
                        }

                        if ("9".equals(input.getStatus())) {
                            // ステータス=9の場合は削除
                            variableService.delete(input.getId());
                            countDelete++;

                        } else if (!equals(input, current)) {
                            // データベースに存在し、差異があれば更新
                            variableService.save(input);
                            countUpdate++;

                        } else {
                            // 差異がなければスキップ
                            countSkip++;
                        }
                    }

                    transactionManager.commit(status);

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


    private boolean equals(Variable input, Variable current) {
        return variableService.equalsEntity(input, current);
    }

    /*
     * 入力チェック(全件チェック)
     */
    private void validateInputFile(ChunkContext chunkContext, BindingResult result, ItemStreamReader<VariableCsv> fileReader) throws Exception {
        VariableCsv csvLine;
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

    private Variable map(VariableCsv csv) {
        final String jobUser = "job_user";

        Variable v = beanMapper.map(csv, Variable.class);
        v.setCreatedDate(dateFactory.newLocalDateTime());
        v.setLastModifiedDate(dateFactory.newLocalDateTime());
        v.setCreatedBy(jobUser);
        v.setLastModifiedBy(jobUser);
        return v;
    }

    private Variable findByKey(String type, String code) {
        List<Variable> list = variableRepository.findAllByTypeAndCode(type, code);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
