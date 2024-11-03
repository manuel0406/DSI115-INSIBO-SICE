package com.dsi.insibo.sice.Seguridad.Configuraciones;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.context.annotation.Bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import java.io.IOException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracionSeguridad {
    
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    // CONFIGURACION DE FILTROS BASICOS
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        
        // He dejado activo el .csrf(csrf -> csrf.disable())
        return httpSecurity
                // ALWAYS = Siempre crea una nueva sesión HTTP, incluso si ya existe una sesión.
                // STATELESS =  No crea una sesión, pero utilizará una sesión existente si ya está presente.
                // NEVER = La sesión solo se crea si es requerida.
                // IF_REQUIERED = No crea ni utiliza sesiones HTTP en absoluto.
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); // Políticas de sesiones
                    session.maximumSessions(1).sessionRegistry(sessionRegistry()); // Número máximo de sesiones
                    session.sessionFixation().migrateSession(); // Previene la fijación de sesión migrando a una nueva sesión
                })
                .authorizeHttpRequests(http -> {
                    http.requestMatchers("/css/**", "/js/**", "/Imagenes/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/recuperarContra/**", "/enviarCorreo/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/correoDeRecuperacion/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/logout-success").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/login").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/login").permitAll();

                    //http.anyRequest().permitAll();
                    http.anyRequest().authenticated(); // AUTENTIFICACIÓN A TODOS
               })
                .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(successHandler()) // Manejador de éxito personalizado
                    .failureHandler(customAuthenticationFailureHandler)
                    .permitAll()
                )
               .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/logout-success")
                    .invalidateHttpSession(true)  // Invalidar la sesión
                    .deleteCookies("JSESSIONID")  // Borrar las cookies
                )
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    // CONTROLADOR DE SECCIONES INICIADAS
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                String targetUrl = determineTargetUrl(authentication);
                if (response.isCommitted()) {
                    return;
                }
                clearAuthenticationAttributes(request);
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }

            protected String determineTargetUrl(Authentication authentication) {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority().equals("ROLE_ADMINISTRADOR")) {
                        return "/gestionarCredenciales"; // URL para usuarios con rol ADMIN
                    }
                    if (authority.getAuthority().equals("ROLE_BIBLIOTECARIO")) {
                        return "/Biblioteca/"; // URL para usuarios con rol BIBLIOTECARIO

                    }
                }
                return "/"; // Por defecto, si no se encuentra ningún rol específico
            }
        };
    }

    // AUTENTIFICADOR DE USUARIOS
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());         // Contraseña
        //provider.setUserDetailsService(userDetailsService());   // Información del Usuario
        provider.setUserDetailsService(userDetailsServiceImpl);
        return provider;
    }

    // LEYES DE ENCRIPTAMIENTO DE CONTRASEÑAS
    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance(); // SOLO PARA PRUEBAS
        return new BCryptPasswordEncoder();
    }

    //USUARIO EN MEMORIA SOLO PARA Restablecimiento
    @Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("administrador")
                               .password(passwordEncoder().encode("admin123"))
                               .roles("ADMINISTRADOR")
                               .build());
        return manager;
    }

    //Obtener datos de la sesion
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


}
