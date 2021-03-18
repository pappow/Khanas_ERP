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

import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/procurement/grnreturn")
public class GrnReturnController extends ASLAbstractController {
	
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private PocrnService pocrnService;
	@Autowired
	private PogrnService pogrnService;
	
	@GetMapping
	public String loadGrnReturnPage(Model model) {
		
		model.addAttribute("pocrnheader", getDefaultPocrnHeader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		
		return "pages/procurement/grnreturn/pocrn";
	}
	
	@GetMapping("/{xcrnnum}")
	public String loadGrnReturnPage(@PathVariable String xcrnnum, Model model) {
		
		Pocrnheader data = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum); 
		if(data == null) data = getDefaultPocrnHeader();

		model.addAttribute("pocrnheader", data);
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		model.addAttribute("pocrnDetailsList", pocrnService.findPocrnDetailByXcrnnum(xcrnnum));
		
		return "pages/procurement/grnreturn/pocrn";
	}
	
	private Pocrnheader getDefaultPocrnHeader() {
		Pocrnheader pocrn = new Pocrnheader();
		//pogrn.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		//pogrn.setXtypetrn("Purchase");
		//pogrn.setXtotamt(BigDecimal.ZERO);
		return pocrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pocrnheader pocrnHeader, BindingResult bindingResult){
		
		
		if((pocrnHeader == null || StringUtils.isBlank(pocrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		
		Pocrnheader existPocrnHeader = pocrnService.findPocrnHeaderByXcrnnum(pocrnHeader.getXcrnnum());
		if(existPocrnHeader != null) {
			BeanUtils.copyProperties(pocrnHeader, existPocrnHeader, "xcrnnum", "xgrnnum", "xtype", "xdate");
			long count = pocrnService.update(existPocrnHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("PRN updated successfully");
			responseHelper.setRedirectUrl("/procurement/grnreturn/" + pocrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pocrnService.save(pocrnHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("PRN created successfully");
		responseHelper.setRedirectUrl("/procurement/grnreturn/" + pocrnHeader.getXcrnnum());
		return responseHelper.getResponse();
		
	}
	
	@GetMapping("/pocrndetail/{xcrnnum}")
	public String reloadPocrnDetailTable(@PathVariable String xcrnnum, Model model) {
		List<Pocrndetail> detailList = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);
		model.addAttribute("pocrnDetailsList", detailList);
		Pocrnheader header = new Pocrnheader();
		header.setXcrnnum(xcrnnum);
		model.addAttribute("pocrnheader", header);
		return "pages/procurement/grnreturn/pocrn::pocrndetailtable";
	}
	
	
	@GetMapping("{xcrnnum}/pocrndetail/{xrow}/show")
	public String openPocrnDetailModal(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {

		if("new".equalsIgnoreCase(xrow)) {
			Pocrndetail pocrndetail = new Pocrndetail();
			pocrndetail.setXcrnnum(xcrnnum);
			pocrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pocrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pocrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			//pocrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("pocrndetail", pocrndetail);
		} else {
			Pocrndetail pocrndetail = pocrnService.findPocrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
			if(pocrndetail == null) {
				pocrndetail = new Pocrndetail();
				pocrndetail.setXcrnnum(xcrnnum);
				pocrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pocrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pocrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("pocrndetail", pocrndetail);
		}

		return "pages/procurement/grnreturn/pocrndetailmodal::pocrndetailmodal";
	}
	
	@PostMapping("/pocrndetail/save")
	public @ResponseBody Map<String, Object> savePocrndetail(Pocrndetail pocrnDetail){
		
		if(pocrnDetail == null || StringUtils.isBlank(pocrnDetail.getXcrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		// modify line amount
		pocrnDetail.setXlineamt(pocrnDetail.getXqtygrn().multiply(pocrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));
		
		// if existing
		Pocrndetail existDetail = pocrnService.findPocrnDetailByXcrnnumAndXrow(pocrnDetail.getXcrnnum(), pocrnDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(pocrnDetail, existDetail, "xcrnnum", "xrow");
			long count = pocrnService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/procurement/grnreturn/" +  pocrnDetail.getXcrnnum());
			responseHelper.setSuccessStatusAndMessage("PRN item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pocrnService.saveDetail(pocrnDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/procurement/grnreturn/" +  pocrnDetail.getXcrnnum());
		responseHelper.setSuccessStatusAndMessage("PRN item saved successfully");
		
		return responseHelper.getResponse();
	}
	
	@PostMapping("{xcrnnum}/pocrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePocrnDetail(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {
		Pocrndetail pd = pocrnService.findPocrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
		if(pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pocrnService.deleteDetail(pd);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/procurement/grnreturn/" +  xcrnnum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/confirmprn/{xcrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xcrnnum){
		if(StringUtils.isBlank(xcrnnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		
		//Get PocrnHeader record by Xcrnnum
		Pocrnheader pocrnHeader = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pocrnHeader.getXgrnnum());
		
		pocrnService.procConfirmCRN(xcrnnum);
		pocrnService.procIssuePricing(pogrnHeader.getXdocnum().toString(), pocrnHeader.getXwh());
		pocrnService.procTransferPRtoAP(xcrnnum);
		
		responseHelper.setSuccessStatusAndMessage("PRN Confirmed successfully");
		responseHelper.setRedirectUrl("/procurement/grnreturn/" + xcrnnum);
		return responseHelper.getResponse();
	}
	
	
	


}
