package com.smart.smartContactManager.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name = "CONTACT")
public class Contact {

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int cid;
	private String name;
	private int phone;
	private String nickName;
	private String work;
	private String email;
	private String image;
	@Column(length = 5000)
	private String Description;
	
	@ManyToOne
	private User user;
	
	

    public int getCid() {
		return cid;
	}



	public void setCid(int cid) {
		this.cid = cid;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getPhone() {
		return phone;
	}



	public void setPhone(int phone) {
		this.phone = phone;
	}



	public String getNickName() {
		return nickName;
	}



	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	

	public String getDescription() {
		return Description;
	}

    public void setDescription(String description) {
		Description = description;
	}
    
    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	public User getUser() 
	  { 
		 return user; 
		  }
	  
	 public void setUser(User user) { 
		 this.user = user;
		  }



	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cid==((Contact)obj).getCid();
	}
	 

	
	
	
	
}