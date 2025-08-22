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
    import org.springframework.web.cors.CorsConfigurationSource;

    import java.util.List;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtProvider jwtProvider;


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .sessionManagement(sessionManagementConfigurer ->
                            sessionManagementConfigurer
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(
                            exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/api/google/login", "/api/google/enroll").permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                    .build();
        }
    }
