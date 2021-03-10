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

import com.asl.entity.Pogrndetail;
import com.asl.entity.Pogrnheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/purchasing/pogrn")
public class PogrnController extends ASLAbstractController {
	
	@Autowired
	private PogrnService pogrnService;
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@GetMapping
	public String loadGRNPage(Model model) {
		
		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {
		
		Pogrnheader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum); 
		if(data == null) data = getDefaultPogrnHeader();

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode()));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		//model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		
		return "pages/purchasing/pogrn/pogrn";
	}
	
	private Pogrnheader getDefaultPogrnHeader() {
		Pogrnheader pogrn = new Pogrnheader();
		pogrn.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXtotamt(BigDecimal.ZERO);
		return pogrn;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pogrnheader pogrnHeader, BindingResult bindingResult){
		
		
		if((pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		
		Pogrnheader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
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
		List<Pogrndetail> detailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		model.addAttribute("pogrnDetailsList", detailList);
		Pogrnheader header = new Pogrnheader();
		header.setXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", header);
		return "pages/purchasing/pogrn/pogrn::pogrndetailtable";
	}
	
	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {

		model.addAttribute("purUnitList", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			Pogrndetail pogrndetail = new Pogrndetail();
			pogrndetail.setXgrnnum(xgrnnum);
			pogrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pogrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("pogrndetail", pogrndetail);
		} else {
			Pogrndetail pogrndetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
			if(pogrndetail == null) {
				pogrndetail = new Pogrndetail();
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
	public @ResponseBody Map<String, Object> savePogrndetail(Pogrndetail pogrnDetail){
		
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		//pogrnDetail.setXlineamt(pogrnDetail.getXqtyord().multiply(pogrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Pogrndetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrnDetail.getXgrnnum(), pogrnDetail.getXrow());
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
		Pogrndetail pd = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
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

}
