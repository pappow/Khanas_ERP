package com.asl.controller;

import java.util.Map;

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

import com.asl.entity.LandCommitteeMembers;
import com.asl.entity.LandEducation;
import com.asl.entity.LandOwner;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.service.LandCommitteeMembersService;
import com.asl.service.LandOwnerService;

@Controller
@RequestMapping("/landcommitteemembers")
public class LandCommitteeMembersController extends ASLAbstractController {
	
	
	@Autowired
	private LandCommitteeMembersService landCommitteeMembersService;
	@GetMapping
	public String loadLandCommitteeMembersPage(Model model) {
		
		LandCommitteeMembers landCommitteeMembers = new LandCommitteeMembers();
		landCommitteeMembers.setNewData(true);
		model.addAttribute("committeeMember", landCommitteeMembers);
		model.addAttribute("allCommitteeMembers", landCommitteeMembersService.getAllLandCommitteeMembers());
		model.addAttribute("contyributionTypes", xcodesService.findByXtype(CodeType.COMMITEECONTRIBUTION_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("memberTypes", xcodesService.findByXtype(CodeType.COMMITEEMEMBER_TYPE.getCode(), Boolean.TRUE));
		
		return "pages/land/landcommitteemembers";
	}
	
	@GetMapping("/{xcommittee}/{xperson}")
	public String loadCommitteeMembersPage(@PathVariable String xcommittee, @PathVariable String xperson, Model model) {
		LandCommitteeMembers landCommitteeMembers = landCommitteeMembersService.findByXcommitteeAndXperson(xcommittee, xperson);
		if (landCommitteeMembers == null)
			return "redirect:/landcommitteemembers";

		model.addAttribute("committeeMember", landCommitteeMembers);
		model.addAttribute("allCommitteeMembers", landCommitteeMembersService.getAllLandCommitteeMembers());
		model.addAttribute("contyributionTypes", xcodesService.findByXtype(CodeType.COMMITEECONTRIBUTION_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("memberTypes", xcodesService.findByXtype(CodeType.COMMITEEMEMBER_TYPE.getCode(), Boolean.TRUE));
		
		return "pages/land/landcommitteemembers";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandCommitteeMembers landCommitteeMembers, BindingResult bindingResult) {
		if (landCommitteeMembers == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		LandCommitteeMembers exist = landCommitteeMembersService.findByXcommitteeAndXperson(landCommitteeMembers.getXcommittee(), landCommitteeMembers.getXperson());

		// if new data
		if(landCommitteeMembers.isNewData()) {
			if(exist != null) {
				responseHelper.setErrorStatusAndMessage("The person " + landCommitteeMembers.getXperson() + " already exits in " + landCommitteeMembers.getXcommittee() + " committee.");
				return responseHelper.getResponse();
			}

			long count = landCommitteeMembersService.save(landCommitteeMembers);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save data");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Committee Member Data saved successfully");
			responseHelper.setRedirectUrl("/landcommitteemembers/" + landCommitteeMembers.getXcommittee()  + "/" + landCommitteeMembers.getXperson());
			return responseHelper.getResponse();
		}

		// if existing data
		if(exist == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(landCommitteeMembers, exist);
		long count = landCommitteeMembersService.update(exist);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update Committee Membe data");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Committee Member Data updated successfully");
		responseHelper.setRedirectUrl("/landcommitteemembers/" + landCommitteeMembers.getXcommittee()  + "/" + landCommitteeMembers.getXperson());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/archive/{xcommittee}/{xperson}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xcommittee, @PathVariable String xperson){
		return doArchiveOrRestore(xcommittee,xperson, true);
	}

	@PostMapping("/restore/{xcommittee}/{xperson}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xcommittee, @PathVariable String xperson){
		return doArchiveOrRestore(xcommittee,xperson, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xcommittee,String xperson, boolean archive){
		LandCommitteeMembers lp = landCommitteeMembersService.findByXcommitteeAndXperson(xcommittee,xperson);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landCommitteeMembersService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		responseHelper.setSuccessStatusAndMessage("Land Committee Member Information updated successfully");
		responseHelper.setRedirectUrl("/landcommitteemembers/" + lp.getXcommittee() +'/'+ lp.getXperson());
		return responseHelper.getResponse();
	}

	

}
