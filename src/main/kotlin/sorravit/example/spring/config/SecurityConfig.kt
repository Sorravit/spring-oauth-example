package sorravit.example.spring.config

import sorravit.example.spring.jwt.JwtConfigurer
import sorravit.example.spring.jwt.JwtTokenProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@EnableWebSecurity
class SecurityConfig(private var jwtTokenProvider: JwtTokenProvider) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/auth/signin").permitAll()
            .anyRequest().authenticated()
            .and()
            .apply(JwtConfigurer(jwtTokenProvider))
    }
}
