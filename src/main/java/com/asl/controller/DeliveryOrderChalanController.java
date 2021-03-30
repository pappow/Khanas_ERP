package com.asl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.SalesOrder;
import com.asl.model.report.SalesOrderChalanReport;
import com.asl.service.CacusService;
import com.asl.service.ImmofgdetailService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/deliveryorderchalan")
public class DeliveryOrderChalanController extends ASLAbstractController {

	@Autowired
	private OpordService opordService;
	@Autowired
	private OpdoService opdoService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private ImmofgdetailService immofgdetailService;
	@Autowired
	private CacusService cacusService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping
	public String loadDeliveryOrderChalanPage(Model model) {
		model.addAttribute("deliveryorderchalan", getDefaultOpdoheader());
		model.addAttribute("deliveryorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("deliveryorderchalanList", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan";
	}

	@GetMapping("/{xdornum}")
	public String loadSalesOrderChalanPage(@PathVariable String xdornum, Model model) {
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null)
			return "redirect:/salesninvoice/deliveryorderchalan";

		model.addAttribute("deliveryorderchalan", oh);
		model.addAttribute("deliveryorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("deliveryorderchalanList", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));

		List<Opdoheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		if ("Open".equalsIgnoreCase(oh.getXstatusord()))
			allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrder(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), "Open", new Date()));

		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum));

		model.addAttribute("openinvoiceorders", allOpenAndConfirmesSalesOrders);
		model.addAttribute("chalandetails", opdoService.findOpdoDetailByXdornum(xdornum));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan";
	}

	private Opdoheader getDefaultOpdoheader() {
		Opdoheader oh = new Opdoheader();
		oh.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		oh.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		oh.setXtypetrn(TransactionCodeType.CHALAN_NUMBER.getCode());
		oh.setXdate(new Date());
		oh.setXstatusord("Open");
		return oh;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opdoheader opdoheader, BindingResult bindingResult, Model model) {
		if (opdoheader == null || StringUtils.isBlank(opdoheader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Opdoheader existOh = opdoService.findOpdoHeaderByXdornum(opdoheader.getXdornum());
		if (existOh != null) {
			BeanUtils.copyProperties(opdoheader, existOh, "xdate", "xstatusord");
			long count = opdoService.update(existOh);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Chalan not updated");
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan/" + existOh.getXdornum());
			responseHelper.setSuccessStatusAndMessage("Chalan updated successfully");
			return responseHelper.getResponse();
		}

		// if new
		opdoheader.setXstatusord("Open");
		opdoheader.setXdate(new Date());
		long count = opdoService.save(opdoheader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Chalan not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan");
		responseHelper.setSuccessStatusAndMessage("Chalan created successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/opendeliveryorder/query")
	public String reloadTableWithData(@RequestParam String xdornum, @RequestParam String date, Model model)
			throws ParseException {
		model.addAttribute("deliveryorderchalan", opdoService.findOpdoHeaderByXdornum(xdornum));
		List<Opdoheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();

		allOpenAndConfirmesSalesOrders
				.addAll(opdoService.findAllInvoiceOrder(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),
						TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), "Open", new Date()));

		allOpenAndConfirmesSalesOrders
				.addAll(opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),
						TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum));
		model.addAttribute("openinvoiceorders", allOpenAndConfirmesSalesOrders);
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan::opendeliveryorderstable";
	}

	@PostMapping("/opendeliveryorder/query")
	public @ResponseBody Map<String, Object> queryForrequistionDetails(String xdornum, Date xdate, Model model) {
		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable",
				"/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum=" + xdornum + "&date="
						+ sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@GetMapping("/ordreqdetails/{xdornum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String xdornum, Model model) {
		model.addAttribute("opdodetailsList", opdoService.findOpdoDetailByXdornum(xdornum));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderdetailmodal::deliveryorderdetailmodal";
	}

	@PostMapping("/deliveryorderconfirm/{chalan}/{xdornum}")
	public @ResponseBody Map<String, Object> confirmSalesOrderAndCreateChalanDetail(@PathVariable String chalan,
			@PathVariable String xdornum, Model model) {

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if (StringUtils.isNotBlank(oh.getXdocnum())) {
			responseHelper.setErrorStatusAndMessage(
					"Sales order already added to chalan : " + oh.getXdocnum() + " . Please reload this page again");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if (details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xdornum + " Sales Order has no item to add this chalan");
			return responseHelper.getResponse();
		}

		// create or update chalan detail first
		for (Opdodetail pd : details) {
			// check chalan detail already exist using item
			Opdodetail existChalanDetail = opdoService.findOpdoDetailByXdornumAndXitem(chalan, pd.getXitem());
			if (existChalanDetail != null) { // update existing with qty
				existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().add(pd.getXqtyord()));
				long countChalanDetail = opdoService.updateDetail(existChalanDetail);
				if (countChalanDetail == 0) {
					responseHelper.setErrorStatusAndMessage("Can't update chalan detail");
					return responseHelper.getResponse();
				}
			} else { // create new detail
				Opdodetail opdodetail = new Opdodetail();
				opdodetail.setXdornum(chalan);
				opdodetail.setXitem(pd.getXitem());
				opdodetail.setXunitsel(pd.getXunitsel());
				opdodetail.setXqtyord(pd.getXqtyord());
				opdodetail.setXrate(pd.getXrate());
				long countChalanDetail = opdoService.saveDetail(opdodetail);
				if (countChalanDetail == 0) {
					responseHelper.setErrorStatusAndMessage("Can't create chalan detail");
					return responseHelper.getResponse();
				}
			}
		}

		// now update sales order with chalan reference
		oh.setXdocnum(chalan);
		oh.setXchalancreated(true);
		//oh.setXstatusord("Confirmed");
		long count = opdoService.update(oh);

		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable",
				"/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum=" + chalan + "&date="
						+ sdf.format(oh.getXdate()));
		responseHelper.setSecondReloadSectionIdWithUrl("deliveryorderchalandetailtable",
				"/salesninvoice/deliveryorderchalan/chalandetail/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order confirmed");
		return responseHelper.getResponse();
	}

	@PostMapping("/deliveryorderrevoke/{chalan}/{xdornum}")
	public @ResponseBody Map<String, Object> revokeSalesOrder(@PathVariable String chalan, @PathVariable String xdornum,
			Model model) {
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if (details == null || details.isEmpty()) {
			responseHelper
					.setErrorStatusAndMessage("This " + xdornum + " Sales Order has no item to remove from chalan");
			return responseHelper.getResponse();
		}

		// create or update chalan detail first
		for (Opdodetail pd : details) {
			// check chalan detail already exist using item
			Opdodetail existChalanDetail = opdoService.findOpdoDetailByXdornumAndXitem(chalan, pd.getXitem());
			if (existChalanDetail == null)
				continue;

			// update existing with qty
			existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().subtract(pd.getXqtyord()));
			long countChalanDetail = 0;
			if (BigDecimal.ZERO.equals(existChalanDetail.getXqtyord())) {
				countChalanDetail = opdoService.deleteDetail(existChalanDetail);
			} else {
				countChalanDetail = opdoService.updateDetail(existChalanDetail);
			}
			if (countChalanDetail == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update chalan detail");
				return responseHelper.getResponse();
			}
		}

		// now update sales order with chalan reference
		oh.setXdocnum(null);
		oh.setXchalancreated(false);
		oh.setXstatusord("Open");
		long count = opdoService.update(oh);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable",
				"/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum=" + chalan + "&date="
						+ sdf.format(oh.getXdate()));
		responseHelper.setSecondReloadSectionIdWithUrl("deliveryorderchalandetailtable",
				"/salesninvoice/deliveryorderchalan/chalandetail/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order revoked");
		return responseHelper.getResponse();
	}

	@GetMapping("/chalandetail/{xdornum}")
	public String reloadChalanDetailSection(@PathVariable String xdornum, Model model) {
		model.addAttribute("deliveryorderchalan", opdoService.findOpdoHeaderByXdornum(xdornum));
		model.addAttribute("chalandetails", opdoService.findOpdoDetailByXdornum(xdornum));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan::deliveryorderchalandetailtable";
	}

	@PostMapping("/lockchalan/{xdornum}")
	public @ResponseBody Map<String, Object> lockChalan(@PathVariable String xdornum, Model model) {

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// transfer all chalan deails to immofgdetail
		/*
		List<Opdodetail> chalandetails = opdoService.findOpdoDetailByXdornum(oh.getXdornum());
		for (Opdodetail c : chalandetails) {
			
			  Immofgdetail id = new Immofgdetail(); id.setXtornum(c.getXordernum());
			  id.setXrow(c.getXrow()); id.setXunit(c.getXunit());
			  id.setXitem(c.getXitem()); id.setXqtyord(c.getXqtyord());
			  id.setXnote(c.getXlong()); long count = immofgdetailService.save(id);
			  if(count == 0) { log.error("ERROR is : {}",
			  "Can't insert chaland details to Immofgdetail table for chalan " +
			  c.getXordernum()); }
			 
		}*/
		
		List<Opdoheader> invoiceList = opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum);

		// now lock chalan
		oh.setXstatusord("Confirmed");
		long count = opdoService.update(oh);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't lock Chalan");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Chalan locked successfully");
		responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan/" + xdornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/print/chalan/{pType}/{xdornum}")
	public ResponseEntity<byte[]> printChalanWithSalesOrderDetails(@PathVariable String pType,
			@PathVariable String xdornum) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if (oh == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Opdoheader> salesOrders = opdoService.findAllInvoiceOrderByChalan(
				TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(),
				TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), oh.getXdornum());
		if (salesOrders == null || salesOrders.isEmpty()) {
			message = "No sales order found in this chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<SalesOrderChalanReport> allReports = new ArrayList<>();
		Cacus cacus = new Cacus();

		for (Opdoheader so : salesOrders) {
			
			SalesOrderChalanReport report = new SalesOrderChalanReport();
			report.setBusinessName(sessionManager.getZbusiness().getZorg());
			report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
			report.setReportName("Delivery Chalan");
			report.setFromDate(SDF.format(oh.getXdate()));
			report.setToDate(SDF.format(oh.getXdate()));
			report.setPrintDate(SDF.format(new Date()));
			report.setCopyrightText("ASL");

			report.setChalanNumber(oh.getXdornum());
			report.setChalanDate(SDF.format(oh.getXdate()));
			report.setChalanStatus(oh.getXstatusar());

			SalesOrder salesOrder = new SalesOrder();
			cacus = cacusService.findByXcus(so.getXcus());
			salesOrder.setOrderNumber(so.getXdornum());
			salesOrder.setReqBranch(so.getXcus());
			salesOrder.setCustomer(cacus.getXorg());
			salesOrder.setCustomerAddress(cacus.getXmadd());
			salesOrder.setDate(SDF.format(so.getXdate()));
			if ("invoices".equalsIgnoreCase(pType)) {
				report.setReportName("Sales Invoice");

				salesOrder.setTotalAmount(so.getXtotamt().toString());
				salesOrder.setVatAmount(so.getXvatamt().toString());
				salesOrder.setDiscountAmount(so.getXdiscamt().toString());
				salesOrder.setGrandTotalAmount(so.getXgrandtot().toString());
			}

			List<Opdodetail> items = opdoService.findOpdoDetailByXdornum(so.getXdornum());
			if (items != null && !items.isEmpty()) {
				items.parallelStream().forEach(it -> {
					ItemDetails item = new ItemDetails();
					item.setItemCode(it.getXitem());
					item.setItemName(it.getXdesc());
					item.setItemQty(it.getXqtyord().toString());
					item.setItemUnit(it.getXunitsel());
					item.setItemCategory(it.getXcatitem());
					item.setItemGroup(it.getXgitem());

					if ("invoices".equalsIgnoreCase(pType)) {
						item.setItemRate(it.getXrate().toString());
						item.setItemTotalAmount(it.getXlineamt().toString());
					}

					salesOrder.getItems().add(item);
				});
			}

			report.getSalesorders().add(salesOrder);

			allReports.add(report);

		}
		byte[] byt;
		if ("invoices".equalsIgnoreCase(pType))
			byt = getBatchPDFByte(allReports, "deliverychalanreport.xsl");
		else
			byt = getBatchPDFByte(allReports, "deliverychalaninvoicesreport.xsl");
		if (byt == null) {
			message = "Can't print report for chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@GetMapping("/print/order/{pType}/{xdornum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String pType,
			@PathVariable String xdornum) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

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

		/*
		 * List<Opdoheader> salesOrders =
		 * opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.
		 * SALES_AND_INVOICE_NUMBER.getCode(),
		 * TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(),
		 * oh.getXdornum()); if(salesOrders == null || salesOrders.isEmpty()) { message
		 * = "No sales order found in this chalan : " + xdornum; return new
		 * ResponseEntity<>(message.getBytes(), headers,
		 * HttpStatus.INTERNAL_SERVER_ERROR); }
		 */

		// List<SalesOrderChalanReport> allReports = new ArrayList<>();
		SalesOrderChalanReport orderReport = new SalesOrderChalanReport();

		// for(Opdoheader so : salesOrders) {

		SalesOrderChalanReport report = new SalesOrderChalanReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Delivery Chalan");
		report.setFromDate(SDF.format(chalan.getXdate()));
		report.setToDate(SDF.format(chalan.getXdate()));
		report.setPrintDate(SDF.format(new Date()));
		report.setCopyrightText("ASL");

		report.setChalanNumber(chalan.getXdornum());
		report.setChalanDate(SDF.format(chalan.getXdate()));
		report.setChalanStatus(chalan.getXstatusar());

		SalesOrder salesOrder = new SalesOrder();
		salesOrder.setOrderNumber(oh.getXdornum());
		salesOrder.setReqBranch(oh.getXcus());
		salesOrder.setCustomer(cacus.getXorg());
		salesOrder.setCustomerAddress(cacus.getXmadd());
		salesOrder.setDate(SDF.format(oh.getXdate()));
		if ("invoice".equalsIgnoreCase(pType)) {
			report.setReportName("Sales Invoice");

			salesOrder.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setVatAmount(oh.getXvatamt() != null ? oh.getXvatamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setDiscountAmount(oh.getXdiscamt() != null ? oh.getXdiscamt().toString() : BigDecimal.ZERO.toString());
			salesOrder.setGrandTotalAmount(oh.getXgrandtot() != null ? oh.getXgrandtot().toString() : BigDecimal.ZERO.toString());
		}

		List<Opdodetail> items = opdoService.findOpdoDetailByXdornum(oh.getXdornum());
		if (items != null && !items.isEmpty()) {
			items.parallelStream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXdesc());
				item.setItemQty(it.getXqtyord().toString());
				item.setItemUnit(it.getXunitsel());
				item.setItemCategory(it.getXcatitem());
				item.setItemGroup(it.getXgitem());

				if ("invoice".equalsIgnoreCase(pType)) {
					item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : BigDecimal.ZERO.toString());
					item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				}

				salesOrder.getItems().add(item);
			});
		}

		report.getSalesorders().add(salesOrder);

		byte[] byt;
		if ("invoice".equalsIgnoreCase(pType))
			byt = getPDFByte(report, "deliverychalaninvoicesreport.xsl");
		else
			byt = getPDFByte(report, "deliverychalanreport.xsl");
		if (byt == null) {
			message = "Can't print report for chalan : " + xdornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}
