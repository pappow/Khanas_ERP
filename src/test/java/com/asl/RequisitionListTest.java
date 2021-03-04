package com.asl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.asl.service.RequisitionListService;

@SpringBootTest
public class RequisitionListTest {

	@Autowired private RequisitionListService requisitionListService;

	@Test
	void testZbusinessSave() {
//		requisitionListService.getAllBranchesRequisitions().stream().forEach(i -> {
//			System.out.println(i.toString());
//		});
	}

	
}
