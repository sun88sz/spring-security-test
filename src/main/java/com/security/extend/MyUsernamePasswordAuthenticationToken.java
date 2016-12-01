package com.security.extend;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by Sun on 16/11/28.
 *
 * 增加认证所用到的额外参数
 *
 */
public class MyUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private Long id;
	private Long companyId;

	public Long getId() {
		return id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public MyUsernamePasswordAuthenticationToken(Object principal, Object credentials, Long id, Long companyId) {
		super(principal, credentials);
		this.id = id;
		this.companyId = companyId;
	}

	public MyUsernamePasswordAuthenticationToken(Object principal, Object credentials, Long id, Long companyId,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.id = id;
		this.companyId = companyId;
	}

}
