package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.LandComEvent;
import com.asl.entity.LandDagDetails;
import com.asl.entity.Pdeducation;
import com.asl.entity.Pdexperience;
import com.asl.entity.Pdmst;
import com.asl.entity.Pdpromodt;
import com.asl.entity.Pdtransdt;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandComEventService;
import com.asl.service.PdmstService;

@Controller
@RequestMapping("/hrpersonal")
public class HRPersonalInfoController extends ASLAbstractController{
	@Autowired private PdmstService pdmstService;
	@GetMapping
	public String loadHRPersonalInfoPage(Model model) {
		model.addAttribute("hrinfo", getDefaultPersonalInfo());
		model.addAttribute("allHrinfos", pdmstService.getAllHRPdmst());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.HR_EMPLOYEE_ID.getCode(), Boolean.TRUE));
		model.addAttribute("sexTypes", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		model.addAttribute("maritalStatus", xcodesService.findByXtype(CodeType.MARITAL_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("departmentNames", xcodesService.findByXtype(CodeType.DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("designationNames", xcodesService.findByXtype(CodeType.DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("grades", xcodesService.findByXtype(CodeType.GRADE.getCode(), Boolean.TRUE));
		model.addAttribute("employeeStatus", xcodesService.findByXtype(CodeType.EMPLOYESS_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("districtNames", xcodesService.findByXtype(CodeType.DISTRICT.getCode(), Boolean.TRUE));
		model.addAttribute("employeeCategories", xcodesService.findByXtype(CodeType.EMPLOYESS_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("bloodGropus", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("leavePlans", xcodesService.findByXtype(CodeType.LEAVE_PLAN.getCode(), Boolean.TRUE));
		model.addAttribute("employeeTypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("weekDays", xcodesService.findByXtype(CodeType.WEEK_DAY.getCode(), Boolean.TRUE));
		model.addAttribute("holidays", xcodesService.findByXtype(CodeType.HOLIDAY_APPLICABLE.getCode(), Boolean.TRUE));
		return "pages/hr/hrpersonal";
	}
	
	private Pdmst getDefaultPersonalInfo() {
		Pdmst pd  = new Pdmst();
		pd.setXtypetrn(TransactionCodeType.HR_EMPLOYEE_ID.getCode());
		pd.setXtrn(TransactionCodeType.HR_EMPLOYEE_ID.getdefaultCode());
		pd.setXstatus("Open");
		return pd;
	}
	
	@GetMapping("/{xstaff}")
	public String loadHRPersonalInfoPage(@PathVariable String xstaff, Model model) {
		Pdmst pdmst = pdmstService.findAllPdmst(xstaff);
		if (pdmst == null) return "redirect:/hrpersonal";
		
		model.addAttribute("hrinfo", pdmst);
		model.addAttribute("allHrinfos", pdmstService.getAllHRPdmst());
		model.addAttribute("hrqlist", pdmstService.findByPdeducation(xstaff));
		model.addAttribute("hrelist", pdmstService.findByPdexperience(xstaff));
		model.addAttribute("hrplist", pdmstService.findByPdpromodt(xstaff));
		model.addAttribute("hrtlist", pdmstService.findByPdtransdt(xstaff));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.HR_EMPLOYEE_ID.getCode(), Boolean.TRUE));
		model.addAttribute("sexTypes", xcodesService.findByXtype(CodeType.SEX.getCode(), Boolean.TRUE));
		model.addAttribute("maritalStatus", xcodesService.findByXtype(CodeType.MARITAL_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("departmentNames", xcodesService.findByXtype(CodeType.DEPARTMENT.getCode(), Boolean.TRUE));
		model.addAttribute("designationNames", xcodesService.findByXtype(CodeType.DESIGNATION.getCode(), Boolean.TRUE));
		model.addAttribute("grades", xcodesService.findByXtype(CodeType.GRADE.getCode(), Boolean.TRUE));
		model.addAttribute("employeeStatus", xcodesService.findByXtype(CodeType.EMPLOYESS_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("districtNames", xcodesService.findByXtype(CodeType.DISTRICT.getCode(), Boolean.TRUE));
		model.addAttribute("employeeCategories", xcodesService.findByXtype(CodeType.EMPLOYESS_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("bloodGropus", xcodesService.findByXtype(CodeType.BLOOD_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("leavePlans", xcodesService.findByXtype(CodeType.LEAVE_PLAN.getCode(), Boolean.TRUE));
		model.addAttribute("employeeTypes", xcodesService.findByXtype(CodeType.EMPLOYEE_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("weekDays", xcodesService.findByXtype(CodeType.WEEK_DAY.getCode(), Boolean.TRUE));
		model.addAttribute("holidays", xcodesService.findByXtype(CodeType.HOLIDAY_APPLICABLE.getCode(), Boolean.TRUE));
		return "pages/hr/hrpersonal";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pdmst pdmst, BindingResult bindingResult) {
		if (pdmst == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		
	// if existing
	if(StringUtils.isNotBlank(pdmst.getXstaff())) {
		Pdmst pi = pdmstService.findAllPdmst(pdmst.getXstaff());
		BeanUtils.copyProperties(pdmst , pi,"xtypetrn","xtrn");
		long count = pdmstService.update(pi);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Personal info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Personal info updated successfully"); 
		responseHelper.setRedirectUrl("/hrpersonal/" + pi.getXstaff());
		return responseHelper.getResponse();
	}
	// if new
	long count = pdmstService.save(pdmst );
	if(count == 0) {
		responseHelper.setErrorStatusAndMessage("Can't save Personal info");
		return responseHelper.getResponse();
	}
	responseHelper.setSuccessStatusAndMessage("Personal info saved successfully");
	responseHelper.setRedirectUrl("/hrpersonal/" + pdmst .getXstaff());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/delete/{xstaff}")
	public @ResponseBody Map<String, Object> deleteHRPersonalInfo(@PathVariable String xstaff,  Model model) {
		Pdmst lp = pdmstService.findAllPdmst(xstaff);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pdmstService.delete(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/hrpersonal/" + xstaff );
		return responseHelper.getResponse();
}

	//start of HRQualification
	
	@GetMapping("/{xstaff}/hrqualification/{xrow}/show")
	public String loadHRQualificationModal(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			Pdeducation hrq = new Pdeducation();
			hrq.setXstaff(xstaff);
			model.addAttribute("hrq", hrq);
		}
		else {
			Pdeducation hrq = pdmstService.findPdeducationByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(hrq==null) {
				hrq = new Pdeducation();
				
			}
			model.addAttribute("hrq", hrq);
		}
		
		return "pages/hr/hrqualificationmodal::hrqualificationmodal";
	}
	
	@PostMapping("/hrqualification/save")
	public @ResponseBody Map<String, Object> saveHRQualification(Pdeducation pdqua) {
		if (pdqua == null || StringUtils.isBlank(pdqua.getXstaff())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Pdeducation exist = pdmstService.findPdeducationByXstaffAndXrow(pdqua.getXstaff(), pdqua.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(pdqua, exist,"xstaff");
			long count = pdmstService.updatePdeducation(exist);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("hrqualificationtable","/hrpersonal/hrqualification/" + pdqua.getXstaff());
			responseHelper.setSuccessStatusAndMessage("Personal qualification updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = pdmstService.savePdeducation(pdqua);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("hrqualificationtable","/hrpersonal/hrqualification/" + pdqua.getXstaff());
		responseHelper.setSuccessStatusAndMessage("Personal qualification saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/hrqualification/{xstaff}")
	public String reloadHRQualificationTable(@PathVariable String xstaff, Model model) {
		List<Pdeducation> hrqList = pdmstService.findByPdeducation(xstaff);
		model.addAttribute("hrqlist", hrqList);
		model.addAttribute("hrinfo", pdmstService.findAllPdmst(xstaff));
		return "pages/hr/hrpersonal::hrqualificationtable";
	}
	
	//delete
		@PostMapping("{xstaff}/hrqualification/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteQualification(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			Pdeducation hrq = pdmstService.findPdeducationByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(hrq == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = pdmstService.deletePdeducation(hrq);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("hrqualificationtable", "/hrpersonal/hrqualification/" + xstaff);
			return responseHelper.getResponse();
		}
	
	//start of HRExperience
	
	@GetMapping("/{xstaff}/hrexperience/{xrow}/show")
	public String loadHRExperienceModal(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			Pdexperience hre = new Pdexperience();
			hre.setXstaff(xstaff);
			model.addAttribute("hre", hre);
		}
		else {
			Pdexperience hre = pdmstService.findPdexperienceByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(hre==null) {
				hre = new Pdexperience();
				
			}
			model.addAttribute("hre", hre);
		}
		
		return "pages/hr/hrexperiencemodal::hrexperiencemodal";
	}
	
	@PostMapping("/hrexperience/save")
	public @ResponseBody Map<String, Object> saveHRExperience(Pdexperience pdex) {
		if (pdex == null || StringUtils.isBlank(pdex.getXstaff())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Pdexperience exist = pdmstService.findPdexperienceByXstaffAndXrow(pdex.getXstaff(), pdex.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(pdex, exist,"xstaff");
			long count = pdmstService.updatePdexperience(exist);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("hrexperiencetable","/hrpersonal/hrexperience/" + pdex.getXstaff());
			responseHelper.setSuccessStatusAndMessage("Personal experiences updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = pdmstService.savePdexperience(pdex);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("hrexperiencetable","/hrpersonal/hrexperience/" + pdex.getXstaff());
		responseHelper.setSuccessStatusAndMessage("Personal experiences saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/hrexperience/{xstaff}")
	public String reloadHRExperienceTable(@PathVariable String xstaff, Model model) {
		List<Pdexperience> hreList = pdmstService.findByPdexperience(xstaff);
		model.addAttribute("hrelist", hreList);
		model.addAttribute("hrinfo", pdmstService.findAllPdmst(xstaff));
		return "pages/hr/hrpersonal::hrexperiencetable";
	}
	
	//delete
		@PostMapping("{xstaff}/hrexperience/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteHRExperience(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			Pdexperience hre = pdmstService.findPdexperienceByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(hre == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = pdmstService.deletePdexperience(hre);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("hrexperiencetable", "/hrpersonal/hrexperience/" + xstaff);
			return responseHelper.getResponse();
		}
		
		//start of HRPromotion
		
		@GetMapping("/{xstaff}/hrpromotion/{xrow}/show")
		public String loadHRPromotionModal(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				Pdpromodt hrp = new Pdpromodt();
				hrp.setXstaff(xstaff);
				model.addAttribute("hrp", hrp);
				model.addAttribute("status", xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode(), Boolean.TRUE));
			}
			else {
				Pdpromodt hrp = pdmstService.findPdpromodtByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
				if(hrp==null) {
					hrp = new Pdpromodt();
					
				}
				model.addAttribute("hrp", hrp);
				model.addAttribute("status", xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode(), Boolean.TRUE));
			}
			
			return "pages/hr/hrpromotionmodal::hrpromotionmodal";
		}
		
		@PostMapping("/hrpromotion/save")
		public @ResponseBody Map<String, Object> saveHRPromotion(Pdpromodt pdpr) {
			if (pdpr == null || StringUtils.isBlank(pdpr.getXstaff())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			// if existing
			Pdpromodt exist = pdmstService.findPdpromodtByXstaffAndXrow(pdpr.getXstaff(), pdpr.getXrow());
			if (exist != null) {
				BeanUtils.copyProperties(pdpr, exist,"xstaff");
				long count = pdmstService.updatePdpromodt(exist);
				if (count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("hrpromotiontable","/hrpersonal/hrpromotion/" + pdpr.getXstaff());
				responseHelper.setSuccessStatusAndMessage("Personal promotion updated successfully");
				return responseHelper.getResponse();
			}

			
			// if new detail
			long count = pdmstService.savePdpromodt(pdpr);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("hrpromotiontable","/hrpersonal/hrpromotion/" + pdpr.getXstaff());
			responseHelper.setSuccessStatusAndMessage("Personal promotion saved successfully");
			return responseHelper.getResponse();
		}

		@GetMapping("/hrpromotion/{xstaff}")
		public String reloadHRPromotionTable(@PathVariable String xstaff, Model model) {
			List<Pdpromodt> hrpList = pdmstService.findByPdpromodt(xstaff);
			model.addAttribute("hrplist", hrpList);
			model.addAttribute("hrinfo", pdmstService.findAllPdmst(xstaff));
			return "pages/hr/hrpersonal::hrpromotiontable";
		}
		
		//delete
		@PostMapping("{xstaff}/hrpromotion/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteHRPromotion(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			Pdpromodt hrp = pdmstService.findPdpromodtByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(hrp == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = pdmstService.deletePdpromodt(hrp);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("hrpromotiontable", "/hrpersonal/hrpromotion/" + xstaff);
			return responseHelper.getResponse();
		}
		
		//start of HRTransfer
		
		@GetMapping("/{xstaff}/hrtransfer/{xrow}/show")
		public String loadHRTransferModal(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			if("new".equalsIgnoreCase(xrow)) {
				Pdtransdt hrt = new Pdtransdt();
				hrt.setXstaff(xstaff);
				model.addAttribute("hrt", hrt);
				model.addAttribute("status", xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode(), Boolean.TRUE));
			}
			else {
				Pdtransdt hrt = pdmstService.findPdtransdtByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
				if(hrt==null) {
					hrt = new Pdtransdt();
					
				}
				model.addAttribute("hrt", hrt);
				model.addAttribute("status", xcodesService.findByXtype(CodeType.CUSTOMER_STATUS.getCode(), Boolean.TRUE));
			}
			
			return "pages/hr/hrtransfermodal::hrtransfermodal";
		}
		
		@PostMapping("/hrtransfer/save")
		public @ResponseBody Map<String, Object> saveHRTransfer(Pdtransdt pdtr) {
			if (pdtr == null || StringUtils.isBlank(pdtr.getXstaff())) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			// if existing
			Pdtransdt exist = pdmstService.findPdtransdtByXstaffAndXrow(pdtr.getXstaff(), pdtr.getXrow());
			if (exist != null) {
				BeanUtils.copyProperties(pdtr, exist,"xstaff");
				long count = pdmstService.updatePdtransdt(exist);
				if (count == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
				responseHelper.setReloadSectionIdWithUrl("hrtransfertable","/hrpersonal/hrtransfer/" + pdtr.getXstaff());
				responseHelper.setSuccessStatusAndMessage("Transfer History updated successfully");
				return responseHelper.getResponse();
			}

			
			// if new detail
			long count = pdmstService.savePdtransdt(pdtr);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("hrtransfertable","/hrpersonal/hrtransfer/" + pdtr.getXstaff());
			responseHelper.setSuccessStatusAndMessage("Transfer History saved successfully");
			return responseHelper.getResponse();
		}

		@GetMapping("/hrtransfer/{xstaff}")
		public String reloadHRTransferTable(@PathVariable String xstaff, Model model) {
			List<Pdtransdt> hrtList = pdmstService.findByPdtransdt(xstaff);
			model.addAttribute("hrtlist", hrtList);
			model.addAttribute("hrinfo", pdmstService.findAllPdmst(xstaff));
			return "pages/hr/hrpersonal::hrtransfertable";
		}
		
		//delete
		@PostMapping("{xstaff}/hrtransfer/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteHRTransfer(@PathVariable String xstaff, @PathVariable String xrow, Model model) {
			Pdtransdt hrt = pdmstService.findPdtransdtByXstaffAndXrow(xstaff, Integer.parseInt(xrow));
			if(hrt == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = pdmstService.deletePdtransdt(hrt);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("hrtransfertable", "/hrpersonal/hrtransfer/" + xstaff);
			return responseHelper.getResponse();
		}
}
