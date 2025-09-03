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

        private static final List<String> whiteList = List.of(
                "/api/google/login",
                "/api/google/enroll",
                "/api/projects/home",
                "/api/blog/home",
                "/api/blogs/**", // 시연을 위한 임시 개방
                "/api/members/**", // 시연을 위한 임시 개방
                "/api/projects/**" // 시연을 위한 임시 개방
        );

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(org.springframework.security.config.Customizer.withDefaults())
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .sessionManagement(sessionManagementConfigurer ->
                            sessionManagementConfigurer
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(
                            exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers(whiteList.toArray(new String[0])).permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, whiteList), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                    .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.addAllowedOriginPattern("*");
            cfg.addAllowedMethod("*");
            cfg.addAllowedHeader("*");
            cfg.setAllowCredentials(true);
            cfg.setMaxAge(3600L);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", cfg);
            return source;
        }
    }
