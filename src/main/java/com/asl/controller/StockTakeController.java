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

import com.asl.entity.Imtag;
import com.asl.entity.Imtdet;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtagService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/stocktake")
public class StockTakeController extends ASLAbstractController {
	
	@Autowired
	private ImtagService imtagService;
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	

	
	@GetMapping
	public String loadStockTakePage(Model model) {
		
		model.addAttribute("imtag", getDefaultImtag());
		model.addAttribute("imtagprefix", xtrnService.findByXtypetrn(TransactionCodeType.STOCK_TAKE.getCode()));
		model.addAttribute("allimtags", imtagService.getAllImTag());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("imtagstatusList", xcodeService.findByXtype(CodeType.STATUS.getCode()));
		
		return "pages/inventory/stocktake/imtag";
	}
	
	@GetMapping("/{xtagnum}")
	public String loadStockTakePage(@PathVariable String xtagnum, Model model) {
		
		Imtag data = imtagService.findImtagByXtagnum(xtagnum);
		if(data == null) data = getDefaultImtag();

		model.addAttribute("imtag", data);
		model.addAttribute("imtagprefix", xtrnService.findByXtypetrn(TransactionCodeType.STOCK_TAKE.getCode()));
		model.addAttribute("allimtags", imtagService.getAllImTag());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("imtagstatusList", xcodeService.findByXtype(CodeType.STATUS.getCode()));
		model.addAttribute("imtagDetailsList", imtagService.findImtdetByXtagnum(xtagnum));
		
		return "pages/inventory/stocktake/imtag";
	}
	
	private Imtag getDefaultImtag() {
		Imtag imtag= new Imtag();
		imtag.setXtype(TransactionCodeType.STOCK_TAKE.getCode());
		imtag.setXtypetrn(TransactionCodeType.STOCK_TAKE.getCode());
		imtag.setXtrn(TransactionCodeType.STOCK_TAKE.getdefaultCode());
		imtag.setXtrnimtag(TransactionCodeType.STOCK_TAKE.getdefaultCode()); // Removal queue
		return imtag;
	}
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Imtag imtag, BindingResult bindingResult){
		
		
		if((imtag == null || StringUtils.isBlank(imtag.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		
		Imtag existImtag = imtagService.findImtagByXtagnum(imtag.getXtagnum());
		if(existImtag != null) {
			BeanUtils.copyProperties(imtag, existImtag, "xtagnum", "xtype", "xdate");
			long count = imtagService.updateImtag(existImtag);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Stock Take Entry updated successfully");
			responseHelper.setRedirectUrl("/inventory/stocktake/" + imtag.getXtagnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtagService.saveImtag(imtag);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Stock Take Entry created successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" + imtag.getXtagnum());
		return responseHelper.getResponse();
		
	}

	
	@GetMapping("/imtagdetail/{xtagnum}")
	public String reloadImtagDetailTable(@PathVariable String xtagnum, Model model) {
		List<Imtdet> detailList = imtagService.findImtdetByXtagnum(xtagnum);
		model.addAttribute("imtagDetailsList", detailList);
		Imtag header = new Imtag();
		header.setXtagnum(xtagnum);
		model.addAttribute("imtag", header);
		return "pages/inventory/stocktake/imtag::imtagdetailtable";
	}
	
	@GetMapping("{xtagnum}/imtagdetail/{xrow}/show")
	public String openImtagDetailModal(@PathVariable String xtagnum, @PathVariable String xrow, Model model) {		

		if("new".equalsIgnoreCase(xrow)) {
			Imtdet imtdet = new Imtdet();
			imtdet.setXtagnum(xtagnum);
			imtdet.setXqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));			
			model.addAttribute("imtagdetail", imtdet);
		} else {
			Imtdet imtdet = imtagService.findImtdetByXtagnumAndXrow(xtagnum, Integer.parseInt(xrow));
			if(imtdet == null) {
				imtdet = new Imtdet();
				imtdet.setXtagnum(xtagnum);
				imtdet.setXqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtagdetail", imtdet);
		}

		return "pages/inventory/stocktake/imtagdetailmodal::imtagdetailmodal";
	}
	
	@PostMapping("/imtagdetail/save")
	public @ResponseBody Map<String, Object> saveImtagDetail(Imtdet imtdet){
		
		if(imtdet == null || StringUtils.isBlank(imtdet.getXtagnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		// if existing
		Imtdet existDetail = imtagService.findImtdetByXtagnumAndXrow(imtdet.getXtagnum(), imtdet.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(imtdet, existDetail, "xtagnum", "xrow");
			long count = imtagService.updateImtdet(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/inventory/stocktake/" +  imtdet.getXtagnum());
			responseHelper.setSuccessStatusAndMessage("Stock-Take entry detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtagService.saveImtdet(imtdet);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/inventory/stocktake/" +  imtdet.getXtagnum());
		responseHelper.setSuccessStatusAndMessage("Stock-Take entry detail saved successfully");
		
		return responseHelper.getResponse();
	}
	
	
	@PostMapping("{xtagnum}/imtagdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteImtagDetail(@PathVariable String xtagnum, @PathVariable String xrow, Model model) {
		Imtdet imtdet = imtagService.findImtdetByXtagnumAndXrow(xtagnum, Integer.parseInt(xrow));
		if(imtdet == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtagService.deleteImtdet(imtdet);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" +  xtagnum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/confirmstocktake/{xtagnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xtagnum){
		if(StringUtils.isBlank(xtagnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		//Get PogrnHeader record by Xgrnnum		
		Imtag imtag = imtagService.findImtagByXtagnum(xtagnum);
		// Validate		
		if("Confirmed".equalsIgnoreCase(imtag.getXstatustag())) {
			responseHelper.setErrorStatusAndMessage("TAG already confirmed");
			return responseHelper.getResponse();
		}		
		Xcodes xcode = xcodeService.findByXtypesAndXcodes(CodeType.WAREHOUSE.getCode(), imtag.getXwh());
		if(xcode == null) {
			responseHelper.setErrorStatusAndMessage("A valid warehouse must be selected.");
			return responseHelper.getResponse();
		}
		List<Imtdet> imtdetList = imtagService.findImtdetByXtagnum(xtagnum);
		if(imtdetList.size() == 0) {
			responseHelper.setErrorStatusAndMessage("Please add items to confirm stock-take!");
			return responseHelper.getResponse();
		}
		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(imtag.getXstatustag())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			//pogrnService.procInventory(x, pogrnHeader.getXpornum(), p_seq);
		}
		
		responseHelper.setSuccessStatusAndMessage("Imtag Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/stocktake/" + imtag.getXtagnum());
		return responseHelper.getResponse();
	}
	

}
