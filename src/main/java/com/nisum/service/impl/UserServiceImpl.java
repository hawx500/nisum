package com.nisum.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nisum.dto.RequestService;
import com.nisum.dto.UserDTO;
import com.nisum.entity.User;
import com.nisum.repo.IPhoneRepo;
import com.nisum.repo.IUserRepo;
import com.nisum.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{
	
	public static final String regExpEmail = "^(.+)@(.+)$";
	
	public static final String regExpPassword = "(?=.*\\d{2})(?=.*[a-z])(?=.*[A-Z]).*";
	
	@Autowired
	private IUserRepo userRepo;
	
	@Autowired
	private IPhoneRepo phoneRepo;

	@Override
	@Transactional
	public RequestService insertUser(UserDTO userDto) {
		User user = new User();
		UUID uuid = UUID.randomUUID();
		
		user.setNombre(userDto.getUser().getNombre());
		user.setEmail(userDto.getUser().getEmail());
		user.setPassword(userDto.getUser().getPassword());
		user.setCreatedDate(LocalDate.now());
		user.setLastLogin(null);
		user.setModifiedDate(null);
		user.setActive(true);
		user.setUuidUser(uuid);
		userRepo.save(user);
		
		userDto.getPhone().forEach(pho -> phoneRepo.insertPhone(pho.getNumber(), pho.getCityCode(), pho.getCountryCode(), user.getId()));
		
		RequestService request = new RequestService();
		request.setId(uuid);
		request.setCreatedDate(LocalDate.now());
		request.setModifiedDate(null);
		request.setLastLogin(LocalDate.now());
		request.setActive(true);
		return request;
	}

	@Override
	public boolean checkEmail(String email) {
		Optional<User> data = userRepo.findByEmail(email);
		if(data.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkValidEmail(String email) {
		Pattern patternRegExp = Pattern.compile(regExpEmail);
		Matcher validRegExp = patternRegExp.matcher(email);
		if(validRegExp.matches()) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean checkPassword(String password) {
		Pattern patternRegExp = Pattern.compile(regExpPassword);
		Matcher validRegExp = patternRegExp.matcher(password);
		if(validRegExp.matches()) {
			return true;
		}
		return false;
	}
	
	public User updateUser(User user) {
		User userUpdate = userRepo.saveAndFlush(user);
		return userUpdate;
	}

	@Override
	public Optional<User> validateAccesUser(String email, String password, UUID uuid) {
		Optional<User> login = userRepo.findByEmailAndPasswordAndUuidUserAndActive(email, password, uuid, true);
		if(login.isPresent()) {
			return login;
		}
		return null;
	}

	@Override
	public Optional<User> findByUuid(UUID uuid) {
		Optional<User> user = userRepo.findByUuidUser(uuid);
		if(user.isPresent()) {
			return user;
		}
		return null;
	}
	
	public List<User> listUser(){
		List<User> listUser = userRepo.findAll();
		return listUser;
	}

}
