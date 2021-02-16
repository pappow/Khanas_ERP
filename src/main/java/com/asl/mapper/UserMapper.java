package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Users;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Mapper
public interface UserMapper {

	public Users save(Users users);
	public Users update(Users users);
	public List<Users> getAllUser();
	public List<Users> findByUsername(String username);
	public List<Users> findByUsernameAndPassword(String username, String password);
	public Users findByUsernameAndBusinessId(String username, String password);
}
