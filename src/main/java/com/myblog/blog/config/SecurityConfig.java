package com.myblog.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.myblog.blog.config.auth.PrincipalDetailService;

// 아래 세 어노테이션은 시큐리티 기본으로 걸어야 되는 3가지 어노테이션이다.
@Configuration // 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것 (IoC관리)
@EnableWebSecurity // 시큐리티 필터 추가 (모든 요청을 가로채서 컨트롤러에서 함수 실행 전에 동작하는 시큐리티 필터 추가) (그 시큐리티 필터 처리 설정을 해당 파일에서 하겠다.)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	// 비밀번호 암호화
	@Bean
	public BCryptPasswordEncoder encodePWD( ) {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인할 경우 password를 가로채기 하는데
	// 해당 password가 뭘로 해쉬 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 db에 있는 해쉬랑 비교할 수 있다.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
    		.csrf().disable() // csrf 토큰 비활성화 (javascript 요청시 csrf가 막는다. 테스트시는 편리상 비활성화)
    		.authorizeRequests() // 인증확인 여부 요청이 들어오면..
    			.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**").permitAll() // auth경로로 들어오는 요청은 누구나 들어올 수 있다. (.permitAll())
    			.anyRequest().authenticated() // 위가 아닌 다른 모든 요청들은 인증이 필요하다.(.authenticated())
    		.and()
        		.formLogin() // 인증(로그인)이 필요할 경우 아래의 경로로 이동한다. ("/auth/loginForm")
        		.loginPage("/auth/loginForm") // 로그인 페이지
        		.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인작업를 한다.
        		.defaultSuccessUrl("/"); // 로그인 성공시 이동할 경로
	}
	
	// 시큐리티가 대신 로그인할 경우 password를 가로채기 하는데
	// 해당 password가 뭘로 해쉬 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 db에 있는 해쉬랑 비교할 수 있다.
//	@Bean
//    public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
//        EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
//            EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
//        contextSourceFactoryBean.setPort(0);
//        return contextSourceFactoryBean;
//    }
//
//    @Bean
//    public AuthenticationManager ldapAuthenticationManager(BaseLdapPathContextSource contextSource) {
//        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
//        factory.setUserDnPatterns("uid={0},ou=people");
//        factory.setUserDetailsContextMapper(new PersonContextMapper());
//        return factory.createAuthenticationManager();
//    }
	
//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//        	.csrf().disable() // csrf 토큰 비활성화 (javascript 요청시 csrf가 막는다. 테스트시는 편리상 비활성화)
//            .authorizeRequests() // 인증확인 여부 요청이 들어오면..
//            	.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**").permitAll() // auth경로로 들어오는 요청은 누구나 들어올 수 있다. (.permitAll())
//            	.anyRequest().authenticated() // 위가 아닌 다른 모든 요청들은 인증이 필요하다.(.authenticated())
//            .and()
//            	.formLogin() // 인증(로그인)이 필요할 경우 아래의 경로로 이동한다. ("/auth/loginForm")
//            	.loginPage("/auth/loginForm") // 로그인 페이지
//            	.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인작업를 한다.
//        		.defaultSuccessUrl("/"); // 로그인 성공시 이동할 경로
//        return http.build();
//    }
	
//	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//		@Override
//		public void configure(HttpSecurity http) throws Exception {
//			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//			http
//					.addFilter(corsConfig.corsFilter())
//					.addFilter(new JwtAuthenticationFilter(authenticationManager))
//					.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
//		}
//	}
	
//	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//	    @Override
//	    public void configure(HttpSecurity http) throws Exception {
//	        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//	        http.addFilter(new CustomAuthenticationFilter(authenticationManager));
//	    }
//
//	    public static MyCustomDsl customDsl() {
//	        return new MyCustomDsl();
//	    }
//	}
}
