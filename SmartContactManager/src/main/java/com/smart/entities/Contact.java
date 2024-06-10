package com.smart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int contactId;
	private String name;
	private String nickName;
	private String phoneNumber;
	private String work;
	@Column(unique = true)
	private String email;
	private String imageUrl;
	@Column(length = 1000)
	private String description;
	@ManyToOne
	private User user;
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contact(int contactId, String name, String nickName, String phoneNumber, String work, String email,
			String imageUrl, String description, User user) {
		super();
		this.contactId = contactId;
		this.name = name;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
		this.work = work;
		this.email = email;
		this.imageUrl = imageUrl;
		this.description = description;
		this.user = user;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
	
	
}
