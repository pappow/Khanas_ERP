package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.Vatait;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrder;
import com.asl.model.report.SalesOrderChalanReport;
import com.asl.service.CacusService;
import com.asl.service.OpcrnService;
import com.asl.service.OpdoService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/salesninvoice/salesandinvoice")
public class SalesAndInvoiceController extends ASLAbstractController {

	@Autowired private OpdoService opdoService;
	@Autowired private OpcrnService opcrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private VataitService vataitService;
	@Autowired private CacusService cacusService;

	@GetMapping
	public String loadInvoicePage(Model model) {

		model.addAttribute("opdoheader", getDefaultOpdoHeader());
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));

		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		if(isBoshila()) {
			return "pages/land/salesninvoice/opdo";
		}
		return "pages/salesninvoice/salesandinvoice/opdo";
	}

	@GetMapping("/{xdornum}")
	public String loadInvoicePage(@PathVariable String xdornum, Model model) {
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (data == null) return "redirect:/salesninvoice/salesandinvoice";

		model.addAttribute("opdoheader", data);
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));

		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));

		model.addAttribute("opdoDetailsList", opdoService.findOpdoDetailByXdornum(xdornum));
		if(isBoshila()) {
			return "pages/land/salesninvoice/opdo";
		}
		return "pages/salesninvoice/salesandinvoice/opdo";
	}

	private Opdoheader getDefaultOpdoHeader() {
		Opdoheader opdoheader = new Opdoheader();
		opdoheader.setXtype(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		opdoheader.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		opdoheader.setXtrnopdo(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		opdoheader.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		opdoheader.setXstatusord("Open");
		opdoheader.setXstatusar("Open");
		opdoheader.setXstatusjv("Open");
		opdoheader.setXtotamt(BigDecimal.ZERO);
		opdoheader.setXgrandtot(BigDecimal.ZERO);
		opdoheader.setXvatait("No Vat");
		opdoheader.setXvatamt(BigDecimal.ZERO);
		opdoheader.setXait(BigDecimal.ZERO);
		opdoheader.setXdiscamt(BigDecimal.ZERO);
		opdoheader.setXpaid(BigDecimal.ZERO);
		opdoheader.setXchange(BigDecimal.ZERO);
		opdoheader.setXpaystatus("Due");
		opdoheader.setXwh("Central Store");
		return opdoheader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opdoheader opdoHeader, BindingResult bindingResult) {
		if (opdoHeader == null || StringUtils.isBlank(opdoHeader.getXtype())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if (StringUtils.isBlank(opdoHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please select a customer to create invoice");
			return responseHelper.getResponse();
		}

		Vatait vatait = vataitService.findVataitByXvatait(opdoHeader.getXvatait());
		if(opdoHeader.getXtotamt() == null) opdoHeader.setXtotamt(BigDecimal.ZERO);
		if(StringUtils.isNotBlank(opdoHeader.getXvatait()) && !"No Vat".equalsIgnoreCase(opdoHeader.getXvatait()) && vatait != null) {
			if(opdoHeader.getXvatamt() == null) opdoHeader.setXvatamt((opdoHeader.getXtotamt().multiply(vatait.getXvat())).divide(BigDecimal.valueOf(100)));
			if(opdoHeader.getXait() == null) opdoHeader.setXait((opdoHeader.getXtotamt().multiply(vatait.getXait())).divide(BigDecimal.valueOf(100)));
		} else {
			if(opdoHeader.getXvatamt() == null) opdoHeader.setXvatamt(BigDecimal.ZERO);
			if(opdoHeader.getXait() == null) opdoHeader.setXait(BigDecimal.ZERO);
		}
		if(opdoHeader.getXdiscamt() == null) opdoHeader.setXdiscamt(BigDecimal.ZERO);
		if(opdoHeader.getXpaid() == null) opdoHeader.setXpaid(BigDecimal.ZERO);
		if(opdoHeader.getXchange() == null) opdoHeader.setXchange(BigDecimal.ZERO);
		BigDecimal grandTotal = (opdoHeader.getXtotamt().add(opdoHeader.getXvatamt()).add(opdoHeader.getXait())).subtract(opdoHeader.getXdiscamt());
		opdoHeader.setXgrandtot(grandTotal);

		if (opdoHeader.getXgrandtot().compareTo(opdoHeader.getXpaid()) == -1) {
			opdoHeader.setXchange(opdoHeader.getXpaid().subtract(opdoHeader.getXgrandtot()));
		} else {
			opdoHeader.setXchange(BigDecimal.ZERO);
		}

		// if existing record
		Opdoheader existOpdoHeader = opdoService.findOpdoHeaderByXdornum(opdoHeader.getXdornum());
		if (existOpdoHeader != null) {
			BeanUtils.copyProperties(opdoHeader, existOpdoHeader, "xdornum", "xtype", "xtypetrn", "xdate", "xtrnopdo", "xtrn");
			long count = opdoService.update(existOpdoHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Invoice updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + opdoHeader.getXdornum());
			return responseHelper.getResponse();
		}

		// If new
		opdoHeader.setXstatusar("Open");
		opdoHeader.setXstatusjv("Open");
		long count = opdoService.save(opdoHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + opdoHeader.getXdornum());
		return responseHelper.getResponse();
	}

	@GetMapping("/opdodetail/{xdornum}")
	public String reloadOpdoDetailTable(@PathVariable String xdornum, Model model) {
		model.addAttribute("opdoDetailsList", opdoService.findOpdoDetailByXdornum(xdornum));
		model.addAttribute("opdoheader", opdoService.findOpdoHeaderByXdornum(xdornum));
		return "pages/salesninvoice/salesandinvoice/opdo::opdodetailtable";
	}

	@GetMapping("/opdoheaderform/{xdornum}")
	public String reloadOpdoheaderform(@PathVariable String xdornum, Model model) {
		model.addAttribute("opdoheader", opdoService.findOpdoHeaderByXdornum(xdornum));
		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode(), Boolean.TRUE));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("payStatusList", xcodeService.findByXtype(CodeType.PAYMENT_MODE.getCode(), Boolean.TRUE));
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode(), Boolean.TRUE));
		return "pages/salesninvoice/salesandinvoice/opdo::opdoheaderform";
	}

	@GetMapping("/{xdornum}/opdodetail/{xrow}/show")
	public String openOpdoDetailModal(@PathVariable String xdornum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Opdodetail opdodetail = new Opdodetail();
			opdodetail.setXdornum(xdornum);
			opdodetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			opdodetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			opdodetail.setXqtycrn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opdodetail", opdodetail);
		} else {
			Opdodetail opdodetail = opdoService.findOpdoDetailByXdornumAndXrow(xdornum, Integer.parseInt(xrow));
			if (opdodetail == null) {
				opdodetail = new Opdodetail();
				opdodetail.setXdornum(xdornum);
				opdodetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				opdodetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				opdodetail.setXqtycrn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("opdodetail", opdodetail);
		}
		return "pages/salesninvoice/salesandinvoice/opdodetailmodal::opdodetailmodal";
	}

	@PostMapping("/opdodetail/save")
	public @ResponseBody Map<String, Object> saveOpdodetail(Opdodetail opdoDetail) {
		if (opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(opdoDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item not selected! Please select an item");
			return responseHelper.getResponse();
		}
		if(opdoDetail.getXqtyord() == null || opdoDetail.getXqtyord().compareTo(BigDecimal.ONE) == -1) {
			responseHelper.setErrorStatusAndMessage("Item quantity can't be less then one");
			return responseHelper.getResponse();
		}
		if(opdoDetail.getXqtyord() == null || opdoDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Item Rate can't be negative");
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if (opdoDetail.getXrow() == 0 && opdoService.findOpdoDetailByXdornumAndXitem(opdoDetail.getXdornum(), opdoDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// modify line amount
		opdoDetail.setXlineamt(opdoDetail.getXqtyord().multiply(opdoDetail.getXrate()).setScale(2, RoundingMode.DOWN));

		// if existing
		Opdodetail existDetail = opdoService.findOpdoDetailByXdornumAndXrow(opdoDetail.getXdornum(), opdoDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opdoDetail, existDetail, "xdornum", "xrow");
			long count = opdoService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/salesandinvoice/opdodetail/" + opdoDetail.getXdornum());
			responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/salesandinvoice/opdoheaderform/" + opdoDetail.getXdornum());
			responseHelper.setSuccessStatusAndMessage("Invoice item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opdoService.saveDetail(opdoDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/salesandinvoice/opdodetail/" + opdoDetail.getXdornum());
		responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/salesandinvoice/opdoheaderform/" + opdoDetail.getXdornum());
		responseHelper.setSuccessStatusAndMessage("Invoice item saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("{xdornum}/opdodetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpdoDetail(@PathVariable String xdornum, @PathVariable String xrow, Model model) {
		Opdodetail pd = opdoService.findOpdoDetailByXdornumAndXrow(xdornum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setErrorStatusAndMessage("Detail item can't found to do delete");
			return responseHelper.getResponse();
		}

		long count = opdoService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("opdodetailtable", "/salesninvoice/salesandinvoice/opdodetail/" + xdornum);
		responseHelper.setSecondReloadSectionIdWithUrl("opdoheaderform", "/salesninvoice/salesandinvoice/opdoheaderform/" + xdornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/confirmopdo/{xdornum}")
	public @ResponseBody Map<String, Object> confirmOpdo(@PathVariable String xdornum) {
		if (StringUtils.isBlank(xdornum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		Opdoheader opdoHeader = opdoService.findOpdoHeaderByXdornum(xdornum);
		List<Opdodetail> opdoDetailList = opdoService.findOpdoDetailByXdornum(xdornum);
		Integer grandTot = ((opdoHeader.getXtotamt().subtract(opdoHeader.getXdiscamt())).add(opdoHeader.getXvatamt()))
				.intValue();

		if (opdoDetailList.size() == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if (!"Other".equalsIgnoreCase(opdoHeader.getXpaymenttype())) {
			Integer paid = ((opdoHeader.getXtotamt().subtract(opdoHeader.getXdiscamt())).add(opdoHeader.getXvatamt()))
					.intValue();
			opdoHeader.setXpaid(BigDecimal.valueOf(paid));
		}
		Integer xpaid99 = opdoHeader.getXpaid().add(BigDecimal.valueOf(0.99)).intValue();

		if (grandTot > xpaid99 && !"Other".equalsIgnoreCase(opdoHeader.getXpaymenttype())) {
			responseHelper.setErrorStatusAndMessage("Paid amount not to be less than Receivable!");
			return responseHelper.getResponse();
		}

		// Opdoheader opdoHeader = opdoService.findOpdoHeaderByXdornum(xdornum);
		// PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);

		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusord())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procConfirmDO(xdornum, p_seq);
			// Error check here for procConfrimDo
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procIssuePricing(opdoHeader.getXdocnum(), opdoHeader.getXwh(), p_seq);
			// Error check here for procIssuePricing
			em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

		}
		if (!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusar())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(),
					TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procTransferOPtoAR(xdornum, "opdoheader", p_seq);
			// Error check here for procTransferOPtoAR
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}

		}

		responseHelper.setSuccessStatusAndMessage("Invoice Confirmed successfully");

		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + xdornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/returnsales/{xdornum}")
	public @ResponseBody Map<String, Object> retrunsales(@PathVariable String xdornum) {
		if (StringUtils.isBlank(xdornum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// Get OpdoHeader record by xdornum
		Opdoheader opdoheader = opdoService.findOpdoHeaderByXdornum(xdornum);

		if (opdoheader != null) {
			Opcrnheader opcrnheader = new Opcrnheader();
			BeanUtils.copyProperties(opdoheader, opcrnheader, "xdate", "xtype", "xtypetrn", "xtrn");
			opcrnheader.setXdornum(opdoheader.getXdornum());
			opcrnheader.setXstatuscrn("Open");
			opcrnheader.setXdate(new Date());
			opcrnheader.setXcus(opdoheader.getXcus());
			opcrnheader.setXtype(TransactionCodeType.SRN_RETURN.getCode());
			opcrnheader.setXtypetrn("CRN Number");
			opcrnheader.setXtrncrn(TransactionCodeType.SRN_RETURN.getCode());
			opcrnheader.setXtrn(TransactionCodeType.SRN_RETURN.getdefaultCode());

			long count = opcrnService.save(opcrnheader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			opcrnheader = opcrnService.findOpcrnHeaderByXdornum(xdornum);

			// Get Sales Order details to Copy them in CRN
			List<Opdodetail> opdoDetailList = opdoService.findOpdoDetailByXdornum(xdornum);
			Opcrndetail opcrnDetail;

			for (int i = 0; i < opdoDetailList.size(); i++) {
				opcrnDetail = new Opcrndetail();
				BeanUtils.copyProperties(opdoDetailList.get(i), opcrnDetail, "xrow", "xnote");
				opcrnDetail.setXcrnnum(opcrnheader.getXcrnnum());
				opcrnDetail.setXunit(opdoDetailList.get(i).getXunitsel());
				// opcrnDetail.setXqtyord(opdoDetailList.get(i).getXqtyord());

				long nCount = opcrnService.saveDetail(opcrnDetail);

				// Update Inventory
				if (nCount == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}
			}

			// Update PoordHeader Status
			/*
			 * opdoheader.setXstatusord("Returned"); long pCount =
			 * opdoService.update(opdoheader); if(pCount == 0) {
			 * responseHelper.setStatus(ResponseStatus.ERROR); return
			 * responseHelper.getResponse(); }
			 */

			responseHelper.setSuccessStatusAndMessage("Sales Returned successfully");
			responseHelper.setRedirectUrl("/salesninvoice/salesreturn/" + opcrnheader.getXcrnnum());
			return responseHelper.getResponse();
		}
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{pType}/{xdornum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String pType,
			@PathVariable String xdornum) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);

		if (oh == null) {
			message = "Invoice not found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Cacus cacus = cacusService.findByXcus(oh.getXcus());
		Opdoheader chalan = opdoService.findOpdoHeaderByXdornum(oh.getXdocnum());
		if (chalan == null) {
			message = "Invoice is not assigned to a chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// SalesOrderChalanReport orderReport = new SalesOrderChalanReport();

		// for(Opdoheader so : salesOrders) {

		SalesOrderChalanReport report = new SalesOrderChalanReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Delivery Chalan");
		report.setFromDate(sdf.format(chalan.getXdate()));
		report.setToDate(sdf.format(chalan.getXdate()));
		report.setPrintDate(sdf.format(new Date()));
		report.setCopyrightText("Copyright © 2021 - ASL");

		report.setChalanNumber(chalan.getXdornum());
		report.setChalanDate(sdf.format(chalan.getXdate()));
		report.setChalanStatus(chalan.getXstatusar());

		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setOrderNumber(oh.getXdornum());
		salesOrder.setReqBranch(oh.getXcus());
		salesOrder.setCustomer(cacus.getXorg());
		salesOrder.setCustomerAddress(cacus.getXmadd());
		salesOrder.setDate(sdf.format(oh.getXdate()));
		if ("invoice".equalsIgnoreCase(pType)) {
			report.setReportName("Invoice Order");

			salesOrder
					.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setVatAmount(oh.getXvatamt() != null ? oh.getXvatamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setDiscountAmount(
					oh.getXdiscamt() != null ? oh.getXdiscamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setGrandTotalAmount(
					oh.getXgrandtot() != null ? oh.getXgrandtot().toString() : BigDecimal.ZERO.toString());
		}

		List<Opdodetail> items = opdoService.findOpdoDetailByXdornum(oh.getXdornum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXdesc());
				item.setItemQty(it.getXqtyord().toString());
				item.setItemUnit(it.getXunitsel());
				item.setItemCategory(it.getXcatitem());
				item.setItemGroup(it.getXgitem());

				if ("invoice".equalsIgnoreCase(pType)) {
					item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : BigDecimal.ZERO.toString());
					item.setItemTotalAmount(
							it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				}

				salesOrder.getItems().add(item);
			});
		}

		report.getSalesorders().add(salesOrder);

		byte[] byt;
		if ("invoice".equalsIgnoreCase(pType))
			byt = getPDFByte(report, "invoicereport.xsl");
		else
			byt = getPDFByte(report, "deliveryorderreport.xsl");
		if (byt == null) {
			message = "Can't print report for chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}
