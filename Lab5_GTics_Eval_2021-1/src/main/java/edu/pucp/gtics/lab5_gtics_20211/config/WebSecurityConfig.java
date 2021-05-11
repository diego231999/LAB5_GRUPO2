package edu.pucp.gtics.lab5_gtics_20211.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.formLogin()
                .loginPage("/user/signIn") // for the Controlller
                .loginProcessingUrl("/processLogin"); // for the POST request of the login form

        httpSecurity.logout();

        httpSecurity.authorizeRequests()
                .antMatchers("/a/*", "/a/**").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().permitAll();
               
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())

                .usersByUsernameQuery("select correo, password, enabled from usuarios WHERE correo = ?")
                .authoritiesByUsernameQuery("select correo, autorizacion from usuarios where enabled=0x01 and correo = ?");


    }

}