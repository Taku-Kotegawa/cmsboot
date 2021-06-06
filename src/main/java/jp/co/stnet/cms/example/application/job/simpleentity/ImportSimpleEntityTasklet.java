package jp.co.stnet.cms.example.application.job.simpleentity;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.common.constant.Constants;
import jp.co.stnet.cms.common.exception.NoChangeBusinessException;
import jp.co.stnet.cms.example.application.repository.simpleentity.SimpleEntityRepository;
import jp.co.stnet.cms.example.application.repository.simpleentity.SimpleEntityRevisionRepository;
import jp.co.stnet.cms.example.application.service.SimpleEntityService;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntity;
import jp.co.stnet.cms.example.domain.model.simpleentity.SimpleEntityCsv;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;


/**
 * job02ジョブ実装
 * CSV -> VARIABLEテーブル
 */
@Component
public class ImportSimpleEntityTasklet implements Tasklet {

    @Autowired
    ItemStreamReader<SimpleEntityCsv> csvReader;

    @Autowired
    ItemStreamReader<SimpleEntityCsv> tsvReader;

    @Autowired
    SmartValidator smartValidator;

    @Autowired
    SimpleEntityRepository simpleEntityRepository;

    @Autowired
    SimpleEntityRevisionRepository simpleEntityRevisionRepository;

    @Autowired
    SimpleEntityService simpleEntityService;

    @Autowired
    Mapper beanMapper;

    @Autowired
    CustomDateFactory dateFactory;

    private static final Logger log = LoggerFactory.getLogger("JobLogger");

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        Long jobInstanceId = chunkContext.getStepContext().getJobInstanceId();
        String jobName = chunkContext.getStepContext().getJobName();
        String fileType = stepContribution.getStepExecution().getJobParameters().getString("filetype");
        String executedBy = stepContribution.getStepExecution().getJobParameters().getString("executedBy");
        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();

        MDC.put("jobInstanceId", jobInstanceId.toString());
        MDC.put("jobName", jobName);
        MDC.put("jobExecutionId", jobExecutionId.toString());
        MDC.put("jobName_jobExecutionId", jobName + "_" + jobExecutionId.toString());

        int count_read = 0; // 読み込み件数　読み込み件数 = 新規登録 + 更新 + 削除 + スキップ
        int count_insert = 0; // 新規登録件数
        int count_update = 0; // 更新件数
        int count_delete = 0; // 削除件数
        int count_skip = 0; // スキップ件数
        int count_error = 0;

        // DB操作時の例外発生の有無を記録する
        boolean hasDBAccessException = false;

        // CSVファイルを１行を格納するBean
        SimpleEntityCsv csvLine = null;

        // CSVファイルの入力チェック結果を格納するBindingResult
        BindingResult result = new BeanPropertyBindingResult(csvLine, "csvLine");

        log.info("Start");

        // フィアルフォーマットの選択
        ItemStreamReader<SimpleEntityCsv> fileReader = null;
        if ("CSV".equals(fileType)) {
            fileReader = csvReader;
        } else {
            fileReader = tsvReader;
        }

        try {
            /*
             * 入力チェック(全件チェック)
             */
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

            /*
             * テーブル更新(一括コミット)
             */
            fileReader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
            while ((csvLine = fileReader.read()) != null) {
                count_read++;
                try {

                    SimpleEntity v = map(csvLine);

                    if (csvLine.getId() == null) {
                        v.setCreatedBy(executedBy);
                        v.setCreatedDate(dateFactory.newLocalDateTime());
                        v.setLastModifiedBy(executedBy);
                        v.setLastModifiedDate(dateFactory.newLocalDateTime());
                        // 新規登録
                        simpleEntityService.save(v);
                        count_insert++;
                    } else {
                        // 更新
                        SimpleEntity current = simpleEntityRepository.findById(Long.parseLong(csvLine.getId())).orElse(null);
                        if (current != null) {
                            if ("99".equals(csvLine.getStatus())) {
                                simpleEntityService.delete(current.getId());
                                count_delete++;
                            } else {
                                try {
                                    v.setVersion(current.getVersion());
                                    simpleEntityService.save(v);
                                    v.setLastModifiedBy(executedBy);
                                    v.setLastModifiedDate(dateFactory.newLocalDateTime());
                                    count_update++;
                                } catch (NoChangeBusinessException e) {
                                    count_skip++;
                                }
                            }

                        } else {
                            count_skip++;
                        }
                    }

                } catch (Exception e) {
                    count_error++;
                    log.error(count_read + " 行目: Exception: " + e.getMessage());
                    hasDBAccessException = true;
                }
            }

            if (hasDBAccessException) {
                log.error(count_error + " 件のエラーが発生しました。");
                log.error(Constants.MSG.DB_ACCESS_ERROR_STOP);
                throw new Exception(Constants.MSG.DB_ACCESS_ERROR_STOP);
            }

        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            throw e;

        } finally {
            fileReader.close();
        }

        log.info("読込件数:    " + count_read);
        log.info("挿入件数:    " + count_insert);
        log.info("更新件数:    " + count_update);
        log.info("削除件数:    " + count_delete);
        log.info("スキップ件数: " + count_skip);
        log.info("End");

        MDC.clear();

        return RepeatStatus.FINISHED;
    }

    /**
     * CSV->Variable変換
     *
     * @param csv CSV一行を表すModel
     * @return VariableのModel
     */
    private SimpleEntity map(SimpleEntityCsv csv) {
//        String JOB_EXECUTOR = "job_user";
        SimpleEntity v = beanMapper.map(csv, SimpleEntity.class);
        return v;
    }

}
