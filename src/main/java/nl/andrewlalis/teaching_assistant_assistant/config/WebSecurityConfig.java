package nl.andrewlalis.teaching_assistant_assistant.config;

import nl.andrewlalis.teaching_assistant_assistant.model.security.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for the TAA application.
 *
 * This configuration makes use of the custom user details service provided by the application for database-persistent
 * user accounts.
 *
 * Login, logout, and registration pages are set so that all users, authenticated and unauthenticated, may access them,
 * while actual site content is only visible to authenticated users.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    protected WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable() // So that we can GET the logout page.

                .authorizeRequests() // Let anyone view the login and logout pages, as well as various registration pages.
                    .antMatchers("/login*", "/logout*", "/register*", "/register/**")
                        .permitAll()
                        .and()

                .authorizeRequests()
                    .antMatchers("/css/**", "/images/**", "/javascript/**")
                    .permitAll()
                    .and()

                .authorizeRequests() // Only logged in users should be able to see site content.
                    .antMatchers("/**").hasRole("user")
                    .anyRequest().hasRole("user")
                    .and()

                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error")
                    .and()

                .logout()
                    .permitAll()
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID");
    }

    /**
     * Configures Spring Security to use a specific password encoder.
     * @return The password encoder to use.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
