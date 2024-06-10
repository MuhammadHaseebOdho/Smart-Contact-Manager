package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "UserName Can't Be Empty.")
	@Size(min = 3,max = 20,message = "UserName Must be Between 3-20 Characters.")
	private String name;
	@Column(unique = true)
	@Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",message = "Invalid Email.")
	private String email;
	private String password;
	private boolean enabled;
	private String imageUrl;
	@Column(length = 500)
	@Size(min = 1,max = 500,message = "Characters Should be between 1-500.")
	private String about;
	private String role;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
	private List<Contact> contacts=new ArrayList<>();
	
	
	
	
	
	public User(int id, String name, String email, String password, boolean enabled, String imageUrl, String about,
			String role, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
		this.role = role;
		this.contacts = contacts;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", enabled=" + enabled +
				", imageUrl='" + imageUrl + '\'' +
				", about='" + about + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}
