package com.asl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.service.ImtrnService;

@Controller
@RequestMapping("/inventory")
public class InventoryController extends ASLAbstractController {
	
	@Autowired
	private ImtrnService imtrnService;
	
}
