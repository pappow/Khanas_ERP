package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/supadjustentry")
public class SupplierAdjustmentEntryController  extends ASLAbstractController{

	@GetMapping
	public String loadSupplierAdjustmentEntryPage() {
		
		return"pages/accounts/supadjustentry";
	}
}
