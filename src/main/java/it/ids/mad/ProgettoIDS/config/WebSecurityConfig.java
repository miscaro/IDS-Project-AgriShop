package it.ids.mad.ProgettoIDS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import it.ids.mad.ProgettoIDS.security.AuthEntryPointJwt;
import it.ids.mad.ProgettoIDS.security.AuthTokenFilter;
import it.ids.mad.ProgettoIDS.security.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/signin", "/api/auth/signup").permitAll()
            // GET pubblici per consultazione catalogo
            .requestMatchers(org.springframework.http.HttpMethod.GET,
                "/api/catalogo/articoli", "/api/catalogo/prodotti", "/api/catalogo/materie-prime",
                "/api/articoli", "/api/prodotti", "/api/materie-prime", "/api/materie-prime/public", "/api/aziende/public").permitAll()
            .requestMatchers("/api/admin/**").hasAuthority("GESTOREPIATTAFORMA")
            .requestMatchers("/api/test/**").permitAll()
            .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/", "/dashboard/**", "/articoli", "/articoli/**", "/articolo/**", "/aziende", "/azienda/**", "/eventi", "/evento/**", "/carrello", "/ordini", "/css/**", "/js/**", "/images/**", "/login", "/register", "/h2-console/**", "/static/**", "/favicon.ico", "/favicon.svg").permitAll()
            .anyRequest().authenticated()
        );

        // Configurazione specifica per H2 console - permettere frame dalla stessa origine
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
