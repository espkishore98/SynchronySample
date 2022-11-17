package com.synchrony.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_images")
public class UserImages {
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private Long id;
	@Column(name="user_id")
	private Long userId;
	@Column(name="image_hash")
	private String imageHash;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getImageHash() {
		return imageHash;
	}
	public void setImageHash(String imageHash) {
		this.imageHash = imageHash;
	}
	@Override
	public String toString() {
		return "UserImages [id=" + id + ", userId=" + userId + ", imageHash=" + imageHash + "]";
	}
	public UserImages(Long id, Long userId, String imageHash) {
		super();
		this.id = id;
		this.userId = userId;
		this.imageHash = imageHash;
	}
	public UserImages() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
