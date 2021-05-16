package jp.co.stnet.cms.config;

import jp.co.stnet.cms.common.processor.RequestDataValueProcessorPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenRequestDataValueProcessor;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Bean
    public SpringTemplateEngine pageTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode( TemplateMode.HTML );
        resolver.setPrefix( "templates/" ); // src/mail/resources/templates
        resolver.setSuffix( ".html" );
        resolver.setCharacterEncoding( "UTF-8" );
        resolver.setCacheable( false );

        var engine = new SpringTemplateEngine();
        engine.setTemplateResolver( resolver );

        return engine;
    }



    // ２重送信防止TransactionTokenの有効化
    // @see https://qiita.com/tasogarei/items/9c3670201d4abb7276c4

    @Bean
    public TransactionTokenInterceptor transactionTokenInterceptor() {
        return new TransactionTokenInterceptor();
    }

    @Bean
    public RequestDataValueProcessor requestDataValueProcessor() {
        return new TransactionTokenRequestDataValueProcessor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(transactionTokenInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public RequestDataValueProcessorPostProcessor requestDataValueProcessorPostProcessor() {
        return new RequestDataValueProcessorPostProcessor();
    }
}