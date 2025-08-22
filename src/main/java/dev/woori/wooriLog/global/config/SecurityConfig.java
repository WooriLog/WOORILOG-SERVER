    package dev.woori.wooriLog.global.config;

    import dev.woori.wooriLog.global.auth.jwt.JwtAuthenticationEntryPoint;
    import dev.woori.wooriLog.global.auth.jwt.JwtProvider;
    import dev.woori.wooriLog.global.filter.ExceptionHandlerFilter;
    import dev.woori.wooriLog.global.filter.JwtAuthenticationFilter;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtProvider jwtProvider;

        private static final String[] whiteList = {
                "/api/google/login",
                "/api/google/enroll"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .sessionManagement(sessionManagementConfigurer ->
                            sessionManagementConfigurer
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(
                            exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                    .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                            authorizationManagerRequestMatcherRegistry
                                    .anyRequest()
                                    .authenticated())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                    .build();
        }
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return web -> web.ignoring().requestMatchers(whiteList);
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 origin
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true); // 쿠키/인증정보 포함 허용

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
    }
