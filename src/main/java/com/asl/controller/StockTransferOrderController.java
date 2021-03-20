package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtorService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/transferorder")
public class StockTransferOrderController extends ASLAbstractController {
	
	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeader());
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		//model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		
		return "pages/inventory/transferorder/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) data = getDefaultImtorHeader();

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeader());
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		return "pages/inventory/transferorder/imtor";
	}

	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		//imtorHeader.setXtype(TransactionCodeType.PURCHASE_ORDER.getCode());
		//imtorHeader.setXtotamt(BigDecimal.ZERO);
		return imtorHeader;
	}
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(ImtorHeader imtorHeader, BindingResult bindingResult){
		if((imtorHeader == null) || StringUtils.isBlank(imtorHeader.getXtrntor()) && StringUtils.isBlank(imtorHeader.getXtornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		
		imtorHeader.setXtype(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrntor(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getdefaultCode());

		// if existing record
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtornum", "xdate");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Transfer Order updated successfully");
			responseHelper.setRedirectUrl("/inventory/transferorder/" + imtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Purchase Order created successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder/" + imtorHeader.getXtornum());
		return responseHelper.getResponse();
	}
	/*
	@PostMapping("/archive/{xpornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, true);
	}

	@PostMapping("/restore/{xpornum}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xpornum, boolean archive){
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null || "GRN Created".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		poordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = poordService.update(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase order updated successfully");
		responseHelper.setRedirectUrl("/purchasing/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}
	*/
	
	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {

		//model.addAttribute("purchaseUnit", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			ImtorDetail imtordetail = new ImtorDetail();
			imtordetail.setXtornum(xtornum);
			imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("imtordetail", imtordetail);
		} else {
			ImtorDetail imtordetail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
			if(imtordetail == null) {
				imtordetail = new ImtorDetail();
				imtordetail.setXtornum(xtornum);
				imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtordetail", imtordetail);
		}

		return "pages/inventory/transferorder/imtordetailmodal::imtordetailmodal";
	}
	

	@PostMapping("/imtordetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(ImtorDetail imtorDetail){
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum()) || StringUtils.isBlank(imtorDetail.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if(imtorDetail.getXrow() == 0 && imtorService.findImtorDetailByXtornumAndXitem(imtorDetail.getXtornum(), imtorDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		
		// if existing
		ImtorDetail existDetail = imtorService.findImtorDetailByXtornumAndXrow(imtorDetail.getXtornum(), imtorDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(imtorDetail, existDetail, "xtornum", "xrow");
			long count = imtorService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/inventory/transferorder/" +  imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Transfer Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtorService.saveDetail(imtorDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/inventory/transferorder/" +  imtorDetail.getXtornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/imtordetail/{xtornum}")
	public String reloadImtorDetailTabble(@PathVariable String xtornum, Model model) {
		List<ImtorDetail> detailList = imtorService.findImtorDetailByXtornum(xtornum);
		model.addAttribute("imtordetailsList", detailList);
		ImtorHeader header = new ImtorHeader();
		header.setXtornum(xtornum);
		model.addAttribute("imtorheader", header);
		return "pages/inventory/transferorder/imtor::imtordetailtable";
	}
	
	
	@PostMapping("{xtornum}/imtordetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteImtorDetail(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorDetail detail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
		if(detail == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = imtorService.deleteDetail(detail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder/" +  xtornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/confirmimtor/{xtornum}")
	public @ResponseBody Map<String, Object> confirmimtor(@PathVariable String xtornum){
		if(StringUtils.isBlank(xtornum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		
		//Get PogrnHeader record by Xgrnnum		
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		// Validate
		
		if("Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Transfer Order already confirmed");
			return responseHelper.getResponse();
		}
		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.size() == 0) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}
		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			imtorService.procConfirmTO(xtornum, "Issue and Receive", p_seq);
		}
		
		responseHelper.setSuccessStatusAndMessage("Transfer Order Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder/" + xtornum);
		return responseHelper.getResponse();
	}
}
