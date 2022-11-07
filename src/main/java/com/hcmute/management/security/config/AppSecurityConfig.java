package com.hcmute.management.security.config;

import com.hcmute.management.common.UserPermission;
import com.hcmute.management.handler.GlobalExceptionHandler;
import com.hcmute.management.handler.OAuth2Handler;
import com.hcmute.management.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.hcmute.management.security.JWT.AuthEntryPointJwt;
import com.hcmute.management.security.Service.AppUserDetailService;
import com.hcmute.management.security.filter.AuthTokenFilter;
import com.hcmute.management.service.CustomOauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig  {
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    AppUserDetailService userDetailsService;
    @Autowired
    CustomOauth2Service customOauth2Service;
    @Autowired
    OAuth2Handler oAuth2Handler;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception {
        return authentication.getAuthenticationManager();
    }
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public GlobalExceptionHandler accessDeniedHandler() {
        return new GlobalExceptionHandler();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .exceptionHandling();
                ;
        ;
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers("/api/authenticate/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .userInfoEndpoint()
                .userService(customOauth2Service)
                .and()
                .successHandler(oAuth2Handler)
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET,"/api/user/**").hasAnyAuthority(UserPermission.USER_READ.getPermission())
//                .antMatchers("/api/admin/**").hasAnyAuthority(UserPermission.ADMIN_READ.getPermission(),UserPermission.ADMIN_WRITE.getPermission())
//                .anyRequest().authenticated();
//    //                .antMatchers("/**").permitAll()
//    //                .and()
//    //                .oauth2Login()
//    //                .authorizationEndpoint()
//    //                .baseUri("/oauth2/authorization")
//    //                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
//    //                .and()
//    //                .userInfoEndpoint()
//    //                .userService(customOauth2Service)
//    //                .and()
//    //                .successHandler(oAuth2Handler)
        ;
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
