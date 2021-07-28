package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/procurements/purchasereturn")
public class PurchaseReturnController extends ASLAbstractController {

	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private PogrnService pogrnService;

	@GetMapping
	public String loadGrnReturnPage(Model model) {
		model.addAttribute("pocrnheader", getDefaultPocrnHeader());
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());

		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_RETURN.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));

		return "pages/procurement/grnreturn/pocrn";
	}

	@GetMapping("/{xcrnnum}")
	public String loadGrnReturnPage(@PathVariable String xcrnnum, Model model) {
		Pocrnheader data = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		if (data == null) return "redirect:/procurements/purchasereturn";

		model.addAttribute("pocrnheader", data);
		model.addAttribute("allPocrnHeader", pocrnService.getAllPocrnheader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("pocrnDetailsList", pocrnService.findPocrnDetailByXcrnnum(xcrnnum));

		return "pages/procurement/grnreturn/pocrn";
	}

	private Pocrnheader getDefaultPocrnHeader() {
		Pocrnheader pocrn = new Pocrnheader();
		pocrn.setXtypetrn(TransactionCodeType.PURCHASE_RETURN.getCode());
		pocrn.setXtype(TransactionCodeType.PURCHASE_RETURN.getdefaultCode());
		pocrn.setXstatuscrn("Open");
		pocrn.setXdate(new Date());
		pocrn.setXpaymenttype("Other");
		return pocrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pocrnheader pocrnHeader, BindingResult bindingResult) {

		// Validate
		if(StringUtils.isBlank(pocrnHeader.getXgrnnum())) {
			responseHelper.setErrorStatusAndMessage("GRN number selection required");
			return responseHelper.getResponse();
		}
		// Search supplier number now
		PogrnHeader pgh = pogrnService.findPogrnHeaderByXgrnnum(pocrnHeader.getXgrnnum());
		if(pgh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN number in this system");
			return responseHelper.getResponse();
		}
		if("Open".equalsIgnoreCase(pgh.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("GRN not confirmed");
			return responseHelper.getResponse();
		}
		pocrnHeader.setXcus(pgh.getXcus());
		if(StringUtils.isBlank(pocrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier not found from this GRN");
			return responseHelper.getResponse();
		}

		// if existing record
		if (StringUtils.isNotBlank(pocrnHeader.getXcrnnum())) {
			Pocrnheader exist = pocrnService.findPocrnHeaderByXcrnnum(pocrnHeader.getXcrnnum());
			BeanUtils.copyProperties(pocrnHeader, exist, "xcrnnum", "xgrnnum", "xtype", "xcus");
			long count = pocrnService.update(exist);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update purchase return");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Purchase return updated successfully");
			responseHelper.setRedirectUrl("/procurements/purchasereturn/" + exist.getXcrnnum());
			return responseHelper.getResponse();
		}

		// if new record
		return pocrnService.save(responseHelper, pocrnHeader, pgh);
	}

	@GetMapping("/pocrndetail/{xcrnnum}")
	public String reloadPocrnDetailTable(@PathVariable String xcrnnum, Model model) {
		model.addAttribute("pocrnDetailsList", pocrnService.findPocrnDetailByXcrnnum(xcrnnum));
		model.addAttribute("pocrnheader", pocrnService.findPocrnHeaderByXcrnnum(xcrnnum));
		return "pages/procurement/grnreturn/pocrn::pocrndetailtable";
	}

	@GetMapping("{xcrnnum}/pocrndetail/{xrow}/show")
	public String openPocrnDetailModal(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Pocrndetail pocrndetail = new Pocrndetail();
			pocrndetail.setXcrnnum(xcrnnum);
			pocrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pocrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pocrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			// pocrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("pocrndetail", pocrndetail);
		} else {
			Pocrndetail pocrndetail = pocrnService.findPocrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
			if (pocrndetail == null) {
				pocrndetail = new Pocrndetail();
				pocrndetail.setXcrnnum(xcrnnum);
				pocrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pocrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("pocrndetail", pocrndetail);
		}

		return "pages/procurement/grnreturn/pocrndetailmodal::pocrndetailmodal";
	}

	@PostMapping("/pocrndetail/save")
	public @ResponseBody Map<String, Object> savePocrndetail(Pocrndetail pocrnDetail) {
		if (pocrnDetail == null || StringUtils.isBlank(pocrnDetail.getXcrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		pocrnDetail.setXlineamt(pocrnDetail.getXqtygrn().multiply(pocrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Pocrndetail existDetail = pocrnService.findPocrnDetailByXcrnnumAndXrow(pocrnDetail.getXcrnnum(),
				pocrnDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(pocrnDetail, existDetail, "xcrnnum", "xrow");
			long count = pocrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/procurements/purchasereturn/" + pocrnDetail.getXcrnnum());
			responseHelper.setSuccessStatusAndMessage("PRN item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pocrnService.saveDetail(pocrnDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/procurements/purchasereturn/" + pocrnDetail.getXcrnnum());
		responseHelper.setSuccessStatusAndMessage("PRN item saved successfully");

		return responseHelper.getResponse();
	}

	@PostMapping("{xcrnnum}/pocrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePocrnDetail(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {
		Pocrndetail pd = pocrnService.findPocrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pocrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/procurements/purchasereturn/" + xcrnnum);
		return responseHelper.getResponse();
	}

	@GetMapping("/confirmprn/{xcrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xcrnnum) {
		if (StringUtils.isBlank(xcrnnum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// Get PocrnHeader record by Xcrnnum
		Pocrnheader pocrnHeader = pocrnService.findPocrnHeaderByXcrnnum(xcrnnum);
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pocrnHeader.getXgrnnum());
		List<Pocrndetail> pocrnHeaderList = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);

		if (!"Open".equalsIgnoreCase(pocrnHeader.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("PRN already confirmed");
			return responseHelper.getResponse();
		}
		if (pocrnHeaderList.size() == 0) {
			responseHelper.setErrorStatusAndMessage("Please add details!");
			return responseHelper.getResponse();
		}
		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(pocrnHeader.getXstatuscrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pocrnService.procConfirmCRN(xcrnnum, p_seq);
			// Error check for procConfirmCRN
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pocrnService.procIssuePricing(pogrnHeader.getXdocnum().toString(), pocrnHeader.getXwh(), p_seq);
			// Error check for procIssuePricing
			em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

		}
		if (!"Confirmed".equalsIgnoreCase(pocrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pocrnService.procTransferPRtoAP(xcrnnum, p_seq);
			// Error check for procTransferPRtoAP
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("PRN Confirmed successfully");
		responseHelper.setRedirectUrl("/procurements/purchasereturn/" + xcrnnum);
		return responseHelper.getResponse();
	}
}