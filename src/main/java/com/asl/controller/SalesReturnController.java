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

import com.asl.entity.Opcrnheader;
import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpcrnService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/salesninvoice/returnsales")
public class SalesReturnController extends ASLAbstractController {

	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private OpcrnService opcrnService;
	@Autowired
	private PocrnService pocrnService;
	@Autowired
	private PogrnService pogrnService;

	@GetMapping
	public String loadGrnReturnPage(Model model) {

		model.addAttribute("opcrnheader", getDefaultOpcrnHeader());
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.SRN_RETURN.getCode()));
		model.addAttribute("allOpcrnHeader", opcrnService.getAllOpcrnheader());
		// model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));

		return "pages/salesninvoice/salesreturn/opcrn";
	}

	@GetMapping("/{xcrnnum}")
	public String loadGrnReturnPage(@PathVariable String xcrnnum, Model model) {

		Opcrnheader data = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);
		if (data == null)
			data = getDefaultOpcrnHeader();

		model.addAttribute("opcrnheader", data);
		model.addAttribute("crnprefix", xtrnService.findByXtypetrn(TransactionCodeType.SRN_RETURN.getCode()));
		model.addAttribute("allOpcrnHeader", opcrnService.getAllOpcrnheader());
		// model.addAttribute("allPogrnHeader", new ArrayList<PogrnHeader>());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		model.addAttribute("opcrnDetailsList", opcrnService.findOpcrnDetailByXcrnnum(xcrnnum));

		return "pages/salesninvoice/salesreturn/opcrn";
	}

	private Opcrnheader getDefaultOpcrnHeader() {
		Opcrnheader opcrn = new Opcrnheader();

		opcrn.setXtype(TransactionCodeType.SRN_RETURN.getCode());
		opcrn.setXtrncrn(TransactionCodeType.SRN_RETURN.getdefaultCode());
		opcrn.setXtypetrn("CRN Number");
		// pogrn.setXtypetrn("Purchase");
		opcrn.setXgrandtot(BigDecimal.ZERO);
		opcrn.setXtotamt(BigDecimal.ZERO);
		opcrn.setXvatamt(BigDecimal.ZERO);
		opcrn.setXaitamt(BigDecimal.ZERO);
		opcrn.setXdiscamt(BigDecimal.ZERO);

		return opcrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Pocrnheader pocrnHeader, BindingResult bindingResult) {

		if ((pocrnHeader == null || StringUtils.isBlank(pocrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record

		Pocrnheader existPocrnHeader = pocrnService.findPocrnHeaderByXcrnnum(pocrnHeader.getXcrnnum());
		if (existPocrnHeader != null) {
			BeanUtils.copyProperties(pocrnHeader, existPocrnHeader, "xcrnnum", "xgrnnum", "xtype", "xdate");
			long count = pocrnService.update(existPocrnHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("PRN updated successfully");
			responseHelper.setRedirectUrl("/procurement/grnreturn/" + pocrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pocrnService.save(pocrnHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("PRN created successfully");
		responseHelper.setRedirectUrl("/procurement/grnreturn/" + pocrnHeader.getXcrnnum());
		return responseHelper.getResponse();

	}

	@GetMapping("/opcrndetail/{xcrnnum}")
	public String reloadPocrnDetailTable(@PathVariable String xcrnnum, Model model) {
		List<Pocrndetail> detailList = pocrnService.findPocrnDetailByXcrnnum(xcrnnum);
		model.addAttribute("pocrnDetailsList", detailList);
		Pocrnheader header = new Pocrnheader();
		header.setXcrnnum(xcrnnum);
		model.addAttribute("pocrnheader", header);
		return "pages/procurement/grnreturn/pocrn::pocrndetailtable";
	}

	@GetMapping("{xcrnnum}/opcrndetail/{xrow}/show")
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
				pocrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pocrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("pocrndetail", pocrndetail);
		}

		return "pages/procurement/grnreturn/pocrndetailmodal::pocrndetailmodal";
	}

	@PostMapping("/opcrndetail/save")
	public @ResponseBody Map<String, Object> savePocrndetail(Pocrndetail pocrnDetail) {

		if (pocrnDetail == null || StringUtils.isBlank(pocrnDetail.getXcrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		pocrnDetail
				.setXlineamt(pocrnDetail.getXqtygrn().multiply(pocrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

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
			responseHelper.setRedirectUrl("/procurement/grnreturn/" + pocrnDetail.getXcrnnum());
			responseHelper.setSuccessStatusAndMessage("PRN item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pocrnService.saveDetail(pocrnDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/procurement/grnreturn/" + pocrnDetail.getXcrnnum());
		responseHelper.setSuccessStatusAndMessage("PRN item saved successfully");

		return responseHelper.getResponse();
	}

	@PostMapping("{xcrnnum}/opcrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePocrnDetail(@PathVariable String xcrnnum, @PathVariable String xrow,
			Model model) {
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
		responseHelper.setRedirectUrl("/procurement/grnreturn/" + xcrnnum);
		return responseHelper.getResponse();
	}

	@GetMapping("/confirmsrn/{xcrnnum}")
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

			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pocrnService.procIssuePricing(pogrnHeader.getXdocnum().toString(), pocrnHeader.getXwh(), p_seq);
			// Error check for procIssuePricing

		}
		if (!"Confirmed".equalsIgnoreCase(pocrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pocrnService.procTransferPRtoAP(xcrnnum, p_seq);
			// Error check for procTransferPRtoAP
		}

		responseHelper.setSuccessStatusAndMessage("PRN Confirmed successfully");
		responseHelper.setRedirectUrl("/procurement/grnreturn/" + xcrnnum);
		return responseHelper.getResponse();
	}
}
