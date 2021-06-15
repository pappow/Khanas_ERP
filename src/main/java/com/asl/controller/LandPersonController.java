package com.asl.controller;

import java.io.File;
import java.io.IOException;
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

import com.asl.entity.LandPerson;
import com.asl.entity.LandDocument;
import com.asl.entity.LandEducation;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandDocumentService;
import com.asl.service.LandPersonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/landperson")
public class LandPersonController extends ASLAbstractController {

	@Autowired
	private LandPersonService landPersonService;
	@Autowired
	private LandDocumentService landDocumentService;

	@GetMapping
	public String loadLandPersonPage(Model model) {
		model.addAttribute("person", getDefaultLandperson());
		model.addAttribute("allPersons", landPersonService.getAllLandPerson());
		model.addAttribute("prefixes",
				xtrnService.findByXtypetrn(TransactionCodeType.PERSON_ID.getCode(), Boolean.TRUE));
		return "pages/land/landperson";
	}

	private LandPerson getDefaultLandperson() {
		LandPerson lp = new LandPerson();

		lp.setXtypetrn(TransactionCodeType.PERSON_ID.getCode());
		lp.setXtrn(TransactionCodeType.PERSON_ID.getdefaultCode());
		return lp;
	}

	@GetMapping("/{xperson}")
	public String loadRolePage(@PathVariable String xperson, Model model) {
		LandPerson landPerson = landPersonService.findByLandPerson(xperson);
		if (landPerson == null)
			return "redirect:/landperson";

		model.addAttribute("person", landPerson);
		model.addAttribute("allPersons", landPersonService.getAllLandPerson());
		model.addAttribute("lpelist", landPersonService.findByPersonEducation(xperson));
		model.addAttribute("lpdlist", landDocumentService.findByLandPersonDocument(xperson));
		model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("prefixes",
				xtrnService.findByXtypetrn(TransactionCodeType.PERSON_ID.getCode(), Boolean.TRUE));
		return "pages/land/landperson";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandPerson landPerson, BindingResult bindingResult) {
		if (landPerson == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		if (StringUtils.isNotBlank(landPerson.getXperson())) {
			LandPerson xlp = landPersonService.findByLandPerson(landPerson.getXperson());
			BeanUtils.copyProperties(landPerson, xlp, "xtypetrn", "xtrn");
			long count = landPersonService.update(xlp);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update person info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Person info updated successfully");
			responseHelper.setRedirectUrl("/landperson/" + xlp.getXperson());
			return responseHelper.getResponse();
		}

		// if new
		long count = landPersonService.save(landPerson);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save person info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Person info saved successfully");
		responseHelper.setRedirectUrl("/landperson/" + landPerson.getXperson());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xperson}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xperson) {
		return doArchiveOrRestore(xperson, true);
	}

