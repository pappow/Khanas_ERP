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

import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
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
		//model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
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
		//model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode()));
		model.addAttribute("paymenttypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvstatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		model.addAttribute("opcrnDetailsList", opcrnService.findOpcrnDetailByXcrnnum(xcrnnum));

		return "pages/salesninvoice/salesreturn/opcrn";
	}

	private Opcrnheader getDefaultOpcrnHeader() {
		Opcrnheader opcrn = new Opcrnheader();

		opcrn.setXtype(TransactionCodeType.SRN_RETURN.getCode());
		opcrn.setXtrncrn(TransactionCodeType.SRN_RETURN.getCode());
		opcrn.setXtrn(TransactionCodeType.SRN_RETURN.getdefaultCode());
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
	public @ResponseBody Map<String, Object> save(Opcrnheader opcrnHeader, BindingResult bindingResult) {

		if ((opcrnHeader == null || StringUtils.isBlank(opcrnHeader.getXtrn()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		opcrnHeader.setXtrncrn(TransactionCodeType.SRN_RETURN.getCode());
		Opcrnheader existOpcrnHeader = opcrnService.findOpcrnHeaderByXcrnnum(opcrnHeader.getXcrnnum());
		if (existOpcrnHeader != null) {
			BeanUtils.copyProperties(opcrnHeader, existOpcrnHeader, "xcrnnum", "xtypetrn", "xtrncrn", "xdate");
			long count = opcrnService.update(existOpcrnHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("SRN updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/returnsales/" + opcrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = opcrnService.save(opcrnHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("SRN created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/returnsales/" + opcrnHeader.getXcrnnum());
		return responseHelper.getResponse();

	}

	@GetMapping("/opcrndetail/{xcrnnum}")
	public String reloadOpcrnDetailTable(@PathVariable String xcrnnum, Model model) {
		List<Opcrndetail> detailList = opcrnService.findOpcrnDetailByXcrnnum(xcrnnum);
		model.addAttribute("opcrnDetailsList", detailList);
		Opcrnheader header = new Opcrnheader();
		header.setXcrnnum(xcrnnum);
		model.addAttribute("opcrnheader", header);
		return "pages/salesninvoice/salesreturn/opcrn::opcrndetailtable";
	}

	@GetMapping("{xcrnnum}/opcrndetail/{xrow}/show")
	public String openOpcrnDetailModal(@PathVariable String xcrnnum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Opcrndetail opcrndetail = new Opcrndetail();
			opcrndetail.setXcrnnum(xcrnnum);
			opcrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			opcrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			// pocrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opcrndetail", opcrndetail);
		} else {
			Opcrndetail opcrndetail = opcrnService.findOpcrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
			if (opcrndetail == null) {
				opcrndetail = new Opcrndetail();
				opcrndetail.setXcrnnum(xcrnnum);
				opcrndetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				//opcrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				opcrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("opcrndetail", opcrndetail);
		}

		return "pages/salesninvoice/salesreturn/opcrndetailmodal::opcrndetailmodal";
	}

	@PostMapping("/opcrndetail/save")
	public @ResponseBody Map<String, Object> saveOpcrndetail(Opcrndetail opcrnDetail) {

		if (opcrnDetail == null || StringUtils.isBlank(opcrnDetail.getXcrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		//opcrnDetail.setXlineamt(opcrnDetail.getXqtygrn().multiply(pocrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Opcrndetail existDetail = opcrnService.findOpcrnDetailByXcrnnumAndXrow(opcrnDetail.getXcrnnum(), opcrnDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opcrnDetail, existDetail, "xcrnnum", "xrow");
			long count = opcrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/salesninvoice/returnsales/" + opcrnDetail.getXcrnnum());
			responseHelper.setSuccessStatusAndMessage("SRN item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opcrnService.saveDetail(opcrnDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/salesninvoice/returnsales/" + opcrnDetail.getXcrnnum());
		responseHelper.setSuccessStatusAndMessage("SRN item saved successfully");

		return responseHelper.getResponse();
	}

	@PostMapping("{xcrnnum}/opcrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpcrnDetail(@PathVariable String xcrnnum, @PathVariable String xrow,
			Model model) {
		Opcrndetail od = opcrnService.findOpcrnDetailByXcrnnumAndXrow(xcrnnum, Integer.parseInt(xrow));
		if (od == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = opcrnService.deleteDetail(od);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/returnsales/" + xcrnnum);
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
		Opcrnheader opcrnHeader = opcrnService.findOpcrnHeaderByXcrnnum(xcrnnum);
		List<Opcrndetail> opcrnHeaderList = opcrnService.findOpcrnDetailByXcrnnum(xcrnnum);

		if(StringUtils.isBlank(opcrnHeader.getXdornum())) {
			responseHelper.setErrorStatusAndMessage("Please select a invoice no.!");
			return responseHelper.getResponse();
		}
		if (!"Open".equalsIgnoreCase(opcrnHeader.getXstatuscrn())) {
			responseHelper.setErrorStatusAndMessage("SRN already confirmed");
			return responseHelper.getResponse();
		}
		if (opcrnHeaderList.size() == 0) {
			responseHelper.setErrorStatusAndMessage("Please add details!");
			return responseHelper.getResponse();
		}
		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(opcrnHeader.getXstatuscrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opcrnService.procConfirmCRN(xcrnnum, p_seq);
			// Error check for procConfirmCRN
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}
		if (!"Confirmed".equalsIgnoreCase(opcrnHeader.getXstatusar())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opcrnService.procTransferOPtoAR(opcrnHeader.getXdornum(), "opdoheader", p_seq);
			// Error check for procTransferPRtoAP
			String em = getProcedureErrorMessages(p_seq);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("SRN Confirmed successfully");
		responseHelper.setRedirectUrl("/salesninvoice/returnsales/" + xcrnnum);
		return responseHelper.getResponse();
	}
}
