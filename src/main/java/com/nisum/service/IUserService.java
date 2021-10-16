package com.nisum.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.nisum.dto.RequestService;
import com.nisum.dto.UserDTO;
import com.nisum.entity.User;

public interface IUserService {

	RequestService insertUser(UserDTO userDto);

	boolean checkEmail(String email);

	boolean checkValidEmail(String email);

	boolean checkPassword(String password);

	Optional<User> validateAccesUser(String email, String password, UUID uuid);

	User updateUser(User user);
	
	Optional<User> findByUuid(UUID uuid);
	
	List<User> listUser();

}
