package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Users;
import com.asl.mapper.UserMapper;
import com.asl.service.UserService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Service
public class UserServiceImpl extends AbstractGenericService implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public long save(Users user) {
		if (user == null) return 0;
		user.setUsername(modifyUsername(user.getUsername()));
		user.setZid(sessionManager.getBusinessId());
		return userMapper.save(user);
	}

	@Override
	public long update(Users user) {
		if (user == null) return 0;
		user.setZid(sessionManager.getBusinessId());
		return userMapper.update(user);
	}

	@Override
	public String modifyUsername(String username) {
		if (StringUtils.isBlank(username)) return "";
		username = username.trim();
		username = username.replace(" ", "-");
		return username;
	}

	@Override
	public List<Users> findByUsernameOnly(String username) {
		if (StringUtils.isBlank(username)) return Collections.emptyList();

		List<Users> list = userMapper.findByUsername(username);
		if (list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public List<Users> findBByUsernameAndPassword(String username, String password) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) return Collections.emptyList();

		List<Users> list = userMapper.findByUsernameAndPassword(username, password);
		if (list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public Users findBByUsernameAndBusinessId(String username, String businessId) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(businessId)) return null;
		return userMapper.findByUsernameAndBusinessId(username, businessId);
	}

	@Override
	public List<Users> getAllUser() {
		List<Users> resultList = userMapper.getAllUser(sessionManager.getBusinessId());
		if (resultList == null) return Collections.emptyList();
		return resultList;
	}

}
