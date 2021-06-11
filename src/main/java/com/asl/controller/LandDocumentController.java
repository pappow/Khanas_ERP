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

import com.asl.entity.LandDocument;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.LandDocumentService;

@Controller
@RequestMapping("/landdocument")
public class LandDocumentController extends ASLAbstractController{
	
	@Autowired private LandDocumentService landDocumentService;
	
	@GetMapping
	public String loadLandDocumentPage(Model model){
		model.addAttribute("document", getDefaultLandDocument());
		model.addAttribute("allDocument", landDocumentService.getAllLandDocument());
		model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		return "pages/land/landdocument";
	}

	private LandDocument getDefaultLandDocument() {
		LandDocument ld  = new LandDocument();
		ld.setXname("Certificate");
		ld.setXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode());
		ld.setXtrn(TransactionCodeType.DOCUMENT_NAME.getdefaultCode());
		return ld;
	}
	
	@GetMapping("/{xdoc}")
	public String loadRolePage(@PathVariable String xdoc, Model model) {
		LandDocument landDocument = landDocumentService.findByLandDocument(xdoc);
		if (landDocument == null) return "redirect:/landdocument";

		model.addAttribute("document", getDefaultLandDocument());
		model.addAttribute("allDocument", landDocumentService.getAllLandDocument());
		model.addAttribute("dt", xcodesService.findByXtype(CodeType.DOCUMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("prefixes", xtrnService.findByXtypetrn(TransactionCodeType.DOCUMENT_NAME.getCode(), Boolean.TRUE));
		return "pages/land/landdocument";
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(LandDocument landDocument, BindingResult bindingResult) {
		if (landDocument == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		// Validation
		if(StringUtils.isBlank(landDocument.getXdocument())) {
			responseHelper.setErrorStatusAndMessage("Please Insert Your Document");
			return responseHelper.getResponse();
		}

		// if existing
		if(StringUtils.isNotBlank(landDocument.getXdoc())) {
			LandDocument exist = landDocumentService.findByLandDocument(landDocument.getXdoc());
			
			BeanUtils.copyProperties(landDocument, exist, "xtypetrn","xtrn");
			long count = landDocumentService.update(exist);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update land info");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Person Experience info updated successfully");
			responseHelper.setRedirectUrl("/landdocument/" + exist.getXdoc());
			return responseHelper.getResponse();
		}
		
		// if new
		long count = landDocumentService.save(landDocument);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save Document info");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Document info saved successfully");
		responseHelper.setRedirectUrl("/landdocument/" + landDocument.getXdoc());
		return responseHelper.getResponse();
	}
	
}
