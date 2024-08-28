package com.pro.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pro.entites.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email")String email);
	//pagination
	
	@Query("SELECT u FROM User u WHERE u.role = 'ROLE_ADMIN'")
	public List<User> findRoleBy(@Param("role")String role);
	
	
	@Query("SELECT u FROM User u WHERE u.role = 'ROLE_USER'")
	public List<User> findRoleByUser(@Param("role")String role);

}
