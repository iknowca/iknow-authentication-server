package xyz.iknow.authenticaionserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import xyz.iknow.authenticaionserver.security.customFilter.AccessTokenRefreshFilter;
import xyz.iknow.authenticaionserver.security.customFilter.LoginFilter;
import xyz.iknow.authenticaionserver.security.customFilter.TokenCheckFilter;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetailsService;
import xyz.iknow.authenticaionserver.security.handler.loginHandler.LoginFailureHandler;
import xyz.iknow.authenticaionserver.security.handler.loginHandler.LoginSuccessHandler;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.jwt.JwtUtility;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtility jwtUtility;
    private final TokenService tokenService;
    @Value("${host.frontend.imf}")
    private String frontendHostUrl;
    @Value("${authorization.match.path.ANONYMOUS}")
    private List<String> anonymousPath;
    @Value("${authorization.match.path.AUTHENTICATED}")
    private List<String> authenticatedPath;
    @Value("${authorization.match.path.PERMITALL}")
    private List<String> permitAllPath;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

            http.addFilterBefore(characterEncodingFilter(), CsrfFilter.class);
            http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
            http.csrf(csrf ->csrf.disable());
            http.formLogin(formLogin -> formLogin.disable());
            http.authenticationManager(buildCustomAuthenticationManager(http));
            http.logout(logout -> logout.disable());
            http.addFilterBefore(loginFilter(buildCustomAuthenticationManager(http)), UsernamePasswordAuthenticationFilter.class);
            http.addFilterBefore(tokenCheckFilter(), UsernamePasswordAuthenticationFilter.class);
            http.addFilterBefore(accessTokenRefreshFilter(), LoginFilter.class);
            http.authorizeHttpRequests((authorizeRequests) -> {
                authorizeRequests.requestMatchers(authenticatedPath.toArray(new String[0])).authenticated();
                authorizeRequests.requestMatchers(anonymousPath.toArray(new String[0])).anonymous();
                authorizeRequests.requestMatchers(permitAllPath.toArray(new String[0])).permitAll();
            });

            return http.build();
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(frontendHostUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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
    public TokenCheckFilter tokenCheckFilter() {
        return new TokenCheckFilter(jwtService, customUserDetailsService, jwtUtility);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessTokenRefreshFilter accessTokenRefreshFilter() throws Exception {
        AccessTokenRefreshFilter refreshFilter = new AccessTokenRefreshFilter(jwtService, tokenService);
        refreshFilter.setUrl("/jwt/refresh");
        return refreshFilter;
    }

}
