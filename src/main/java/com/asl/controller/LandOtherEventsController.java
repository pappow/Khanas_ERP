package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.LandComEvent;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandComEventService;

@Controller
@RequestMapping("/landotherevents")
public class LandOtherEventsController extends ASLAbstractController{

@Autowired private LandComEventService landComEventService;
	
	@GetMapping
	public String loadLandOtherEventsPage(Model model) {
		
		model.addAttribute("comevent", getDefaultLandOtherEvent());
		model.addAttribute("allComEvents", landComEventService.getAllLandOtherEvent());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDOTHEREVENT_ID.getCode(), Boolean.TRUE));
		model.addAttribute("otherEventTypes", xcodesService.findByXtype(CodeType.OTHEREVENT_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landotherevents";
	}
	
	private LandComEvent getDefaultLandOtherEvent() {
		LandComEvent lce  = new LandComEvent();
		lce.setXtypetrn(TransactionCodeType.LANDOTHEREVENT_ID.getCode());
		lce.setXtrn(TransactionCodeType.LANDOTHEREVENT_ID.getdefaultCode());
		return lce;
	}
	
	@GetMapping("/{xevent}")
	public String loadComEventPage(@PathVariable String xevent, Model model) {
		LandComEvent landComEvent = landComEventService.findAllLandComEvent(xevent);
		if (landComEvent == null) return "redirect:/landotherevents";
		
		model.addAttribute("comevent", landComEvent);
		model.addAttribute("allComEvents", landComEventService.getAllLandOtherEvent());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDOTHEREVENT_ID.getCode(), Boolean.TRUE));
		model.addAttribute("otherEventTypes", xcodesService.findByXtype(CodeType.OTHEREVENT_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landotherevents";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandComEvent landComEvent, BindingResult bindingResult) {
		if (landComEvent == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
		// if existing
		if(StringUtils.isNotBlank(landComEvent.getXevent())) {
			LandComEvent xlp = landComEventService.findAllLandComEvent(landComEvent.getXevent());
			BeanUtils.copyProperties(landComEvent, xlp,"xtypetrn","xtrn");
			long count = landComEventService.updateLandComEvent(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Other Event info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Other Event info updated successfully");
			responseHelper.setRedirectUrl("/landotherevents/" + xlp.getXevent());
			return responseHelper.getResponse();
		}
		// if new
		long count = landComEventService.saveLandComEvent(landComEvent);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save Other Event info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Other Event info saved successfully");
		responseHelper.setRedirectUrl("/landotherevents/" + landComEvent.getXevent());
			return responseHelper.getResponse();
		}
}
