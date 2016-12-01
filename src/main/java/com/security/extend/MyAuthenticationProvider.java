package com.security.extend;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * Created by Sun on 16/11/28.
 * 主要把UsernamePasswordAuthenticationToken扩展类的额外参数传递给UserDetailsService；
 */
public class MyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

	// ~ Instance fields
	// ================================================================================================

	private PasswordEncoder passwordEncoder;

	private String userNotFoundEncodedPassword;

	private IMyUserDetailsService userDetailsService;

	public MyAuthenticationProvider() {

	}

	@SuppressWarnings("deprecation")
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	public void setPasswordEncoder(Object passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		setPasswordEncoder((PasswordEncoder) passwordEncoder);
	}

	private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
		this.userNotFoundEncodedPassword = passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
		this.passwordEncoder = passwordEncoder;
	}

	protected PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	protected void doAfterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
	}

	public void setUserDetailsService(IMyUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public IMyUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser;
		MyUsernamePasswordAuthenticationToken myauthentication = (MyUsernamePasswordAuthenticationToken) authentication;

		try {
			// 获取用户
			loadedUser = this.getUserDetailsService().loadUserByUsername(username, myauthentication.getCompanyId());

			System.out.println("xxxxx");
		} catch (UsernameNotFoundException notFound) {
			if (authentication.getCredentials() != null) {
				String presentedPassword = authentication.getCredentials().toString();
				getPasswordEncoder().matches(presentedPassword, userNotFoundEncodedPassword);
			}
			throw notFound;
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new InternalAuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}
}
