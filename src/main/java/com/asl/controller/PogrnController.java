package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
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

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/purchasing/pogrn")
public class PogrnController extends ASLAbstractController {
	
	@Autowired
	private PogrnService pogrnService;
	@Autowired 
	private PocrnService pocrnService;
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private VataitService vataitService;
	
	@GetMapping
	public String loadGRNPage(Model model) {
		
		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {
		
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum); 
		if(data == null) data = getDefaultPogrnHeader();

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXtrngrn(TransactionCodeType.GRN_NUMBER.getdefaultCode());
		pogrn.setXtypetrn("Purchase");
		pogrn.setXtotamt(BigDecimal.ZERO);
		pogrn.setXvatamt(BigDecimal.ZERO);
		pogrn.setXdiscprime(BigDecimal.ZERO);
		pogrn.setXdate(new Date());
		return pogrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult){
		
		
		if((pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		if(StringUtils.isBlank(pogrnHeader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Please provide bill info");
			return responseHelper.getResponse();
		}

		// if existing record
		
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if(existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtype", "xdate", "xtotamt");
			long count = pogrnService.update(existPogrnHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/purchasing/pogrn/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pogrnService.save(pogrnHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
		
	}

	
	@GetMapping("/pogrndetail/{xgrnnum}")
	public String reloadPogrnDetailTable(@PathVariable String xgrnnum, Model model) {
		List<PogrnDetail> detailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		model.addAttribute("pogrnDetailsList", detailList);
		PogrnHeader header = new PogrnHeader();
		header.setXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", header);
		return "pages/purchasing/pogrn/pogrn::pogrndetailtable";
	}
	
	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum); 
		model.addAttribute("customer", data.getXcus());
		model.addAttribute("pornum", data.getXpornum());
		model.addAttribute("purUnitList", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			PogrnDetail pogrndetail = new PogrnDetail();
			pogrndetail.setXgrnnum(xgrnnum);
			pogrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pogrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("pogrndetail", pogrndetail);
		} else {
			PogrnDetail pogrndetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
			if(pogrndetail == null) {
				pogrndetail = new PogrnDetail();
				pogrndetail.setXgrnnum(xgrnnum);
				pogrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pogrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("pogrndetail", pogrndetail);
		}

		return "pages/purchasing/pogrn/pogrndetailmodal::pogrndetailmodal";
	}
	
	@PostMapping("/pogrndetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail pogrnDetail){
		
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		pogrnDetail.setXlineamt(pogrnDetail.getXqtygrn().multiply(pogrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		PogrnDetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrnDetail.getXgrnnum(), pogrnDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(pogrnDetail, existDetail, "xgrnnum", "xrow");
			long count = pogrnService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/purchasing/pogrn/" +  pogrnDetail.getXgrnnum());
			responseHelper.setSuccessStatusAndMessage("GRN Item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrnDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/purchasing/pogrn/" +  pogrnDetail.getXgrnnum());
		responseHelper.setSuccessStatusAndMessage("GRN Item saved successfully");
		
		return responseHelper.getResponse();
	}
	
	
	@PostMapping("{xgrnnum}/pogrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePogrnDetail(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnDetail pd = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
		if(pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pogrnService.deleteDetail(pd);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/" +  xgrnnum);
		return responseHelper.getResponse();
	}
	
	/*
	@GetMapping("/confirmgrn/{xgrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xgrnnum){
		if(StringUtils.isBlank(xgrnnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		
		//Get PogrnHeader record by Xgrnnum
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		
		if(pogrnHeader != null) {
			
			List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
			Imtrn imtrn;
			for(int i=0; i< pogrnDetailList.size(); i++) {
				
				imtrn = new Imtrn();
				BeanUtils.copyProperties(pogrnDetailList.get(i), imtrn);
				imtrn.setXdate(pogrnHeader.getXdate());
				imtrn.setXwh(pogrnHeader.getXwh());
				imtrn.setXqty(pogrnDetailList.get(i).getXqtygrn());
				imtrn.setXsign(+1);
				imtrn.setXtype(TransactionCodeType.INVENTORY_NUMBER.getCode());
				imtrn.setXtrnimtrn(TransactionCodeType.INVENTORY_NUMBER.getdefaultCode());
				//imtrn.set
				
				long imtrnCount = imtrnService.save(imtrn);
				if(imtrnCount == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}	
			}
			
			//Create Arhed (Account Payable)
			
			 Arhed arhed = new Arhed(); 
			 BeanUtils.copyProperties(pogrnHeader, arhed);
			 arhed.setXprime(pogrnHeader.getXtotamt());
			 //arhed.setXbalprime(pogrnHeader.getXtotamt());
			 arhed.setXbase(pogrnHeader.getXtotamt());
			 arhed.setXdiscprime(pogrnHeader.getXdiscprime());
			 arhed.setXsign(-1);
			 arhed.setXtyperec("Due");
			 arhed.setXstatusjv("Confirmed");
			 arhed.setXtype(TransactionCodeType.ACCOUNT_PAYABLE.getCode());
			 arhed.setXtrnarhed(TransactionCodeType.ACCOUNT_PAYABLE.getdefaultCode());
			 long arCount = arhedService.save(arhed);
			 
			 if(arCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}	
			 
			
			//Update PoordHeader Status
			
			PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(pornum);
			poordHeader.setXstatuspor("GRN Confirmed");
			long pCount = poordService.update(poordHeader);
			if(pCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}	
			
			//Update grn status
			pogrnHeader.setXstatusgrn("GRN Confirmed");
			long grnCount = pogrnService.update(pogrnHeader);
			if(grnCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			
			
			responseHelper.setSuccessStatusAndMessage("GRN Confirmed successfully");
			responseHelper.setRedirectUrl("/purchasing/pogrn/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
			
		}
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}*/
	
	@GetMapping("/confirmgrn/{xgrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xgrnnum){
		if(StringUtils.isBlank(xgrnnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		//Get PogrnHeader record by Xgrnnum		
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		// Validate
		
		if("Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("GRN already confirmed");
			return responseHelper.getResponse();
		}		
		Xcodes xcode = xcodeService.findByXtypesAndXcodes(CodeType.WAREHOUSE.getCode(), pogrnHeader.getXwh());
		if(xcode == null) {
			responseHelper.setErrorStatusAndMessage("A valid warehouse must be selected.");
			return responseHelper.getResponse();
		}
		List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if(pogrnDetailList.size() == 0) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}
		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procInventory(xgrnnum, pogrnHeader.getXpornum(), p_seq);
		}
				
		
		if(!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procTransferPOtoAP(xgrnnum, p_seq);
		}
		
		responseHelper.setSuccessStatusAndMessage("GRN Confirmed successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/" + xgrnnum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/returngrn/{xgrnnum}")
	public @ResponseBody Map<String, Object> creategrn(@PathVariable String xgrnnum){
		if(StringUtils.isBlank(xgrnnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// Get PoordHeader record by Xpornum
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		
		if(pogrnHeader != null) {
			Pocrnheader pocrnHeader = new Pocrnheader();
			BeanUtils.copyProperties(pogrnHeader, pocrnHeader, "xdate", "xtype", "xtrngrn", "xnote");
			pocrnHeader.setXgrnnum(xgrnnum);
			pocrnHeader.setXstatuscrn("Open");
			pocrnHeader.setXdate(new Date());
			pocrnHeader.setXsup(pogrnHeader.getXcus());
			pocrnHeader.setXtype(TransactionCodeType.PRN_NUMBER.getCode());
			pocrnHeader.setXtrncrn(TransactionCodeType.PRN_NUMBER.getdefaultCode());
			//pocrnHeader.setXtypetrn("??");
			
			long count = pocrnService.save(pocrnHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			
			pocrnHeader = pocrnService.findPocrnHeaderByXgrnnum(xgrnnum);
			//Get GRN items to copy them in CRN.
			List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
			Pocrndetail pocrnDetail;
			for(int i=0; i< pogrnDetailList.size(); i++) {
				pocrnDetail = new Pocrndetail();
				//Copying PO items to GRN items.
				BeanUtils.copyProperties(pogrnDetailList.get(i), pocrnDetail, "xrow", "xnote");
				pocrnDetail.setXcrnnum(pocrnHeader.getXcrnnum());
				pocrnDetail.setXqtygrn(pogrnDetailList.get(i).getXqtygrn());
				
				long nCount = pocrnService.saveDetail(pocrnDetail);
				
				// Update Inventory				
				if(nCount == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}				
			}
			
			//Update PoordHeader Status
			pogrnHeader.setXstatusgrn("GRN Returned"); // Is it final?
			long pCount = pogrnService.update(pogrnHeader);
			if(pCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}			
			 
			responseHelper.setSuccessStatusAndMessage("GRN Returned successfully");
			responseHelper.setRedirectUrl("/procurement/grnreturn" + pocrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}	
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}

}
