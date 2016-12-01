package com.security.extend;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public class MyUserDetailsService implements IMyUserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username, Long companyId) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("用户名为空");
		}

		// Login login = loginService.findByUsername(username).orElseThrow(() ->
		// new UsernameNotFoundException("用户不存在"));

		Set<GrantedAuthority> authorities = new HashSet<>();
		// roleService.getRoles(login.getId()).forEach(r -> authorities.add(new
		// SimpleGrantedAuthority(r.getName())));

		User user = new User(username, "$2a$10$93z8UuvxraEPuY2VJj.1re3D398TQlLFgh1WqgIUh7gSPIUU.m9s.", // 密码
				true, // 是否可用
				true, // 是否过期
				true, // 证书不过期为true
				true, // 账户未锁定为true
				authorities);

		return user;
	}

}