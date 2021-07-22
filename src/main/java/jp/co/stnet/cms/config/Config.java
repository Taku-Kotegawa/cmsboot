package jp.co.stnet.cms.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Config {

    @Value("${app.security.prohibitedChars}")
    private char[] prohibitedChars;

    @Value("${app.security.prohibitedCharsForFileName}")
    private char[] prohibitedCharsForFileName;


    // TODO InputValidationFilterの移植(特殊文字の外部からの侵入を防御してくれる。ライブラリ不足で移植できていない)
//    @Bean
//    public InputValidationFilter inputValidationFilter() {
//        return new InputValidationFilter(prohibitedChars, prohibitedCharsForFileName);
//    }


    //TODO 以下の定義はExceptionハンドリングで必要になるが、SpringBootの標準機能で不足がなければ削除

//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//        source.setBasename("i18n/application-messages");
//        source.setDefaultEncoding("UTF-8");
//        return source;
//    }

//    @Bean
//    public SimpleMappingExceptionCodeResolver exceptionCodeResolver() {
//        SimpleMappingExceptionCodeResolver resolver = new SimpleMappingExceptionCodeResolver();
//        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        map.put("ResourceNotFoundException", "e.sl.fw.5001");
//        map.put("MultipartException", "e.sl.fw.6001");
//        map.put("InvalidTransactionTokenException", "e.sl.fw.7001");
//        map.put("BusinessException", "e.sl.fw.8001");
//        map.put("DataAccessException", "e.sl.fw.9002");
//        resolver.setExceptionMappings(map);
//        resolver.setDefaultExceptionCode("e.sl.fw.9001");
//        return resolver;
//    }

//    @Bean
//    public ExceptionLogger exceptionLogger() {
//        ExceptionLogger logger = new ExceptionLogger();
//        logger.setExceptionCodeResolver(exceptionCodeResolver());
//        return logger;
//    }

//    @Bean
//    public ExceptionLoggingFilter exceptionLoggingFilter() {
//        ExceptionLoggingFilter filter = new ExceptionLoggingFilter();
//        filter.setExceptionLogger(exceptionLogger());
//        return filter;
//    }


}
