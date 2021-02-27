package com.asl.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asl.entity.Xusers;
import com.asl.model.MyUserDetails;
import com.asl.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired private XusersService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(StringUtils.isBlank(username)) {
			throw  new UsernameNotFoundException("User not found in the system");
		}

		String[] token = username.split("\\|");
		if(token.length < 2) throw  new UsernameNotFoundException("User not found in the system");

		String xusername = token[0];
		String businessId = token[1];

		Xusers xuser = userService.findByZemailAndZid(xusername, businessId);
		if(xuser == null) throw  new UsernameNotFoundException("User not found in the system");
		return new MyUserDetails(xuser);
	}

}
