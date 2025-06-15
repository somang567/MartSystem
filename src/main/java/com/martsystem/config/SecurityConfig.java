package com.martsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (개발 편의상, 실제 배포 시에는 CSRF 토큰 고려)
				.authorizeHttpRequests(authorize -> authorize
						// 회원가입 관련 모든 URL 및 정적 리소스에 대해 모든 사용자 접근 허용
						.requestMatchers(
								new AntPathRequestMatcher("/login"),
								new AntPathRequestMatcher("/register/**"),
								new AntPathRequestMatcher("/css/**"),
								new AntPathRequestMatcher("/js/**"),
								new AntPathRequestMatcher("/images/**"),
								new AntPathRequestMatcher("/fonts/**"),   // **이 줄을 추가해야 합니다!**
								new AntPathRequestMatcher("/")
						).permitAll()
						.requestMatchers("api/**").permitAll()
						// 나머지 모든 요청은 인증 필요
						.anyRequest().authenticated()
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.defaultSuccessUrl("/")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll()
				);

		return http.build();
	}
}