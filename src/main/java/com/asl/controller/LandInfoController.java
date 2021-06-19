package com.asl.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import com.asl.entity.LandDagDetails;
import com.asl.entity.LandDocument;
import com.asl.entity.LandSurvey;
import com.asl.entity.LandInfo;
import com.asl.entity.LandOwner;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandDocumentService;
import com.asl.service.LandInfoService;
import com.asl.service.LandSurveyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/landinfo")
public class LandInfoController extends ASLAbstractController {
	
	@Autowired private LandInfoService landInfoService;
	@Autowired private LandDocumentService landDocumentService;
	@Autowired private LandSurveyService landSurveyService;
	
	@GetMapping
	public String loadLandInfoPage(Model model) {
		
		model.addAttribute("info", getDefaultLandinfo());
		model.addAttribute("allInfos", landInfoService.getAllLandInfo());
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LAND_ID.getCode(), Boolean.TRUE));
		model.addAttribute("statusTypes", xcodesService.findByXtype(CodeType.STATUS_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/landinfo";
	}
	
	private LandInfo getDefaultLandinfo() {
		LandInfo lf  = new LandInfo();
		
		lf.setXtypetrn(TransactionCodeType.LAND_ID.getCode());
		lf.setXtrn(TransactionCodeType.LAND_ID.getdefaultCode());
		lf.setXlandqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlanddedother(0);
		lf.setXroadred(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXotherred(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlandnetqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		lf.setXlanddedroad(0);
		lf.setXriversideqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		
		
		return lf;
	}
	
	@GetMapping("/{xland}")
	public String loadInfoPage(@PathVariable String xland, Model model) {
		LandInfo landInfo = landInfoService.findByLandInfo(xland);
		if (landInfo == null) return "redirect:/landinfo";

		
		model.addAttribute("info", landInfo);
		model.addAttribute("allInfos", landInfoService.getAllLandInfo());
		model.addAttribute("lldlist", landDocumentService.findByAllLandDocument(xland));
		model.addAttribute("lpelist", landInfoService.findByLandOwner(xland));
		model.addAttribute("lddlist", landInfoService.findByLandDagDetails(xland));
		model.addAttribute("llslist", landSurveyService.findByXlandSurvey(xland));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.LAND_ID.getCode(), Boolean.TRUE));
		model.addAttribute("statusTypes", xcodesService.findByXtype(CodeType.STATUS_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		return "pages/land/landinfo";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandInfo landInfo, BindingResult bindingResult) {
		if (landInfo == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(landInfo.getXland())) {
			LandInfo xlp = landInfoService.findByLandInfo(landInfo.getXland());
			BeanUtils.copyProperties(landInfo, xlp,"xtypetrn","xtrn");
			long count = landInfoService.update(xlp);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update land info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Land info updated successfully");
			responseHelper.setRedirectUrl("/landinfo/" + xlp.getXland());
			return responseHelper.getResponse();
		}

		// if new
		long count = landInfoService.save(landInfo);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save land info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Land info saved successfully");
		responseHelper.setRedirectUrl("/landinfo/" + landInfo.getXland());
		return responseHelper.getResponse();
	}
	
	@PostMapping("/archive/{xland}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xland){
		return doArchiveOrRestore(xland, true);
	}

	@PostMapping("/restore/{xland}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xland){
		return doArchiveOrRestore(xland, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xland, boolean archive){
		LandInfo lp = landInfoService.findByLandInfo(xland);
		if(lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landInfoService.update(lp);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Land Information updated successfully");
		responseHelper.setRedirectUrl("/landinfo/" + lp.getXland());
		return responseHelper.getResponse();
	}
	
	//start of landowner
	
	@GetMapping("/{xland}/owner/{xrow}/show")
	public String loadOwnerModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandOwner lpe = new LandOwner();
			lpe.setXnote("");
			lpe.setXperson("");
			lpe.setXqty(0);
			lpe.setXland(xland);
			lpe.setXtype("");
			lpe.setXunit("");
			model.addAttribute("lpe", lpe);
			model.addAttribute("ownerTypes", xcodesService.findByXtype(CodeType.OWNER_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		else {
			LandOwner lpe = landInfoService.findLandOwnerByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(lpe==null) {
				lpe = new LandOwner();
				
			}
			model.addAttribute("lpe", lpe);
			model.addAttribute("ownerTypes", xcodesService.findByXtype(CodeType.OWNER_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		
		return "pages/land/ownermodal::ownermodal";
	}
	
	@PostMapping("/owner/save")
	public @ResponseBody Map<String, Object> saveLandOwner(LandOwner landOwner){
		if(landOwner == null || StringUtils.isBlank(landOwner.getXland())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		LandOwner exist = landInfoService.findByXlandAndXperson(landOwner.getXland(), landOwner.getXperson());

		// if new data
		if(landOwner.getXrow() == 0 && exist != null) {
			responseHelper.setErrorStatusAndMessage("Land " + landOwner.getXland() + " with person " + landOwner.getXperson() + " data already exist in this system");
			return responseHelper.getResponse();
		}
		
		// if existing
		if(landOwner.getXrow() != 0 && exist != null) {
			BeanUtils.copyProperties(landOwner, exist);
			long count = landInfoService.update(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("ownertable","/landinfo/owner/" + landOwner.getXland());
			responseHelper.setSuccessStatusAndMessage("Owner Detaails updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = landInfoService.save(landOwner);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("ownertable", "/landinfo/owner/" + landOwner.getXland());
		responseHelper.setSuccessStatusAndMessage("Owner Details saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/owner/{xland}")
	public String reloadOwnerTable(@PathVariable String xland, Model model) {
		List<LandOwner> ownerList = landInfoService.findByLandOwner(xland);
		model.addAttribute("lpelist", ownerList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::ownertable";
	}

	//delete
	@PostMapping("{xland}/owner/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOwnerDetails(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandOwner lpe = landInfoService.findLandOwnerByXlandAndXrow(xland, Integer.parseInt(xrow));
		if(lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landInfoService.deleteLandOwner(lpe);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("ownertable", "/landinfo/owner/" + xland);
		return responseHelper.getResponse();
	}
	////end of landowner
	
	
	//start of dag details
	
	@GetMapping("/{xland}/landdag/{xrow}/show")
	public String loadLandDagModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandDagDetails ldd = new LandDagDetails();
			ldd.setXdagnum(0);
			ldd.setXdagtype("");
			ldd.setXland(xland);
			ldd.setXqty(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			ldd.setXtype("");
			ldd.setXunit("");
			model.addAttribute("ldd", ldd);
			model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landTypes", xcodesService.findByXtype(CodeType.LAND_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		else {
			LandDagDetails ldd = landInfoService.findlandDagDetailsByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(ldd==null) {
				ldd = new LandDagDetails();
				
			}
			model.addAttribute("ldd", ldd);
			model.addAttribute("dagTypes", xcodesService.findByXtype(CodeType.DAG_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landTypes", xcodesService.findByXtype(CodeType.LAND_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("landUnitTypes", xcodesService.findByXtype(CodeType.LAND_UNIT.getCode(), Boolean.TRUE));
		}
		
		return "pages/land/landdagmodal::landdagmodal";
	}
	
	@PostMapping("/landdag/save")
	public @ResponseBody Map<String, Object> saveLandDagDetails(LandDagDetails landDagDetails) {
		if (landDagDetails == null || StringUtils.isBlank(landDagDetails.getXland())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		LandDagDetails exist = landInfoService.findlandDagDetailsByXlandAndXrow(landDagDetails.getXland(), landDagDetails.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(landDagDetails, exist,"xland");
			long count = landInfoService.update(exist);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("landdagtable","/landinfo/landdag/" + landDagDetails.getXland());
			responseHelper.setSuccessStatusAndMessage("Land Dag Details updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = landInfoService.save(landDagDetails);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("landdagtable","/landinfo/landdag/" + landDagDetails.getXland());
		responseHelper.setSuccessStatusAndMessage("Land Dag Details saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/landdag/{xland}")
	public String reloadLandDagTable(@PathVariable String xland, Model model) {
		List<LandDagDetails> dagList = landInfoService.findByLandDagDetails(xland);
		model.addAttribute("lddlist", dagList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::landdagtable";
	}
	
	//delete
		@PostMapping("{xland}/landdag/{xrow}/delete")
		public @ResponseBody Map<String, Object> deleteLandDagDetails(@PathVariable String xland, @PathVariable String xrow, Model model) {
			LandDagDetails ldd = landInfoService.findlandDagDetailsByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(ldd == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			long count = landInfoService.deleteLandDagDetails(ldd);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Deleted successfully");
			responseHelper.setReloadSectionIdWithUrl("landdagtable", "/landinfo/landdag/" + xland);
			return responseHelper.getResponse();
		}
	
	
	// For Land Land Document

	@GetMapping("/{xland}/land/{xrow}/show")
	public String loadLandDocModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if ("new".equalsIgnoreCase(xrow)) {
			LandDocument lld = new LandDocument();
			lld.setXname("");
			lld.setXperson("");
			lld.setXland(xland);
			lld.setXsurveyor("");
			model.addAttribute("lld", lld);
			lld.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
			lld.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		} else {
			LandDocument lld = landDocumentService.findLandLandDocumentByLandAndXrow(xland,Integer.parseInt(xrow));
			if (lld == null) {
				lld = new LandDocument();
				lld.setXperson("");
				lld.setXsurveyor("");
				lld.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
				lld.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				
			}
			model.addAttribute("lld", lld);
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		}

		return "pages/land/landdocumentmodal::landdocumentmodal";
	}

	@PostMapping("/landdoc/save")
	public @ResponseBody Map<String, Object> saveLandDoc(LandDocument landDocument, @RequestParam("files[]") MultipartFile[] files) {
		if (landDocument == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		 
		if (files != null && files.length > 0) {

			// Rename the file
			String extension = null;
			int j = files[0].getOriginalFilename().lastIndexOf('.');
			if (j > 0) {
				extension = files[0].getOriginalFilename().substring(j + 1);
			}
			
			//Split Text
			 String[] a = files[0].getOriginalFilename().split("\\.");
			 String part1 = a[0];
			 System.out.println("The File Name Is: "+part1);
			 //End split
			
			String fileName = UUID.randomUUID() + "_" + part1 + "." + extension;
			log.debug("File name is now: {}", fileName);

			try {
				// create a directory if not exist
				String uploadPath = "D://Bosila//BosilaDocuments";
				File dir = new File(uploadPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// Upload file into server
				Files.copy(files[0].getInputStream(), Paths.get(uploadPath, fileName));
				landDocument.setXdocument(uploadPath + "/" + fileName);
				landDocument.setXnameold(files[0].getOriginalFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// if existing
		LandDocument existLand = landDocumentService.findLandLandDocumentByLandAndXrow(landDocument.getXland(), landDocument.getXrow());
		if (existLand != null) {
			BeanUtils.copyProperties(landDocument, existLand, "xdoc");
			long count = landDocumentService.update(existLand);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("landdocumenttable", "/landinfo/land/" + existLand.getXland());
			responseHelper.setSuccessStatusAndMessage("Land Document updated successfully");
			return responseHelper.getResponse();
		}
		
		
		String xtrn =  xtrnService.generateAndGetXtrnNumber(landDocument.getXtypetrn(), landDocument.getXtrn(), 6);
		landDocument.setXdoc(xtrn);
		System.out.println("The Value Of Xtrn Is Now: "+xtrn);

		// if new detail
		long count = landDocumentService.save(landDocument);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("landdocumenttable","/landinfo/land/" + landDocument.getXland());
		responseHelper.setSuccessStatusAndMessage("land Document saved successfully");
		return responseHelper.getResponse();

	}

	@GetMapping("/land/{xland}")
	public String reloadLandDocTable(@PathVariable String xland, Model model) {
		List<LandDocument> LandDocList = landDocumentService.findByAllLandDocument(xland);
		model.addAttribute("lldlist", LandDocList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::landdocumenttable";
	}
	
	@PostMapping("{xland}/land/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteLandDocDetail(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandDocument lpe = landDocumentService.findLandLandDocumentByLandAndXrow(xland, Integer.parseInt(xrow));
		if (lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landDocumentService.deleteDetail(lpe);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Document Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("landdocumenttable", "/landinfo/land/" + xland);
		return responseHelper.getResponse();
	}


	@GetMapping("/{xland}/survey/{xrow}/show")
	public String loadLandSurveyModal(@PathVariable String xland, @PathVariable String xrow, Model model) {
		if("new".equalsIgnoreCase(xrow)) {
			LandSurvey lls = new LandSurvey();
			lls.setXland(xland);
			lls.setXsurveyor("");
			lls.setXnote("");
			model.addAttribute("lls", lls);
		}
		else {
			LandSurvey lls = landSurveyService.findLandSurveydetailByXlandAndXrow(xland, Integer.parseInt(xrow));
			if(lls==null) {
				lls = new LandSurvey();
			}
			model.addAttribute("lls", lls);
		}
		
		return "pages/land/surveymodal::surveymodal";
	}
	
	@PostMapping("/survey/save")
	public @ResponseBody Map<String, Object> saveLandSurveyDetails(LandSurvey landSurvey) {
		if (landSurvey == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		if(StringUtils.isEmpty(landSurvey.getXsurveyor())) {
			responseHelper.setErrorStatusAndMessage("Please Select your Surveyor ID");
			return responseHelper.getResponse();
		}

		// if existing
		LandSurvey exist = landSurveyService.findLandSurveydetailByXlandAndXrow(landSurvey.getXland(), landSurvey.getXrow());
		if (exist != null) {
			BeanUtils.copyProperties(landSurvey, exist);
			long count = landSurveyService.update(landSurvey);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("surveytable","/landinfo/survey/" + exist.getXland());
			responseHelper.setSuccessStatusAndMessage("Survey Details updated successfully");
			return responseHelper.getResponse();
		}

		
		// if new detail
		long count = landSurveyService.save(landSurvey);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("surveytable","/landinfo/survey/" + landSurvey.getXland());
		responseHelper.setSuccessStatusAndMessage("Survey Details saved successfully");
		return responseHelper.getResponse();
	}
	
	@GetMapping("/survey/{xland}")
	public String reloadLandSurveyTable(@PathVariable String xland, Model model) {
		List<LandSurvey> LandSurveyList = landSurveyService.findByXlandSurvey(xland);
		model.addAttribute("llslist", LandSurveyList);
		model.addAttribute("info", landInfoService.findByLandInfo(xland));
		return "pages/land/landinfo::surveytable";
	}
	
	@PostMapping("{xland}/survey/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteLandSurveyDetail(@PathVariable String xland, @PathVariable String xrow, Model model) {
		LandSurvey lls = landSurveyService.findLandSurveydetailByXlandAndXrow(xland, Integer.parseInt(xrow));
		if (lls == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landSurveyService.deleteDetail(lls);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Survey Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("surveytable", "/landinfo/survey/" + xland);
		return responseHelper.getResponse();
	}
	
}
	
