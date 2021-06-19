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
import com.asl.entity.LandSurveyor;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandComEventService;
import com.asl.service.LandInfoService;

@Controller
@RequestMapping("/landevents")
public class LandComEventController extends ASLAbstractController{
	
	@Autowired private LandComEventService landComEventService;
	
	@GetMapping
	public String loadLandEventsPage(Model model) {
		
		model.addAttribute("comevent", getDefaultLandComEvent());
		model.addAttribute("allComEvents", landComEventService.getAllLandComEvent());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDCOMEVENT_ID.getCode(), Boolean.TRUE));
		model.addAttribute("comTypes", xcodesService.findByXtype(CodeType.EVENTCOMMITEE_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landcomevent";
	}
	
	private LandComEvent getDefaultLandComEvent() {
		LandComEvent lce  = new LandComEvent();
		lce.setXtypetrn(TransactionCodeType.LANDCOMEVENT_ID.getCode());
		lce.setXtrn(TransactionCodeType.LANDCOMEVENT_ID.getdefaultCode());
		return lce;
	}
	
	@GetMapping("/{xevent}")
	public String loadComEventPage(@PathVariable String xevent, Model model) {
		LandComEvent landComEvent = landComEventService.findAllLandComEvent(xevent);
		if (landComEvent == null) return "redirect:/landevents";
		
		model.addAttribute("comevent", landComEvent);
		model.addAttribute("allComEvents", landComEventService.getAllLandComEvent());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDCOMEVENT_ID.getCode(), Boolean.TRUE));
		model.addAttribute("comTypes", xcodesService.findByXtype(CodeType.EVENTCOMMITEE_TYPE.getCode(), Boolean.TRUE));
		return "pages/land/landcomevent";
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
				responseHelper.setErrorStatusAndMessage("Can't update Event Committee info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Event Committee info updated successfully");
			responseHelper.setRedirectUrl("/landevents/" + xlp.getXevent());
			return responseHelper.getResponse();
		}
		// if new
		long count = landComEventService.saveLandComEvent(landComEvent);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save Event Committee info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Event Committee info saved successfully");
		responseHelper.setRedirectUrl("/landevents/" + landComEvent.getXevent());
			return responseHelper.getResponse();
		}

}
