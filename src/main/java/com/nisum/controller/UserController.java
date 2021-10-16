package com.nisum.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nisum.commons.MessageApi;
import com.nisum.dto.RequestService;
import com.nisum.dto.UserDTO;
import com.nisum.entity.Phone;
import com.nisum.entity.User;
import com.nisum.repo.IPhoneRepo;
import com.nisum.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IPhoneRepo phoneRepo;
	
	@PostMapping("/insert")
	private ResponseEntity<?> insertUser(@RequestBody UserDTO userDto) {
		
		if(userService.checkEmail(userDto.getUser().getEmail())) {
			return new ResponseEntity<MessageApi>(new MessageApi("El email ya se encuentra registrado") , HttpStatus.CONFLICT);
		}
		
		if(!userService.checkValidEmail(userDto.getUser().getEmail())) {
			return new ResponseEntity<MessageApi>(new MessageApi("El email no es valido") , HttpStatus.CONFLICT);
		}
		
		if(!userService.checkPassword(userDto.getUser().getPassword())) {
			return new ResponseEntity<MessageApi>(new MessageApi("El formato del password no es valido, debe contener una mayuscula, al menos una minuscula y dos números") , HttpStatus.CONFLICT);
		}
		
		RequestService request = userService.insertUser(userDto);
		
		return new ResponseEntity<RequestService>(request, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	private ResponseEntity<?> validLogin(@RequestBody User userLogin){

		Optional<User> userTemp = userService.validateAccesUser(userLogin.getEmail(), userLogin.getPassword(), userLogin.getUuidUser());
		if(userTemp != null) {
			userLogin.setId(userTemp.get().getId());
			userLogin.setNombre(userTemp.get().getNombre());
			userLogin.setEmail(userTemp.get().getEmail());
			userLogin.setPassword(userTemp.get().getPassword());
			userLogin.setCreatedDate(userTemp.get().getCreatedDate());			
			userLogin.setModifiedDate(userTemp.get().getModifiedDate());
			userLogin.setLastLogin(LocalDate.now());
			userLogin.setActive(userTemp.get().getActive());
			userLogin.setUuidUser(userTemp.get().getUuidUser());
			userService.updateUser(userLogin);
			return new ResponseEntity<MessageApi>(new MessageApi("Usuario autenticado"), HttpStatus.OK);
		}
		
		return new ResponseEntity<MessageApi>(new MessageApi("Usuario o password no validos"), HttpStatus.FORBIDDEN);
	}
	
	@PutMapping("/update/{userUuid}")
	private ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable UUID userUuid){
		Optional<User> queryUser = userService.findByUuid(userUuid);
		
		if(queryUser == null) {
			return new ResponseEntity<MessageApi>(new MessageApi("Usuario no encontrado"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
		if(!userService.checkValidEmail(user.getEmail())) {
			return new ResponseEntity<MessageApi>(new MessageApi("El email no es valido") , HttpStatus.CONFLICT);
		}
		
		if(!userService.checkPassword(user.getPassword())) {
			return new ResponseEntity<MessageApi>(new MessageApi("El formato del password no es valido, debe contener una mayuscula, al menos una minuscula y dos números") , HttpStatus.CONFLICT);
		}
		
		if(user.getNombre() != null) {
			queryUser.get().setNombre(user.getNombre());
		}
		
		if(user.getEmail() != null) {
			queryUser.get().setEmail(user.getEmail());
		}
		
		if(user.getPassword() != null) {
			queryUser.get().setPassword(user.getPassword());
		}
		
		if(user.getActive() != null) {
			queryUser.get().setActive(user.getActive());
		}
		
		queryUser.get().setModifiedDate(LocalDate.now());
		
		userService.updateUser(queryUser.get());
		
		return new ResponseEntity<MessageApi>(new MessageApi("Usuario actualizado exitosamente"), HttpStatus.OK);
	}
	
	@GetMapping("/users")
	private List<Phone> listPhone(){
		List<User> usuarios = userService.listUser();
		
		List<Phone> phone = phoneRepo.findByUserIn(usuarios);
		return phone;
	}

}
