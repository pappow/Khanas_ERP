package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Xusers;
import com.asl.mapper.XusersMapper;
import com.asl.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Service
public class UserServiceImpl extends AbstractGenericService implements XusersService {

	@Autowired
	private XusersMapper userMapper;

	@Override
	public long save(Xusers xusers) {
		if (xusers == null) return 0;
		xusers.setZemail(modifyZemail(xusers.getZemail()));
		xusers.setZid(sessionManager.getBusinessId());
		return userMapper.save(xusers);
	}

	@Override
	public long update(Xusers user) {
		if (user == null) return 0;
		user.setZid(sessionManager.getBusinessId());
		return userMapper.update(user);
	}

	@Override
	public String modifyZemail(String username) {
		if (StringUtils.isBlank(username)) return "";
		username = username.trim();
		username = username.replace(" ", "-");
		return username;
	}

	@Override
	public List<Xusers> findByZemail(String zemail) {
		if (StringUtils.isBlank(zemail)) return Collections.emptyList();

		List<Xusers> list = userMapper.findByZemail(zemail);
		if (list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public List<Xusers> findByZemailAndXpassword(String zemail, String xpassword) {
		if (StringUtils.isBlank(zemail) || StringUtils.isBlank(xpassword)) return Collections.emptyList();

		List<Xusers> list = userMapper.findByZemailAndXpassword(zemail, xpassword);
		if (list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public Xusers findByZemailAndZid(String zemail, String zid) {
		if (StringUtils.isBlank(zemail) || StringUtils.isBlank(zid)) return null;
		return userMapper.findByZemailAndZid(zemail, zid);
	}

	@Override
	public List<Xusers> getAllXusers() {
		List<Xusers> resultList = userMapper.getAllXusers(sessionManager.getBusinessId());
		if (resultList == null) return Collections.emptyList();
		return resultList;
	}

}
