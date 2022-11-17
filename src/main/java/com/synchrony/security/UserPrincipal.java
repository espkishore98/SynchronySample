package com.synchrony.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synchrony.entity.User;

public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;

	private String userName;

	

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String userName, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserPrincipal create(User user) {

		return new UserPrincipal(user.getId(), user.getUserName(), user.getPassword(), null);
	}

	public Long getId() {
		return id;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}