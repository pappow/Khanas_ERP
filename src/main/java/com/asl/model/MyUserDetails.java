package com.asl.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.asl.entity.Xusers;
import com.asl.entity.Zbusiness;
import com.asl.enums.UserRole;

import lombok.ToString;

/**
 * @author Zubayer Ahamed
 * @since 27-11-2020
 *
 */
@ToString
public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = 112410652033677128L;

	private String fullName;
	private String username;
	private String password;
	private String emailAddress;
	private String businessId;
	private Zbusiness zbusiness;
	private boolean accountExpired;
	private boolean credentialExpired;
	private boolean accountLocked;
	private boolean enabled;
	private String roles;
	private List<GrantedAuthority> authorities;

	public MyUserDetails(Xusers user, Zbusiness zbusiness) {
		this.fullName = user.getXname();
		this.username = user.getZemail();
		this.password = user.getXpassword();
		this.businessId = user.getZid();
		this.zbusiness = zbusiness;
		this.accountExpired = false;
		this.credentialExpired = false;
		this.accountLocked = !Boolean.TRUE.equals(user.getZactive());
		this.enabled = Boolean.TRUE.equals(user.getZactive());
		this.roles = StringUtils.isBlank(user.getRoles()) ? UserRole.SUBSCRIBER.getCode() : user.getRoles();
		this.authorities = Arrays.stream(roles.split(","))
									.map(SimpleGrantedAuthority::new)
									.collect(Collectors.toList());
	}

	public String getFullName() {
		return this.fullName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getBusinessId() {
		return businessId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getRoles() {
		return roles;
	}

	public Zbusiness getZbusiness() {
		return this.zbusiness;
	}

}
