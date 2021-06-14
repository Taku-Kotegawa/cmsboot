package jp.co.stnet.cms.config;

import jp.co.stnet.cms.base.domain.model.authentication.CustomAuthenticationUserDetailService;
import jp.co.stnet.cms.base.domain.model.authentication.CustomPreAuthenticatedProcessingFilter;
import jp.co.stnet.cms.base.domain.model.authentication.CustomUsernamePasswordAuthenticationFilter;
import jp.co.stnet.cms.base.domain.model.authentication.CustomUsernamePasswordAuthenticationProvider;
import jp.co.stnet.cms.common.validation.rule.EncodedPasswordHistoryRule;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.passay.EnglishCharacterData.*;

public class SpringSecurityConfig {

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {


            http.antMatcher("/api/**").authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .addFilter(preAuthenticatedProcessingFilter())
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // セッションを使用しない
                    .and()
                    .exceptionHandling().authenticationEntryPoint(http403ForbiddenEntryPoint())
                    .and()
                    .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(preAuthenticatedAuthenticationProvider());
        }

        // https://sebenkyo.com/2020/07/22/post-1186/#toc_id_4
        @Bean
        public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
            PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
            preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService());
            preAuthenticatedAuthenticationProvider.setUserDetailsChecker(new AccountStatusUserDetailsChecker());

            return preAuthenticatedAuthenticationProvider;
        }

        @Bean
        public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService() {
            return new CustomAuthenticationUserDetailService();
        }

        @Bean
        public AbstractPreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter() throws Exception {
            AbstractPreAuthenticatedProcessingFilter preAuthenticatedProcessingFilter = new CustomPreAuthenticatedProcessingFilter();
            preAuthenticatedProcessingFilter.setAuthenticationManager(authenticationManager());

            return preAuthenticatedProcessingFilter;
        }

        @Bean
        Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
            return new Http403ForbiddenEntryPoint();
        }

    }


    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final int UPPERCASE_MIN_LENGTH = 1;
        private final int LOWERCASE_MIN_LENGTH = 1;
        private final int DIGIT_MIN_LENGTH = 1;
        private final int SPECIAL_MIN_LENGTH = 1;

        // @see https://www.passay.org/
        @Autowired
        UserDetailsService loggedInUserDetailsService;

        @Value("${security.passwordMinimumLength}")
        Integer minimumLength;

        @Autowired
        SessionRegistry sessionRegistry;

        // ログイン中ユーザ一覧の表示のため
        @Bean
        public static ServletListenerRegistrationBean httpSessionEventPublisher() {
            return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class)
                    .addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                    .authorizeRequests()

                    .antMatchers("/login").permitAll()
                    .antMatchers("/account/create").permitAll()
                    .antMatchers("/reissue/**").permitAll()
                    .antMatchers("/app/**").permitAll()
                    .antMatchers("/AdminLTE/**").permitAll()
                    .antMatchers("/plugins/**").permitAll()

                    .antMatchers("/unlock/**").hasAnyRole("ADMIN")
                    .antMatchers("/admin/impersonate/**").hasAnyRole("ADMIN")

                    .anyRequest()
                    .authenticated();

            http.formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
                    .passwordParameter("password");

            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true);

            // 同時ログイン数の制御
//            http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());

        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(loggedInUserDetailsService);
            auth.authenticationProvider(customUsernamePasswordAuthenticationProvider());
        }

        @Bean
        public SessionRegistryImpl sessionRegistry() {
            return new SessionRegistryImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new DelegatingPasswordEncoder(
                    "pbkdf2",
                    new HashMap<String, PasswordEncoder>() {{
                        put("pbkdf2", new Pbkdf2PasswordEncoder());
                        put("bcrypt", new BCryptPasswordEncoder());
                    }}
            );
        }

        @Bean
        public LengthRule lengthRule() {
            LengthRule rule = new LengthRule();
            rule.setMinimumLength(minimumLength);
            return rule;
        }

        @Bean
        public List<CharacterRule> passwordGenerationRules() {
            List<CharacterRule> list = new ArrayList<>();
            list.add(new CharacterRule(UpperCase, UPPERCASE_MIN_LENGTH));
            list.add(new CharacterRule(LowerCase, LOWERCASE_MIN_LENGTH));
            list.add(new CharacterRule(Digit, DIGIT_MIN_LENGTH));
            return list;
        }

        @Bean
        public CharacterCharacteristicsRule characterCharacteristicsRule() {
            CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule();
            rule.setRules(
                    new CharacterRule(UpperCase, UPPERCASE_MIN_LENGTH),
                    new CharacterRule(LowerCase, LOWERCASE_MIN_LENGTH),
                    new CharacterRule(Digit, DIGIT_MIN_LENGTH),
                    new CharacterRule(Special, SPECIAL_MIN_LENGTH)
            );
            rule.setNumberOfCharacteristics(3);
            return rule;
        }

        @Bean
        public UsernameRule usernameRule() {
            return new UsernameRule();
        }

        @Bean
        public EncodedPasswordHistoryRule encodedPasswordHistoryRule() {
            return new EncodedPasswordHistoryRule(passwordEncoder());
        }

        @Bean
        public PasswordValidator characteristicPasswordValidator() {
            return new PasswordValidator(
                    lengthRule(),
                    characterCharacteristicsRule(),
                    usernameRule()
            );
        }

        @Bean
        public PasswordValidator encodedPasswordHistoryValidator() {
            return new PasswordValidator(
                    encodedPasswordHistoryRule()
            );
        }

        @Bean
        public PasswordGenerator passwordGenerator() {
            return new PasswordGenerator();
        }

        @Bean
        public CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider() {
            CustomUsernamePasswordAuthenticationProvider provider = new CustomUsernamePasswordAuthenticationProvider();
            provider.setUserDetailsService(loggedInUserDetailsService);
            provider.setPasswordEncoder(passwordEncoder());

            return provider;
        }


        public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
            CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();

            filter.setRequiresAuthenticationRequestMatcher(
                    new AntPathRequestMatcher("/login", "POST")
            );

            filter.setAuthenticationManager(authenticationManager());

            filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error=true"));

            filter.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());


            List<SessionAuthenticationStrategy> strategies = new ArrayList<>();
            strategies.add(new CsrfAuthenticationStrategy(new HttpSessionCsrfTokenRepository()));
            strategies.add(new SessionFixationProtectionStrategy());

            ConcurrentSessionControlAuthenticationStrategy sessionControlAuthenticationStrategy
                    = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
            sessionControlAuthenticationStrategy.setMaximumSessions(1);
            sessionControlAuthenticationStrategy.setExceptionIfMaximumExceeded(false);
            strategies.add(sessionControlAuthenticationStrategy);

            filter.setSessionAuthenticationStrategy(new CompositeSessionAuthenticationStrategy(strategies));

            strategies.add(new RegisterSessionAuthenticationStrategy((sessionRegistry)));

            return filter;
        }


        // 非同期処理で認証情報を呼び出し先のオブジェクトに渡す
        @PostConstruct
        public void enableAuthCtxOnSpawnedThreads() {
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        }

        // ユーザ切り替え機能
        public SwitchUserFilter switchUserFilter() {
            SwitchUserFilter filter = new SwitchUserFilter();
            filter.setUserDetailsService(loggedInUserDetailsService);
            filter.setUsernameParameter("username");
            filter.setSwitchUserUrl("/admin/impersonate");
            filter.setExitUserUrl("/logout/impersonate");
            filter.setTargetUrl("/");
            filter.afterPropertiesSet();
            return filter;
        }


    }

}