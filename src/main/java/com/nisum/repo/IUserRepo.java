package com.nisum.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nisum.entity.User;

public interface IUserRepo extends JpaRepository<User, Integer>{
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByEmailAndPasswordAndUuidUserAndActive(String email, String password, UUID uuidUser, boolean Active);
	
	public Optional<User> findByUuidUser(UUID uuidUser);

}
