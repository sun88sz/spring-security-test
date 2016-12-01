package com.security.extend;

import com.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sun on 16/11/28. 主要获取request参数
 */
public class MyAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		String s = request.getParameter("companyId");
		Long companyId = null;
		if (StringUtils.isNumeric(s)) {
			companyId = Long.parseLong(s);
		}

		String s2 = request.getParameter("id");
		Long id = null;
		if (StringUtils.isNumeric(s2)) {
			id = Long.parseLong(s2);
		}

		username = username.trim();

		MyUsernamePasswordAuthenticationToken authRequest = new MyUsernamePasswordAuthenticationToken(username,
				password, id, companyId);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
