package com.security.extend;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Sun on 16/11/29.
 */
public interface IMyUserDetailsService {

	UserDetails loadUserByUsername(String username, Long companyId) throws UsernameNotFoundException;

}
