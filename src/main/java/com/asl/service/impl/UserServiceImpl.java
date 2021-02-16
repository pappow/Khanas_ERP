package com.asl.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Users;
import com.asl.entity.Users;
import com.asl.enums.RecordStatus;
import com.asl.mapper.UserMapper;
import com.asl.repository.UserRepository;
import com.asl.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Slf4j
@Service
public class UserServiceImpl extends AbstractGenericService implements UserService {

	@Autowired private UserMapper userMapper;

	@Override
	@Transactional
	public Users save(Users Users) {
		if(Users == null) return null;
		Users.setUsername(modifyUsername(Users.getUsername()));
		//Users.setBusinessId(sessionManager.getBusinessId());
		return userMapper.save(Users);
	}

	@Override
	public String modifyUsername(String username) {
		if(StringUtils.isBlank(username)) return "";
		username = username.trim();
		username = username.replace(" ", "-");
		return username;
	}

	@Override
	public List<Users> findByUsernameOnly(String username) {
		List<Users> list = new ArrayList<>();
		List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM Users WHERE zemail='"+ username +"'");
		result.stream().forEach(m -> {
			Users u = new Users();
			u.setUsername((String) m.get("zemail"));
			u.setPswd((String) m.get("xpassword"));
			u.setZid((Integer) m.get("zid"));
			u.setZactive((String) m.get("zactive"));
			u.setStatus("L".equalsIgnoreCase((String) m.get("status")) ? RecordStatus.L : RecordStatus.D);
			list.add(u);
		});

		return list;
	}

	@Override
	public List<Users> findBByUsernameAndPassword(String username, String password) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) return null;

		List<Users> list = new ArrayList<>();
		List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM Users WHERE zemail='"+ username +"' AND xpassword='"+ password +"' AND zactive='1'");
		result.stream().forEach(m -> {
			Users u = new Users();
			u.setUsername((String) m.get("zemail"));
			u.setPswd((String) m.get("xpassword"));
			u.setZid((Integer) m.get("zid"));
			u.setZactive((String) m.get("zactive"));
			u.setStatus("L".equalsIgnoreCase((String) m.get("status")) ? RecordStatus.L : RecordStatus.D);
			list.add(u);
		});
		return list;
	}

	@Override
	public Users findBByUsernameAndBusinessId(String username, String businessId) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(businessId)) return null;
	
		List<Users> list = em.createQuery("SELECT u FROM Users u WHERE u.zemail=:username AND u.zid=:zid", Users.class)
				.setParameter("username", username)
				.setParameter("zid", Integer.valueOf(businessId))
				.getResultList();

		return list == null ? null : list.stream().findFirst().orElse(null);
	}

	@Override
	public List<Users> getAllUser() {
		List<Users> resultList = em.createQuery("SELECT u FROM Users u WHERE u.zid=:zid", Users.class)
								.setParameter("zid", Integer.parseInt(sessionManager.getBusinessId()))
								.getResultList();
		if(resultList == null) return Collections.emptyList();
		return resultList;
	}


}
