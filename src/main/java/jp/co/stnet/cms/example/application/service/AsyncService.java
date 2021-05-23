package jp.co.stnet.cms.example.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
public class AsyncService {

    @Autowired
    SimpleEntityService simpleEntityService;

    @Async
    public void execute(Long id, String name, Date reqDate) throws InterruptedException {

        log.info("Async Start");
        // 適当なバッチ処理
        Thread.sleep(5000);

        log.info("Async Stop");
    }
}
