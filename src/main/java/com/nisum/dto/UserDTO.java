package com.nisum.dto;

import java.util.List;

import com.nisum.entity.Phone;
import com.nisum.entity.User;

public class UserDTO {
	
	private User user;
	
	private List<Phone> phone;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Phone> getPhone() {
		return phone;
	}

	public void setPhone(List<Phone> phone) {
		this.phone = phone;
	}

}
