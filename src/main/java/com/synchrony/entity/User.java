package com.synchrony.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(name="externalId")
	private String externalId;
	@Column(name="user_name")
	private String userName;
	@Column(name="password")
	private String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	public User( String externalId, String userName, String password) {
		super();
		
		this.externalId = externalId;
		this.userName = userName;
		this.password = password;
	}
	
	public User(Long id, String externalId, String userName, String password) {
		super();
		this.id = id;
		this.externalId = externalId;
		this.userName = userName;
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", externalId=" + externalId + ", userName=" + userName + ", password=" + password
				+ "]";
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
