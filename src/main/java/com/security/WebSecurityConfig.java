package com.security;

import com.security.extend.MyAuthenticationProcessingFilter;
import com.security.extend.MyAuthenticationProvider;
import com.security.extend.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("com")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@PropertySource("classpath:application.yml")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MyUserDetailsService userDetailsService;
	// @Autowired
	// private PersistentTokenRepository tokenRepository;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public MyAuthenticationProcessingFilter authenticationProcessingFilter() throws Exception {
		MyAuthenticationProcessingFilter tokenProcessingFilter = new MyAuthenticationProcessingFilter();
		tokenProcessingFilter.setAuthenticationManager(authenticationManagerBean());
		return tokenProcessingFilter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Bean
	public MyAuthenticationProvider authenticationProvider() {
		MyAuthenticationProvider provider = new MyAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);

		return provider;
	}

	/**
	 * HttpSecurity：一般用它来具体控制权限，角色，url等安全的东西。
	 * AuthenticationManagerBuilder：用来做登录认证的。具体的注释，看org.springframework.security
	 * .config.annotation.web.configuration 包的 WebSecurityConfigurerAdapter 类的
	 * protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {...}方法的注释，很清楚，注释也教了怎么用这个东西。 WebSecurity：For example, if you
	 * wish to ignore certain requests.
	 * 
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.parentAuthenticationManager(authenticationManagerBean());
		// 如果不设置 No AuthenticationProvider found for
		// com.security.extend.MyUsernamePasswordAuthenticationToken

		auth.authenticationProvider(authenticationProvider());

		// 此行开启则自动调用userDetailsService中的getByUsername方法
		// auth.userDetailsService(userDetailsService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/**/favicon.ico");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin().disable()// disable
				.csrf().disable() // X-Frame-Options
				//
				// .accessDecisionManager(accessDecisionManager)//用注解替换,如果不使用注解，取消注释

				.authorizeRequests().anyRequest().authenticated()// 其他url需要鉴权

				.antMatchers("/", "/home").permitAll()// 无需登录验证

				//使用自定义Filter
				.and().addFilter(authenticationProcessingFilter()).formLogin().loginPage("/login").permitAll()// 登录接口无需验证

				// .and().formLogin().usernameParameter("username")
				// .passwordParameter("password").loginProcessingUrl("/login").loginPage("/login")

				// 登出
				.and().logout().deleteCookies("remove").invalidateHttpSession(false).logoutUrl("/custom-logout")
				.logoutSuccessUrl("/logout-success");

		// .and().addFilter(authenticationProcessingFilter()).formLogin().usernameParameter("username")
		// .passwordParameter("password").loginProcessingUrl("/login").loginPage("/login")
		// .failureUrl("/login?error").permitAll().and().logout().deleteCookies("JSESSIONID")
		// .logoutRequestMatcher(new
		// AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests()
	// .and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/",
	// true)
	// .and().logout().logoutUrl("/logout")
	// .and().sessionManagement().maximumSessions(1).expiredUrl("/expired")
	// .and()
	// .and().exceptionHandling().accessDeniedPage("/accessDenied");
	// }

	// @Bean
	// public RememberMeServices rememberMeServices() {
	// // Key must be equal to rememberMe().key()
	// PersistentTokenBasedRememberMeServices rememberMeServices = new
	// PersistentTokenBasedRememberMeServices("key",
	// userDetailsService, tokenRepository);
	// rememberMeServices.setCookieName("remember-me");
	// rememberMeServices.setParameter("remember-me");
	// rememberMeServices.setTokenValiditySeconds(864000);
	// return rememberMeServices;
	// }

	public static void main(String[] args) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String encode = encoder.encode("1");

		boolean matches = encoder.matches("123456", encode);
		System.out.println(encode);
		System.out.println(matches);
	}

}