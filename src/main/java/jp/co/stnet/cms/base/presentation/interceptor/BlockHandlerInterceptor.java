package jp.co.stnet.cms.base.presentation.interceptor;


import jp.co.stnet.cms.base.application.service.accesscounter.AccessCounterService;
import jp.co.stnet.cms.sales.application.service.document.DocumentAccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class BlockHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    AccessCounterService accessCounterService;

    @Autowired
    DocumentAccessService documentAccessService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if (ex != null) {
            return;
        }

        // staticファイルはインターセプトさせずにスルーさせる
        if (handler instanceof ResourceHttpRequestHandler) {
            return;
        }

        String[] noCountUrl = {""};
        if (response.getContentType() != null && response.getContentType().contains("text/html")) {
            accessCounterService.countUp(request.getRequestURI());
        }

    }

}
