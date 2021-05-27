package com.asl.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.entity.Caitem;
import com.asl.entity.ConventionBookedDetails;
import com.asl.service.CaitemService;
import com.asl.service.HallBookingService;
import com.asl.service.OpordService;

@Controller
@RequestMapping("/conventionmanagement")
public class ConventionManagementController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private CaitemService caitemService;
	@Autowired private HallBookingService hallBookingService;

	@GetMapping
	public String loadConventionManagementMenuPage(Model model) {

		model.addAttribute("availableHalls", opordService.findAvailableHallsByDate(new Date()));
		model.addAttribute("bookedHalls", opordService.findBookedHallsByXfuncdate(new Date()));


		Map<String, List<Caitem>> map = new HashMap<>();
		model.addAttribute("itemMap", map);


		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.HOUR_OF_DAY, 23);
		cal2.set(Calendar.MINUTE, 59);
		cal2.set(Calendar.SECOND, 59);

		String xstartdate = sdf.format(cal.getTime()).toUpperCase();
		String xenddate = sdf.format(cal2.getTime()).toUpperCase();
		List<ConventionBookedDetails> bookedHalls = hallBookingService.allBookedHallsInDateRange2("Convention Hall", xstartdate, xenddate);
		bookedHalls.stream().forEach(b -> b.setBooked(true));

		model.addAttribute("bookedHalls", bookedHalls);

		List<Caitem> availHalls = new ArrayList<>();
		List<Caitem> items = caitemService.findByXcatitem("Convention Hall");
		for(Caitem c : items) {
			for(ConventionBookedDetails booked : bookedHalls) {
				if(!c.getXitem().equals(booked.getXitem())) {
					availHalls.add(c);
				}
			}
		}

		model.addAttribute("availableHalls", availHalls);

		return "pages/conventionmanagement/conventionmanagement";
	}

}
