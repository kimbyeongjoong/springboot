package com.libms.configuration;

import com.libms.component.UserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationProvider authenticationProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
        web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .requestMatchers(new AntPathRequestMatcher("/common/common"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user_info/**").authenticated()
                .antMatchers("/mail_send").hasRole("ADMIN") // 내부적으로 접두어 "ROLE_"가 붙는다.
                .anyRequest().permitAll();

        http.formLogin()
                .loginPage("/login") // default
                .loginProcessingUrl("/authenticate")
                .usernameParameter("user_id")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .permitAll();

        http.logout()
                .logoutUrl("/logout") //default
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();

        http.sessionManagement().maximumSessions(1);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    // spring security bean 설정은 여기서 한다.
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new LoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        String user_id = "user_id";
        String password = "password";
        return new LoginFailureHandler(user_id, password);
    }
}

//.csrf().disable()
//  참고용 코드
//        http.csrf().disable().authorizeRequests()
//                // /about 요청에 대해서는 로그인을 요구함
//                .antMatchers("/about").authenticated()
//                // /admin 요청에 대해서는 ROLE_ADMIN 역할을 가지고 있어야 함
//                .antMatchers("/admin").hasRole("ADMIN")
//                // 나머지 요청에 대해서는 로그인을 요구하지 않음
//                .anyRequest().permitAll()
//
//                .and()
//                // 로그인하는 경우에 대해 설정함
//                .formLogin()
//                // 로그인 페이지를 제공하는 URL을 설정함
//                .loginPage("/login")
//                // 인증을 처리하는 경로를 지정한다
//                .loginProcessingUrl("/authenticate")
//                // 로그인 성공 URL을 설정함
//                .successForwardUrl("/index")
//                // 로그인 실패 URL을 설정함
////                .failureForwardUrl("/index")
//                .permitAll();
////                .and()
////                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);