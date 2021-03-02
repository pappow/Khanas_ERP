package com.asl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.Xusers;
import com.asl.service.XusersService;

@SpringBootTest
public class XusersTest {

	@Autowired private XusersService xusersService;

	@Test
	void testXusersSave() {
		Xusers user = getCentralUser();
		long count = xusersService.update(user);
		System.out.println(count);
	}

	private Xusers getCentralUser() {
		Xusers user = new Xusers();
		user.setZemail("b1");
		user.setZactive(Boolean.TRUE);
		user.setZid("900010");
		user.setXpassword("1234");
		user.setSystemadmin(true);
		return user;
	}
	
	
}
