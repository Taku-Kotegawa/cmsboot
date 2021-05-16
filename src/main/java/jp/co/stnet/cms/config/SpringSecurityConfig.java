package jp.co.stnet.cms.config;


import jp.co.stnet.cms.common.validation.rule.EncodedPasswordHistoryRule;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.passay.EnglishCharacterData.*;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final int UPPERCASE_MIN_LENGTH = 1;
    private final int LOWERCASE_MIN_LENGTH = 1;
    private final int DIGIT_MIN_LENGTH = 1;
    private final int SPECIAL_MIN_LENGTH = 1;

    // @see https://www.passay.org/
    @Autowired
    UserDetailsService loggedInUserDetailsService;
    @Value("${security.passwordMinimumLength}")
    private Integer minimumLength;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/reissue/**").permitAll()
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
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("demo").password("{pbkdf2}1dd84f42a7a9a173f8f806d736d34939bed6a36e2948e8bfe88801ee5e6e61b815efc389d03165a4").roles("USER");

        auth.userDetailsService(loggedInUserDetailsService);

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


}