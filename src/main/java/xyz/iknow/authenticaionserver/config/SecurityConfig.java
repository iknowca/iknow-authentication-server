package xyz.iknow.authenticaionserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.iknow.authenticaionserver.domain.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.security.customFilter.LoginFilter;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetailsService;
import xyz.iknow.authenticaionserver.security.handler.loginHandler.LoginFailureHandler;
import xyz.iknow.authenticaionserver.security.handler.loginHandler.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

            http.cors(cors-> cors.disable());
            http.csrf(csrf ->csrf.disable());
            http.formLogin(formLogin -> formLogin.disable());

            http.authenticationManager(buildCustomAuthenticationManager(http));
            http.addFilterBefore(loginFilter(buildCustomAuthenticationManager(http)), UsernamePasswordAuthenticationFilter.class);


            return http.build();
    }

    @Bean
    public AuthenticationManager buildCustomAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager){
        LoginFilter loginFilter = new LoginFilter("/account/login");
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtService));
        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        return loginFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
