package com.asl;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.UserAuditRecord;
import com.asl.entity.Users;
import com.asl.service.UserAuditRecordService;
import com.asl.service.UserService;

@SpringBootTest
class AslErpApplicationTests {

	@Autowired private UserService userService;
	@Autowired private UserAuditRecordService userAuditRecordService;

	@Test
	void saveUserAuditRecord() {
		UserAuditRecord aur = new UserAuditRecord();
		aur.setUserId("admin");
		aur.setBusinessId("D900010");
		aur.setRecordDate(new Date());
		long count = userAuditRecordService.save(aur);
		System.out.println(count);
	}

	@Test
	void saveUser() {
		Users user = new Users();
		user.setUsername("admin");
		user.setZactive(Boolean.TRUE);
		user.setZid("900010");
		user.setPswd("1234");
		user.setSystemAdmin(true);
		long count = userService.save(user);
		System.out.println(count);
	}

	@Test
	void findByUsername() {
		List<Users> list = userService.findByUsernameOnly("admin");
		list.stream().forEach(l -> {
			System.out.println(l.toString());
			System.out.println(l.getSystemAdmin());
		});
	}

	@Test
	void update() {
		List<Users> list = userService.findByUsernameOnly("admin");
		Users user = list.get(0);
		user.setPswd("admin");
		long count = userService.update(user);
		System.out.println(count);
	}

}
