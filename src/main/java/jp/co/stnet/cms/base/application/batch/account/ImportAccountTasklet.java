package jp.co.stnet.cms.base.application.batch.account;

import com.github.dozermapper.core.Mapper;
import jp.co.stnet.cms.base.application.repository.authentication.AccountRepository;
import jp.co.stnet.cms.base.application.service.authentication.AccountService;
import jp.co.stnet.cms.base.domain.model.authentication.Account;
import jp.co.stnet.cms.common.auditing.CustomDateFactory;
import jp.co.stnet.cms.common.batch.ReaderFactory;
import jp.co.stnet.cms.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Component
public class ImportAccountTasklet implements Tasklet {

    // 専用のJOBログ用のlogger
    private static final Logger log = LoggerFactory.getLogger("JobLogger");

    // インポートファイルのカラム定義
    private final String[] columns = {"username","firstName","lastName","department","email","url","profile","roles","status","statusLabel","imageUuid","apiKey","allowedIp"};

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SmartValidator smartValidator;

    @Autowired
    Mapper beanMapper;

    @Autowired
    CustomDateFactory dateFactory;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordGenerator passwordGenerator;

    @Resource(name = "passwordGenerationRules")
    List<CharacterRule> passwordGenerationRules;

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
        AccountCsv csvLine = null;

        // CSVファイルの入力チェック結果を格納するBindingResult
        BindingResult result = new BeanPropertyBindingResult(csvLine, "csvLine");

        // 選択したフォーマット用のReader取得
        ItemStreamReader<AccountCsv> fileReader
                = new ReaderFactory<AccountCsv>(columns, AccountCsv.class).getItemStreamReader(fileType, inputFile, encoding);

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
                    Account input = map(csvLine);

                    // キーでデータベースを検索
                    Account current = findByKey(input.getUsername());

                    if (current == null) {
                        // データベースに存在しない場合、新規登録
                        input.setVersion(null);
                        String rawPassword = passwordGenerator.generatePassword(10, passwordGenerationRules);
                        input.setPassword(passwordEncoder.encode(rawPassword));
                        accountService.save(input);
                        countInsert++;

                    } else if ("admin".equals(current.getUsername())) {
                        log.info("adminユーザは更新できないため、スキップされました。");
                        countSkip++;

                    } else {

                        input.setVersion(current.getVersion());
                        input.setPassword(current.getPassword());
                        if (StringUtils.isNotBlank(input.getImageUuid())) {
                            input.setImageUuid(current.getImageUuid());
                        }

                        if ("9".equals(input.getStatus())) {
                            // ステータス=9の場合は削除
                            accountService.delete(input.getId());
                            countDelete++;

                        } else if (!equals(input, current)) {
                            // データベースに存在し、差異があれば更新
                            accountService.save(input);
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

    private boolean equals(Account input, Account current) {
        return Objects.equals(input, current);
    }

    /*
     * 入力チェック(全件チェック)
     */
    private void validateInputFile(ChunkContext chunkContext, BindingResult result, ItemStreamReader<AccountCsv> fileReader) throws Exception {
        AccountCsv csvLine;
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

    private Account map(AccountCsv csv) {
        final String jobUser = "job_user";

        Account v = beanMapper.map(csv, Account.class);
        v.setCreatedDate(dateFactory.newLocalDateTime());
        v.setLastModifiedDate(dateFactory.newLocalDateTime());
        v.setCreatedBy(jobUser);
        v.setLastModifiedBy(jobUser);
        return v;
    }

    private Account findByKey(String username) {
        return accountRepository.findById(username).orElse(null);
    }

}
