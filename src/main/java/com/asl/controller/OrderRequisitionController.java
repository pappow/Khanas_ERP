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

import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.PoordService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/purchasing/requisition")
public class OrderRequisitionController extends ASLAbstractController {

	@Autowired private XcodesService xcodeService;
	@Autowired private PoordService poordService;
	@Autowired private XtrnService xtrnService;

	@GetMapping
	public String loadPoordPage(Model model) {
		model.addAttribute("poordheader", getDefaultPoordHeader());
		model.addAttribute("poprefix", xtrnService.findByXtypetrn(TransactionCodeType.REQUISITION_ORDER.getCode()));
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtype(TransactionCodeType.REQUISITION_ORDER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.REQUISITION_ORDER_STATUS.getCode()));
		return "pages/purchasing/requisition/poord";
	}

	@GetMapping("/{xpornum}")
	public String loadPoordPage(@PathVariable String xpornum, Model model) {
		PoordHeader data = poordService.findPoordHeaderByXpornum(xpornum); 
		if(data == null) data = getDefaultPoordHeader();

		model.addAttribute("poordheader", data);
		model.addAttribute("poprefix", xtrnService.findByXtypetrn(TransactionCodeType.REQUISITION_ORDER.getCode()));
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtype(TransactionCodeType.REQUISITION_ORDER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.REQUISITION_ORDER_STATUS.getCode()));
		model.addAttribute("poorddetailsList", poordService.findPoorddetailByXpornum(xpornum));
		return "pages/purchasing/requisition/poord";
	}

	private PoordHeader getDefaultPoordHeader() {
		PoordHeader poord = new PoordHeader();
		poord.setXtype(TransactionCodeType.REQUISITION_ORDER.getCode());
		poord.setXtotamt(BigDecimal.ZERO);
		return poord;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PoordHeader poordHeader, BindingResult bindingResult){
		if((poordHeader == null || StringUtils.isBlank(poordHeader.getXtype())) || StringUtils.isBlank(poordHeader.getXtrnpor()) && StringUtils.isBlank(poordHeader.getXpornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		PoordHeader existPoordHeader = poordService.findPoordHeaderByXpornum(poordHeader.getXpornum());
		if(existPoordHeader != null) {
			BeanUtils.copyProperties(poordHeader, existPoordHeader, "xpornum", "xtype", "xdate", "xtotamt");
			long count = poordService.update(existPoordHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Requisition Order updated successfully");
			responseHelper.setRedirectUrl("/purchasing/requisition/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = poordService.save(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Requisition Order created successfully");
		responseHelper.setRedirectUrl("/purchasing/requisition/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}

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
		if(poordHeader == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		poordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = poordService.update(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Requisition order updated successfully");
		responseHelper.setRedirectUrl("/purchasing/requisition/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}

	@GetMapping("{xpornum}/poorddetail/{xrow}/show")
	public String openPoordDetailModal(@PathVariable String xpornum, @PathVariable String xrow, Model model) {

		model.addAttribute("purchaseUnit", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			PoordDetail poorddetail = new PoordDetail();
			poorddetail.setXpornum(xpornum);
			poorddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			poorddetail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			model.addAttribute("poorddetail", poorddetail);
		} else {
			PoordDetail poorddetail = poordService.findPoorddetailByXportNumAndXrow(xpornum, Integer.parseInt(xrow));
			if(poorddetail == null) {
				poorddetail = new PoordDetail();
				poorddetail.setXpornum(xpornum);
				poorddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				poorddetail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			}
			model.addAttribute("poorddetail", poorddetail);
		}

		return "pages/purchasing/requisition/poorddetailmodal::poorddetailmodal";
	}

	@PostMapping("/poorddetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(PoordDetail poordDetail){
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if(poordDetail.getXrow() == 0 && poordService.findPoorddetailByXpornumAndXitem(poordDetail.getXpornum(), poordDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// if existing
		PoordDetail existDetail = poordService.findPoorddetailByXportNumAndXrow(poordDetail.getXpornum(), poordDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(poordDetail, existDetail, "xpornum", "xrow");
			long count = poordService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/purchasing/requisition/" +  poordDetail.getXpornum());
			responseHelper.setSuccessStatusAndMessage("Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = poordService.saveDetail(poordDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/purchasing/requisition/" +  poordDetail.getXpornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/poorddetail/{xpornum}")
	public String reloadPoordDetailTabble(@PathVariable String xpornum, Model model) {
		List<PoordDetail> detailList = poordService.findPoorddetailByXpornum(xpornum);
		model.addAttribute("poorddetailsList", detailList);
		PoordHeader header = new PoordHeader();
		header.setXpornum(xpornum);
		model.addAttribute("poordheader", header);
		return "pages/purchasing/requisition/poord::poorddetailtable";
	}

	@PostMapping("{xpornum}/poorddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xpornum, @PathVariable String xrow, Model model) {
		PoordDetail pd = poordService.findPoorddetailByXportNumAndXrow(xpornum, Integer.parseInt(xrow));
		if(pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = poordService.deleteDetail(pd);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/purchasing/requisition/" +  xpornum);
		return responseHelper.getResponse();
	}
}
