package nora.movlog.config;

import lombok.RequiredArgsConstructor;
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

/*
* 사용자 로그인, 회원가입 등 보안 관련 config
*/

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
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
        return http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/icons/**"),
                                         new AntPathRequestMatcher("/img/**"),
                                         new AntPathRequestMatcher("/css/**")).permitAll() // 웹페이지 표시를 위한 리소스는 전체 공개
                        .requestMatchers(new AntPathRequestMatcher("/login"),
                                         new AntPathRequestMatcher("/join")).anonymous() // 로그인, 회원가입 페이지는 로그인하지 않은 회원에게만 보이게
                        .anyRequest().authenticated() // 그 외 페이지는 로그인한 회원에게만 보이게
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/search") // 이후 수정
                        .failureUrl("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .build();
    }
}
