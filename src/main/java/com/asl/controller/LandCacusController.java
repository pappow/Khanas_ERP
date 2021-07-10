package com.asl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.LandComEvent;
import com.asl.entity.LandMemberInfo;
import com.asl.entity.Xcodes;
import com.asl.entity.Xtrn;
import com.asl.enums.CacusType;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CacusService;
import com.asl.service.LandMemberInfoService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;
import com.asl.service.ZbusinessService;

@Controller
@RequestMapping("/cacus")
public class LandCacusController extends ASLAbstractController{
	
	@Autowired private LandMemberInfoService cacusService;
	

	@GetMapping
	public String loadCacusPage(Model model) {
		model.addAttribute("cacus", getDefaultLandmemberinfo());
		model.addAttribute("allMembers", cacusService.getAllLandMembers());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDMEMBER_ID.getCode(), Boolean.TRUE));
		model.addAttribute("memberStatus",xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("memberGroup",xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode(), Boolean.TRUE));
		return "pages/land/landmemberinfo";
	}
	
	private Cacus getDefaultLandmemberinfo() {
		Cacus lm  = new Cacus();
		lm.setXtypetrn(TransactionCodeType.LANDMEMBER_ID.getCode());
		lm.setXtrn(TransactionCodeType.LANDMEMBER_ID.getdefaultCode());
		return lm;
	}

	@GetMapping("/{xcus}")
	public String loadCacusPage(@PathVariable String xcus, Model model) {
		
		Cacus cacus = cacusService.findByXcus(xcus);
		if(cacus == null) return "redirect:/cacus";
		
		model.addAttribute("cacus", cacus);
		model.addAttribute("allMembers", cacusService.getAllLandMembers());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LANDMEMBER_ID.getCode(), Boolean.TRUE));
		model.addAttribute("memberStatus",xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("memberGroup",xcodesService.findByXtype(CodeType.CUSTOMER_GROUP.getCode(), Boolean.TRUE));
		
		
		return "pages/land/landmemberinfo";
	}



	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Cacus cacus,BindingResult bindingResult){
		if(cacus == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		
		if(StringUtils.isNotBlank(cacus.getXcus())) {
			Cacus existingCacus = cacusService.findByXcus(cacus.getXcus());
			BeanUtils.copyProperties(cacus, existingCacus, "xtypetrn", "xtrn", "xcus");
			long count = cacusService.update(existingCacus);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update member info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Data updated successfully");
			responseHelper.setRedirectUrl("/cacus/" + existingCacus.getXcus());
			return responseHelper.getResponse();
		}

		long cus = cacusService.save(cacus);
		if(cus ==0) {
			responseHelper.setErrorStatusAndMessage("Can't save info");
			return responseHelper.getResponse();
		}
		
		
		// If new
		long count = cacusService.save(cacus);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Data saved successfully");
		responseHelper.setRedirectUrl("/cacus/" + cacus.getXcus());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xcus}")
	public @ResponseBody Map<String, Object> deleteOtherEvent(@PathVariable String xcus,  Model model) {
		Cacus lp = cacusService.findByXcus(xcus);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = cacusService.delete(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/cacus/" + xcus );
		return responseHelper.getResponse();
}

	

}
