package com.asl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/landsurveyor")
public class LandSurveyorController extends ASLAbstractController{

	@GetMapping
	public String loadLandSurveyorPage() {
		return "pages/landsurveyor";
	}
}
