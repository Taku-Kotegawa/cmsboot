package jp.co.stnet.cms.base.presentation.controller.job;


import jp.co.stnet.cms.base.domain.model.authentication.LoggedInUser;
import jp.co.stnet.cms.base.domain.model.authentication.Role;
import jp.co.stnet.cms.base.presentation.controller.uploadfile.UploadFileForm;
import jp.co.stnet.cms.common.exception.IllegalStateBusinessException;
import jp.co.stnet.cms.common.message.MessageKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.terasoluna.gfw.common.message.ResultMessages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("job")
public class JobController {

    private static final String BASE_PATH = "job";
    private static final String JSP_RESULT = BASE_PATH + "/result";
    private static final String JSP_JOBLOG = BASE_PATH + "/joblog";
    private static final String JSP_SUMMARY = BASE_PATH + "/summary";

    @Autowired
    JobExplorer jobExplorer;

    @Value("${job.log.dir}")
    String jobLogDir;


    private static final String FIND_EXECUTIONS =
            "SELECT p.JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION_PARAMS p\n" +
                    "INNER JOIN BATCH_JOB_EXECUTION bje on bje.JOB_EXECUTION_ID = p.JOB_EXECUTION_ID \n" +
                    "INNER JOIN BATCH_JOB_INSTANCE bji on bji.JOB_INSTANCE_ID  = bje.JOB_INSTANCE_ID and bji.JOB_NAME = ?\n" +
                    "WHERE p.KEY_NAME = 'executedBy' AND p.STRING_VAL = ?\n" +
                    "ORDER BY p.JOB_EXECUTION_ID DESC\n" +
                    "LIMIT 20";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @ModelAttribute
    UploadFileForm setup() {
        return new UploadFileForm();
    }


    /**
     * ユーザが管理者を持っている。
     *
     * @param loggedInUser loggedInUser
     * @return true: 管理者権限あり, false:権限なし
     */
    private boolean isAdmin(LoggedInUser loggedInUser) {
        return loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()));
    }

    @GetMapping("summary")
    public String summary(Model model, @AuthenticationPrincipal LoggedInUser loggedInUser) {
        List<Map<String, Object>> jobSummary = new ArrayList<>();
        List<String> jobNames = jobExplorer.getJobNames();

        if (isAdmin(loggedInUser)) {

            // 管理者はすべてのジョブの結果を確認できる。
            for (String jobName : jobNames) {
                Map map = new LinkedHashMap();
                JobInstance jobInstance = jobExplorer.getLastJobInstance(jobName);
                map.put("jobName", jobName);
                map.put("jobInstance", jobInstance);
                map.put("jobExecution", jobExplorer.getLastJobExecution(jobInstance));
                jobSummary.add(map);
            }

        } else {
            // 通常は自分が実行したジョブの結果を確認できる。
            for (String jobName : jobNames) {
                List<Long> jobExecutionIds = jdbcTemplate.queryForList(FIND_EXECUTIONS, Long.class, loggedInUser.getUsername(), jobName);
                if (!jobExecutionIds.isEmpty()) {
                    JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionIds.get(0));
                    Map map = new LinkedHashMap();
                    map.put("jobName", jobName);
                    map.put("jobInstance", jobExecution.getJobInstance());
                    map.put("jobExecution", jobExecution);
                    jobSummary.add(map);
                }
            }

        }

        model.addAttribute("jobSummary", jobSummary);
        return JSP_SUMMARY;
    }


    @GetMapping("result")
    public String result(Model model,
                         @RequestParam(value = "targetjob", required = false) String targetjob,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {


        List<String> jobList = jobExplorer.getJobNames();

        if (targetjob == null && !jobList.isEmpty()) {
            targetjob = jobList.get(0);
        }

        List<JobInstance> instances = new ArrayList<>();
        List<JobExecution> executions = new ArrayList<>();

        if (isAdmin(loggedInUser)) {
            // 管理者は全ジョブを参照可能
            instances = jobExplorer.getJobInstances(targetjob, 0, 20);
            for (JobInstance i : instances) {
                ArrayList<JobExecution> list = (ArrayList<JobExecution>) jobExplorer.getJobExecutions(i);

                executions.addAll(list);
            }

        } else {
            // 通常は自分が実行したジョブを参照可能
            List<Long> jobExecutionIds = jdbcTemplate.queryForList(FIND_EXECUTIONS, Long.class, loggedInUser.getUsername(), targetjob);
            for (Long jobExecutionId : jobExecutionIds) {
                JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);
                instances.add(jobExecution.getJobInstance());
                executions.add(jobExecution);
            }
        }

        model.addAttribute("jobList", jobList);
        model.addAttribute("selectedJob", targetjob);
        model.addAttribute("jobResults", executions);
        return JSP_RESULT;
    }

    @GetMapping("joblog")
    public String jobLog(Model model, @RequestParam(value = "jobexecutionid") Long jobExecutionId,
                         @AuthenticationPrincipal LoggedInUser loggedInUser) {

        if (!isAdmin(loggedInUser)) {
            List<String> jobNames = jobExplorer.getJobNames();
            List<Long> jobExecutionIds = new ArrayList<>();

            for (String jobName : jobNames) {
                jobExecutionIds.addAll(jdbcTemplate.queryForList(FIND_EXECUTIONS, Long.class, loggedInUser.getUsername(), jobName));
            }

            if (!jobExecutionIds.contains(jobExecutionId)) {
                // 権限なし
                throw new IllegalStateBusinessException(ResultMessages.error().add(MessageKeys.E_SL_FW_5001));
            }

        }

        JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);
        if (jobExecution == null) {
            throw new IllegalStateBusinessException(ResultMessages.error().add(MessageKeys.E_SL_FW_5001));
        }

        String jobName = jobExecution.getJobInstance().getJobName();
        String filePath = jobLogDir + "/Job_" + jobName + "_" + jobExecutionId + ".log";
        List<String> fileLines = new ArrayList<>();
        try {
            fileLines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            fileLines.add("ログファイルが見つかりません。");
        }

        model.addAttribute("jobName", jobName);
        model.addAttribute("jobExecutionId", jobExecutionId.toString());
        model.addAttribute("jobLog", fileLines);
        return JSP_JOBLOG;
    }

}
