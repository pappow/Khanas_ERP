package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
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

import com.asl.entity.Caitem;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.Opordheader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.ImtorService;
import com.asl.service.OpordService;
import com.asl.service.XcodesService;

@Controller
@RequestMapping("/inventory/transferorder")
public class StockTransferOrderController extends ASLAbstractController {

	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private ImstockService imstockService;
	@Autowired private CaitemService caitemService;
	@Autowired private OpordService opordService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeader());
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXcode(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		if(isBoshila()) {
			return "pages/land/inventory/transferorder/imtor";
		}
		return "pages/inventory/transferorder/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) data = getDefaultImtorHeader();

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeader());
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("torstatusList", xcodeService.findByXtype(CodeType.TRANSFER_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		if(isBoshila()) {
			return "pages/land/inventory/transferorder/imtor";
		}
		return "pages/inventory/transferorder/imtor";
	}

	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		imtorHeader.setXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getdefaultCode());
		imtorHeader.setXstatustor("Open");
		return imtorHeader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(ImtorHeader imtorHeader, BindingResult bindingResult){
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if(imtorHeader.getXfwh().equalsIgnoreCase(imtorHeader.getXtwh())) {
			responseHelper.setErrorStatusAndMessage("Stock can't be transfered to same warehouse !");
			return responseHelper.getResponse();
		}

		// chalan reference validation
		if(StringUtils.isNotBlank(imtorHeader.getXchalanref())) {
			Opordheader chalan = opordService.findOpordHeaderByXordernum(imtorHeader.getXchalanref());
			if(chalan == null) {
				responseHelper.setErrorStatusAndMessage("Invalid chalan reference");
				return responseHelper.getResponse();
			}
		}
		//TODO: 

		// If existing
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtypetrn", "xtrn");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update Transfer Order");
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Transfer Order Updated Successfully");
			responseHelper.setRedirectUrl("/inventory/transferorder/" + existImtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Create Transfer Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Transfer Order Created Successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder/" + imtorHeader.getXtornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xtornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't archive transfer order");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> details = imtorService.findImtorDetailByXtornum(xtornum);
		if(details != null && !details.isEmpty()) {
			long dcount = imtorService.deleteImtorDetailByXtornum(xtornum);
			if(dcount == 0) {
				responseHelper.setErrorStatusAndMessage("Can't archive transfer order");
				return responseHelper.getResponse();
			}
		}

		imtorHeader.setZactive(Boolean.FALSE);
		long count = imtorService.update(imtorHeader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't archive transfer order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted Successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder");
		return responseHelper.getResponse();
	}

	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) return "redirect:/inventory/transferorder";

		model.addAttribute("fromWarehouse", imtorHeader.getXfwh());
		model.addAttribute("toWarehouse", imtorHeader.getXfwh());

		if("new".equalsIgnoreCase(xrow)) {
			ImtorDetail imtordetail = new ImtorDetail();
			imtordetail.setXtornum(xtornum);
			imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("imtordetail", imtordetail);
			model.addAttribute("availablestock", Collections.emptyList());
		} else {
			ImtorDetail imtordetail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
			if(imtordetail == null) {
				imtordetail = new ImtorDetail();
				imtordetail.setXtornum(xtornum);
				imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtordetail", imtordetail);
			model.addAttribute("availablestock", imstockService.findByXitem(imtordetail.getXitem()));
		}
		return "pages/inventory/transferorder/imtordetailmodal::imtordetailmodal";
	}

	@PostMapping("/imtordetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(ImtorDetail imtorDetail, String xfwh){
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum()) || StringUtils.isBlank(imtorDetail.getXitem())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(imtorDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if(imtorDetail.getXrow() == 0 && imtorService.findImtorDetailByXtornumAndXitem(imtorDetail.getXtornum(), imtorDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		if(imtorDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please enter valid quantity");
			return responseHelper.getResponse();
		}

		// if existing
		ImtorDetail existDetail = imtorService.findImtorDetailByXtornumAndXrow(imtorDetail.getXtornum(), imtorDetail.getXrow());
		if(existDetail != null) {
			// check the item is transfer eligible, that means it has enough stock available
			Imstock stock = imstockService.findByXitemAndXwh(imtorDetail.getXitem(), xfwh);
			if(stock == null) {
				responseHelper.setErrorStatusAndMessage("There is no stock information available in system for item : " + imtorDetail.getXitem());
				return responseHelper.getResponse();
			}
			if(stock.getXinhand().compareTo(imtorDetail.getXqtyord()) == -1) {
				responseHelper.setErrorStatusAndMessage("Stock not available");
				return responseHelper.getResponse();
			}

			BeanUtils.copyProperties(imtorDetail, existDetail, "xtornum", "xrow");
			long count = imtorService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/transferorder/imtordetail/" + imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Transfer Order detail updated successfully");
			return responseHelper.getResponse();
		}

		Imstock stock = imstockService.findByXitemAndXwh(imtorDetail.getXitem(), xfwh);
		if(stock == null) {
			responseHelper.setErrorStatusAndMessage("There is no stock information available in system for item : " + imtorDetail.getXitem());
			return responseHelper.getResponse();
		}
		if(stock.getXavail().compareTo(imtorDetail.getXqtyord()) == -1) {
			responseHelper.setErrorStatusAndMessage("Stock not available");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtorService.saveDetail(imtorDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/transferorder/imtordetail/" + imtorDetail.getXtornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/imtordetail/{xtornum}")
	public String reloadImtorDetailTabble(@PathVariable String xtornum, Model model) {
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		model.addAttribute("imtorheader", imtorService.findImtorHeaderByXtornum(xtornum));
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
		responseHelper.setReloadSectionIdWithUrl("imtordetailtable", "/inventory/transferorder/imtordetail/" + xtornum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmimtor/{xtornum}")
	public @ResponseBody Map<String, Object> confirmimtor(@PathVariable String xtornum){
		ImtorHeader imtorHeader = imtorService.findImtorHeaderByXtornum(xtornum);
		if(imtorHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if("Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			responseHelper.setErrorStatusAndMessage("Transfer Order already confirmed");
			return responseHelper.getResponse();
		}

		List<ImtorDetail> imtorDetailList = imtorService.findImtorDetailByXtornum(xtornum);
		if(imtorDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Please add detail!");
			return responseHelper.getResponse();
		}

		// Stock validation check
		StringBuilder error = new StringBuilder();
		for(ImtorDetail id : imtorDetailList) {
			Imstock is = imstockService.findByXitemAndXwh(id.getXitem(), imtorHeader.getXfwh());
			if(is == null || is.getXinhand().compareTo(id.getXqtyord()) == -1) {
				error.append("Item " + is.getXitem() + " not available in " + imtorHeader.getXfwh() + " to do transfer\n");
			}
		}
		if(StringUtils.isNotBlank(error.toString())) {
			responseHelper.setErrorStatusAndMessage(error.toString());
			return responseHelper.getResponse();
		}

		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(imtorHeader.getXstatustor())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			imtorService.procConfirmTO(xtornum, "Issue and Receive", p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("Transfer Order Confirmed successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder/" + xtornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}

	@GetMapping("/stock/{xitem}")
	public String findAvailableStockInfo(@PathVariable String xitem, Model model){
		model.addAttribute("availablestock", imstockService.findByXitem(xitem));
		return "pages/inventory/transferorder/imtordetailmodal::stocktable";
	}
}