	@PostMapping("/restore/{xperson}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xperson) {
		return doArchiveOrRestore(xperson, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xperson, boolean archive) {
		LandPerson lp = landPersonService.findByLandPerson(xperson);
		if (lp == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		lp.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = landPersonService.update(lp);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Person Information updated successfully");
		responseHelper.setRedirectUrl("/landperson/" + lp.getXperson());
		return responseHelper.getResponse();
	}

	// For Person Education

	@GetMapping("/{xperson}/education/{xrow}/show")
	public String loadEducationModal(@PathVariable String xperson, @PathVariable String xrow, Model model) {
		if ("new".equalsIgnoreCase(xrow)) {
			LandEducation lpe = new LandEducation();
			lpe.setXyear("");
			lpe.setXexam("");
			lpe.setXresult("");
			lpe.setXperson(xperson);
			model.addAttribute("lpe", lpe);
		} else {
			LandEducation lpe = landPersonService.findLandEducationdetailByXpersonAndXrow(xperson,
					Integer.parseInt(xrow));
			if (lpe == null) {
				lpe = new LandEducation();
				lpe.setXyear("");
				lpe.setXexam("");
				lpe.setXresult("");
			}
			model.addAttribute("lpe", lpe);
		}

		return "pages/land/educationmodal::educationmodal";
	}

	@PostMapping("/education/save")
	public @ResponseBody Map<String, Object> savePersonEducation(LandEducation landEducation) {
		if (landEducation == null || StringUtils.isBlank(landEducation.getXperson())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validation
		if (StringUtils.isBlank(landEducation.getXyear())) {
			responseHelper.setErrorStatusAndMessage("Please select Your Year");
			return responseHelper.getResponse();
		}
		if (landEducation.getXmajor() == null || StringUtils.isBlank(landEducation.getXmajor())) {
			responseHelper.setErrorStatusAndMessage("Please Enter your Major");
			return responseHelper.getResponse();
		}
		if (StringUtils.isBlank(landEducation.getXresult())) {
			responseHelper.setErrorStatusAndMessage("Please Enter Your Result");
			return responseHelper.getResponse();
		}

		// if existing
		LandEducation existPerson = landPersonService.findLandEducationdetailByXpersonAndXrow(landEducation.getXperson(), landEducation.getXrow());
		if (existPerson != null) {
			BeanUtils.copyProperties(landEducation, existPerson, "xperson");
			long count = landPersonService.update(existPerson);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("educationtable",
					"/landperson/education/" + landEducation.getXperson());
			responseHelper.setSuccessStatusAndMessage("Person Education updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = landPersonService.save(landEducation);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("educationtable",
				"/landperson/education/" + landEducation.getXperson());
		responseHelper.setSuccessStatusAndMessage("Person Education saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/education/{xperson}")
	public String reloadEducationTable(@PathVariable String xperson, Model model) {
		List<LandEducation> educationList = landPersonService.findByPersonEducation(xperson);
		model.addAttribute("lpelist", educationList);
		model.addAttribute("person", landPersonService.findByLandPerson(xperson));
		return "pages/land/landperson::educationtable";
	}

	@PostMapping("{xperson}/education/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePersonDetail(@PathVariable String xperson, @PathVariable String xrow,
			Model model) {
		LandEducation lpe = landPersonService.findLandEducationdetailByXpersonAndXrow(xperson, Integer.parseInt(xrow));
		if (lpe == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = landPersonService.deleteDetail(lpe);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("educationtable", "/landperson/education/" + xperson);
		return responseHelper.getResponse();
	}

	// For Person Land Document

	@GetMapping("/{xperson}/person/{xrow}/show")
	public String loadPersonDocModal(@PathVariable String xperson, @PathVariable String xrow, Model model) {
		if ("new".equalsIgnoreCase(xrow)) {
			LandDocument lpd = new LandDocument();
			lpd.setXname("");
			lpd.setXsurveyor("");
			lpd.setXland("");
			/* lpd.setNewData(true); */
			lpd.setXperson(xperson);
			model.addAttribute("lpd", lpd);
			lpd.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
			lpd.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
			model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
			model.addAttribute("prefixes",xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		} else {
			LandDocument lpd = landDocumentService.findLandPersonDocumentByXpersonAndXrow(xperson,
					Integer.parseInt(xrow));
			if (lpd == null) {
				lpd = new LandDocument();
				lpd.setXsurveyor("");
				lpd.setXland("");
				lpd.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
				lpd.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
				model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
				model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
			}
			model.addAttribute("lpd", lpd);
		}

		return "pages/land/persondocumentmodal::persondocumentmodal";
	}

	@PostMapping("/persondoc/save")
	public @ResponseBody Map<String, Object> savePersonDoc(LandDocument landDocument, @RequestParam("files[]") MultipartFile[] files) {
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

			String fileName = UUID.randomUUID() + "." + extension;
			log.debug("File name is now: {}", fileName);

			try {
				// create a directory if not exist
				String uploadPath = "D://Bosila//Document";
				File dir = new File(uploadPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// Upload file into server
				Files.copy(files[0].getInputStream(), Paths.get(uploadPath, fileName));
				landDocument.setXdocument(uploadPath + "/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// if existing
		LandDocument existPerson = landDocumentService.findLandPersonDocumentByXpersonAndXrow(landDocument.getXperson(), landDocument.getXrow());
		if (existPerson != null) {
			BeanUtils.copyProperties(landDocument, existPerson, "xdoc");
			long count = landDocumentService.update(existPerson);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("persondocumenttable", "/landperson/person/" + existPerson.getXperson());
			responseHelper.setSuccessStatusAndMessage("Person Document updated successfully");
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
		responseHelper.setReloadSectionIdWithUrl("persondocumenttable","/landperson/person/" + landDocument.getXperson());
		responseHelper.setSuccessStatusAndMessage("Person Document saved successfully");
		return responseHelper.getResponse();

	}

	@GetMapping("/person/{xperson}")
	public String reloadPersonDocTable(@PathVariable String xperson, Model model) {
		List<LandDocument> LandPersonDocList = landDocumentService.findByLandPersonDocument(xperson);
		model.addAttribute("lpdlist", LandPersonDocList);
		model.addAttribute("person", landPersonService.findByLandPerson(xperson));
		model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("prefixes",
				xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		return "pages/land/landperson::persondocumenttable";
	}
	
	@PostMapping("{xperson}/person/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePersonDocDetail(@PathVariable String xperson,
			@PathVariable String xrow, Model model) {
		LandDocument lpe = landDocumentService.findLandPersonDocumentByXpersonAndXrow(xperson, Integer.parseInt(xrow));
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
		responseHelper.setReloadSectionIdWithUrl("persondocumenttable", "/landperson/person/" + xperson);
		return responseHelper.getResponse();
	}
}
