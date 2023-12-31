package nora.movlog.config;

import lombok.RequiredArgsConstructor;
import nora.movlog.handler.LoginFailureHandler;
import nora.movlog.handler.MyAccessDeniedHandler;
import nora.movlog.handler.MyAuthenticationEntryPoint;
import nora.movlog.repository.user.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static nora.movlog.utils.constant.StringConstant.*;

/*
* 사용자 로그인, 회원가입 등 보안 관련 config
*/

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final MemberRepository memberRepository;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/icons/**"),
                                         new AntPathRequestMatcher("/img/**"),
                                         new AntPathRequestMatcher("/css/**")).permitAll() // 웹페이지 표시를 위한 리소스는 전체 공개
                        .requestMatchers(new AntPathRequestMatcher(LOGIN_URI),
                                         new AntPathRequestMatcher(JOIN_URI)).anonymous() // 로그인, 회원가입 페이지는 로그인하지 않은 회원에게만 보이게
                        .requestMatchers(new AntPathRequestMatcher(CHECK_VERIFY_URI)).hasAnyAuthority(AUTH_UNVERIFIED, AUTH_VERIFIED)
                        .requestMatchers(new AntPathRequestMatcher(VERIFY_URI),
                                         new AntPathRequestMatcher(VERIFY_URI + RESEND_URI)).hasAuthority(AUTH_UNVERIFIED)
                        .anyRequest().hasAuthority(AUTH_VERIFIED) // 그 외 페이지는 로그인한 회원에게만 보이게
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                        .accessDeniedHandler(new MyAccessDeniedHandler(memberRepository))
                )
                .formLogin(login -> login
                        .loginPage(LOGIN_URI)
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl(CHECK_VERIFY_URI, true) // 이후 수정
                        .failureUrl(LOGIN_URI)
                        .failureHandler(new LoginFailureHandler())
                )
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_URI)
                        .logoutSuccessUrl(LOGIN_URI)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
