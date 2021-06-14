package jp.co.stnet.cms.base.presentation.interceptor;


import jp.co.stnet.cms.base.application.service.accesscounter.AccessCounterService;
import jp.co.stnet.cms.base.domain.model.common.AccessCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class BlockHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    AccessCounterService accessCounterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // log.info("preHandle : {} {} {}", request, request, handler);

        // Handlerメソッドを呼び出す場合はtrueを返却する
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if (modelAndView != null && modelAndView.hasView()) {
//            Optional<AccessCounter> accessCounter = accessCounterService.findByUrl(request.getRequestURI());
//            accessCounter.ifPresent(counter -> modelAndView.addObject("accessCount", counter.getCount()));
//        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String[] noCountUrl = {""};

        if (response.getContentType() != null && response.getContentType().contains("text/html")) {
            accessCounterService.countUp(request.getRequestURI());
        }
    }

}
