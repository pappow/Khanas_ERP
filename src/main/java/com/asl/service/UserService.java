package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Users;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Component
public interface UserService {

	public Users save(Users users);
	public List<Users> findByUsernameOnly(String username);
	public List<Users> findBByUsernameAndPassword(String username, String password);
	public Users findBByUsernameAndBusinessId(String username, String businessId);
	public List<Users> getAllUser();
	public String modifyUsername(String username);
}
