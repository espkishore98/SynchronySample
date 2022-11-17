package com.synchrony.domain;

import java.io.Serializable;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class AuthUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private String userName;
	private String firstName;
	private String lastName;
	private Calendar expiration;
	private Long sub;
	private String jti;
	private String loginSource;
	private String tawkHash;
	private String userType;
	private String gdprEmailPolicy;
	private Boolean firstTimeSignedIn;

	public AuthUser() {
		super();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getExpiration() {
		return expiration;
	}

	public void setExpiration(Calendar expiration) {
		this.expiration = expiration;
	}

	public Long getSub() {
		return sub;
	}

	public void setSub(Long sub) {
		this.sub = sub;
	}

	public String getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(String loginSource) {
		this.loginSource = loginSource;
	}

	public Boolean getFirstTimeSignedIn() {
		return firstTimeSignedIn;
	}

	public void setFirstTimeSignedIn(Boolean firstTimeSignedIn) {
		this.firstTimeSignedIn = firstTimeSignedIn;
	}

	public String getTawkHash() {
		return tawkHash;
	}

	public void setTawkHash(String tawkHash) {
		this.tawkHash = tawkHash;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getGdprEmailPolicy() {
		return gdprEmailPolicy;
	}

	public void setGdprEmailPolicy(String gdprEmailPolicy) {
		this.gdprEmailPolicy = gdprEmailPolicy;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	@Override
	public String toString() {
		return "AuthUser [id=" + id + ", email=" + email + ", userName=" + userName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", expiration=" + expiration + ", sub=" + sub + ", jti=" + jti
				+ ", loginSource=" + loginSource + ", tawkHash=" + tawkHash + ", userType=" + userType
				+ ", gdprEmailPolicy=" + gdprEmailPolicy + ", firstTimeSignedIn=" + firstTimeSignedIn + "]";
	}

	

}
