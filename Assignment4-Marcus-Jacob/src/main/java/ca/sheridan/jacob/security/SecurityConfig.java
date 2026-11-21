package ca.sheridan.jacob.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}//encoder
	@Bean
	public SecurityFilterChain filterchain(HttpSecurity http) {
		return http
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/user/**").hasRole("USER")
						.requestMatchers("/user/**,admin/**").hasRole("ADMIN")
						.requestMatchers("/", "/css/**", "/images/**", "/permissionDenied", "/h2-console/**").permitAll()
						.requestMatchers("/**").denyAll()) // ** matches 2 or more path segments, ? matches one character
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
				.headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.formLogin(form -> form.loginPage("/login").permitAll()).logout(logout -> logout.permitAll())
				.exceptionHandling(exception -> exception.accessDeniedPage("/permissionDenied")).build();

	}// filterchain
}
