package com.web.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // security 관련 config 이기 때문
public class SecurityConfig {

    @Bean// 인수가 중요
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(auth -> auth.anyRequest().authenticated())// 어떤 요청도 무조건 인증을 해야 한다
                //.authorizeRequests(auth->auth.anyRequest().permitAll()) // 로그인 화면 없이(인증 없이) 바로 실행
                .formLogin(form->form
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/",true)// ssr 에서만 의미있음, csr 인 경우에는 클라이언트가 리다이렉션한다
                        .failureUrl("/failed")// csr 에서는 사용자 인증 요청을 먼저하고, 토큰이 담긴 응답을 받아서, 토큰을 가지고 진짜 하고 싶은 요청을 함
                        .usernameParameter("userId")// ssr 인 경우에 form 의 name="userId"
                        .passwordParameter("userPass")
                        .successHandler((request, response, authentication) ->{// 단순 리다이렉션 이외의 일을 하고 싶을 때
                            System.out.println("authentication = " + authentication);
                            response.sendRedirect("/home");
                        })// authentication 안에 인증된 사용자에 대한 정보가 있음
                        .failureHandler((request, response, exception)->{
                            System.out.println("exception = " + exception);
                            response.sendRedirect("/login");
                        })//
                        .permitAll()
                )
                .build();// HttpSecurity 에서 지정한 설정을 바탕으로 SecurityFilterChain 생성
    }

    /**
     * 여러명의 계정 추가
     * user + 터미널에 나오는 키 대신
     * 메모리에 올라가는 계정들(디비x)
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // noop 는 암호화하지 않고 평문으로 넣겠다
        UserDetails userDetails01 =
                User.withUsername("jang").password("{noop}1234").roles("USER").build();
        UserDetails userDetails02 =
                User.withUsername("kim").password("{noop}1234").roles("USER").build();
        UserDetails userDetails03 =
                User.withUsername("king").password("{noop}1234").roles("USER").build();

        //InMemoryUserDetailsManager 는 UserDetailService 의 구현체, 아이디 정보가 디비에 들어가지 않고 메모리에 올라간다
        return new InMemoryUserDetailsManager(userDetails01, userDetails02, userDetails03);
    }
}
