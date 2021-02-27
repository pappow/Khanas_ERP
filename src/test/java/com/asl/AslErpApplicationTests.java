package com.asl;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.entity.UserAuditRecord;
import com.asl.entity.Xusers;
import com.asl.service.UserAuditRecordService;
import com.asl.service.XusersService;

@SpringBootTest
class AslErpApplicationTests {

	@Autowired private XusersService userService;
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
		Xusers user = new Xusers();
		user.setZemail("admin");
		user.setZactive(Boolean.TRUE);
		user.setZid("900010");
		user.setXpassword("1234");
		user.setSystemadmin(true);
		long count = userService.save(user);
		System.out.println(count);
	}

	@Test
	void findByUsername() {
		List<Xusers> list = userService.findByZemail("admin");
		list.stream().forEach(l -> {
			System.out.println(l.toString());
			System.out.println(l.getSystemadmin());
		});
	}

	@Test
	void update() {
		List<Xusers> list = userService.findByZemail("admin");
		Xusers user = list.get(0);
		user.setXpassword("admin");
		long count = userService.update(user);
		System.out.println(count);
	}

}
