package com.nisum.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nisum.entity.Phone;
import com.nisum.entity.User;

public interface IPhoneRepo extends JpaRepository<Phone, Integer>{
	
	@Modifying
	@Query(value = "INSERT INTO phone (number, city_code, country_code, fk_cliente) VALUES (:number, :city_code, :country_code, :cliente)", nativeQuery = true)
	Integer insertPhone(@Param("number") String phone, @Param("city_code") Integer city_code, @Param("country_code") Integer country_code, @Param("cliente") Integer cliente);
	
	List<Phone> findByUserIn(List<User> id);

}
