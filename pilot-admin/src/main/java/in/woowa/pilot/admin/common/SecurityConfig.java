package in.woowa.pilot.admin.common;

import in.woowa.pilot.admin.security.CustomOAuth2UserService;
import in.woowa.pilot.admin.security.OAuth2AuthenticationFailureHandler;
import in.woowa.pilot.admin.security.OAuth2AuthenticationSuccessHandler;
import in.woowa.pilot.admin.security.OAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] ADMIN_PATHS = new String[]{
            "/api/owners/**",
            "/api/orders/**",
            "/api/rewards/**",
            "/api/settles/**",
            "/api/members/**",
            "/api/authorities/**",
    };
    public static final String[] PERMIT_ALL_PATHS = {
            "/",
            "/login",
            "/error",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/auth/**",
            "/oauth2/**",
            "/h2-console/**"
    };

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                .oauth2Login()
                .disable();
        http
                .authorizeRequests()
                .antMatchers(PERMIT_ALL_PATHS)
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/authorities")
                .hasAnyRole("NORMAL", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/members")
                .hasAnyRole("NORMAL", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/members/all")
                .hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, ADMIN_PATHS)
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, ADMIN_PATHS)
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, ADMIN_PATHS)
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, ADMIN_PATHS)
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated();

        http
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(oAuth2AuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(exceptionHandlerFilter, LogoutFilter.class);
    }

    public static boolean shouldNotAuthentication(HttpServletRequest request) {
        return Arrays.stream(PERMIT_ALL_PATHS)
                .anyMatch(path -> new AntPathRequestMatcher(path).matches(request));
    }
}
