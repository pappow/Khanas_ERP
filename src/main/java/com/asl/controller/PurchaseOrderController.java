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
import com.asl.entity.Caitem;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.ItemDetails;
import com.asl.model.report.PurchaseOrder;
import com.asl.model.report.PurchaseReport;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/procurements/poord")
public class PurchaseOrderController extends ASLAbstractController {

	@Autowired private PoordService poordService;
	@Autowired private CacusService cacusService;
	@Autowired private CaitemService caitemService;
	@Autowired private PogrnService pogrnService;

	@GetMapping
	public String loadPoordPage(Model model) {
		model.addAttribute("poordheader", getDefaultPoordHeader());
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		if(isBoshila()) {
			return "pages/land/purchasing/poord";
		}
		return "pages/purchasing/poord/poord";
	}

	@GetMapping("/{xpornum}")
	public String loadPoordPage(@PathVariable String xpornum, Model model) {
		PoordHeader data = poordService.findPoordHeaderByXpornum(xpornum);
		if(data == null) data = getDefaultPoordHeader();
		if(data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("poordheader", data);
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("poorddetailsList", poordService.findPoorddetailByXpornum(xpornum));

		model.addAttribute("grnlist", pogrnService.findPogrnHeaderByXpornum(xpornum));

		if(isBoshila()) {
			return "pages/land/purchasing/poord";
		}
		return "pages/purchasing/poord/poord";
	}

	@GetMapping("/clear")
	public String clearPoordForm(Model model) {
		model.addAttribute("poordheader", getDefaultPoordHeader());
		model.addAttribute("allPoordHeader", poordService.getPoordHeadersByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		model.addAttribute("prefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode()));
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		if(isBoshila()) {
			return "pages/land/purchasing/poord::poordheaderform";
		}
		return "pages/purchasing/poord/poord::poordheaderform";
	}

	private PoordHeader getDefaultPoordHeader() {
		PoordHeader poord = new PoordHeader();
		poord.setXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode());
		poord.setXstatuspor("Open");
		poord.setXtotamt(BigDecimal.ZERO);
		return poord;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PoordHeader poordHeader, BindingResult bindingResult){
		// Validate
		if(StringUtils.isBlank(poordHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}

		// if existing record
		if(StringUtils.isNotBlank(poordHeader.getXpornum())) {
			PoordHeader exist = poordService.findPoordHeaderByXpornum(poordHeader.getXpornum());

			BeanUtils.copyProperties(poordHeader, exist, "xpornum", "xtype", "xtypetrn", "xtrn", "xtotamt");
			long count = poordService.update(exist);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Purchase Order updated successfully");
			responseHelper.setRedirectUrl("/procurements/poord/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = poordService.save(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase Order created successfully");
		responseHelper.setRedirectUrl("/procurements/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xpornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, true);
	}

	private Map<String, Object> doArchiveOrRestore(String xpornum, boolean archive){
		// todo: need to delete actually from db
		
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find purchase order " + xpornum);
			return responseHelper.getResponse();
		}
		if(!"Open".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setErrorStatusAndMessage("Can't delete purhcase order, because order is not Open");
			return responseHelper.getResponse();
		}

		// check order has detail
		List<PoordDetail> details = poordService.findPoorddetailByXpornum(xpornum);
		if(details != null && !details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all order details first");
			return responseHelper.getResponse();
		}

		long count = poordService.deletePoordheaderByXpornum(xpornum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Delete Purchase Order");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase order deleted successfully");
		responseHelper.setRedirectUrl("/procurements/poord");
		return responseHelper.getResponse();
	}

	@PostMapping("/confirm/{xpornum}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xpornum){

		PoordHeader ph = poordService.findPoordHeaderByXpornum(xpornum);
		if(ph == null) {
			responseHelper.setErrorStatusAndMessage("Purchase order " + xpornum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(ph.getXstatuspor())) {
			responseHelper.setErrorStatusAndMessage("Purchase order " + xpornum + " is not Open");
			return responseHelper.getResponse();
		}

		// check puchase order has details list
		List<PoordDetail> details = poordService.findPoorddetailByXpornum(xpornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Purchase order details is empty");
			return responseHelper.getResponse();
		}

		ph.setXstatuspor("Confirmed");
		long count = poordService.update(ph);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirmed purchase order " + xpornum);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("poordheaderform", "/procurements/poord/poordheaderform/" + ph.getXpornum());
		responseHelper.setSuccessStatusAndMessage("Purchase order confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("{xpornum}/poorddetail/{xrow}/show")
	public String openPoordDetailModal(@PathVariable String xpornum, @PathVariable String xrow, Model model) {

		model.addAttribute("purchaseUnit", xcodesService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			PoordDetail poorddetail = new PoordDetail();
			poorddetail.setXpornum(xpornum);
			poorddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			poorddetail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			model.addAttribute("poorddetail", poorddetail);
		} else {
			PoordDetail poorddetail = poordService.findPoorddetailByXpornumAndXrow(xpornum, Integer.parseInt(xrow));
			if(poorddetail == null) {
				poorddetail = new PoordDetail();
				poorddetail.setXpornum(xpornum);
				poorddetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				poorddetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				poorddetail.setXlineamt(poorddetail.getXqtyord().multiply(poorddetail.getXrate()));
			}
			model.addAttribute("poorddetail", poorddetail);
		}

		return "pages/purchasing/poord/poorddetailmodal::poorddetailmodal";
	}

	@PostMapping("/poorddetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(PoordDetail poordDetail){
		if(poordDetail == null || StringUtils.isBlank(poordDetail.getXpornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// validation
		if(StringUtils.isBlank(poordDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}
		if(poordDetail.getXqtyord() == null || BigDecimal.ZERO.equals(poordDetail.getXqtyord()) || poordDetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Purchase quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// modify line amount
		// first get item vat rate
		Caitem caitem = caitemService.findByXitem(poordDetail.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found");
			return responseHelper.getResponse();
		}
		if(caitem.getXvatrate() == null) caitem.setXvatrate(BigDecimal.ZERO);

		poordDetail.setXlineamt(poordDetail.getXqtyord().multiply(poordDetail.getXrate().setScale(2, RoundingMode.DOWN)));
		poordDetail.setXlineamt(poordDetail.getXlineamt().add((poordDetail.getXlineamt().multiply(caitem.getXvatrate())).divide(BigDecimal.valueOf(100))));

		// if existing
		PoordDetail existDetail = poordService.findPoorddetailByXpornumAndXrow(poordDetail.getXpornum(), poordDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(poordDetail, existDetail, "xpornum", "xrow");
			long count = poordService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update order detail");
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("poorddetailtable", "/procurements/poord/poorddetail/" + poordDetail.getXpornum());
			responseHelper.setSecondReloadSectionIdWithUrl("poordheaderform", "/procurements/poord/poordheaderform/" + poordDetail.getXpornum());
			responseHelper.setSuccessStatusAndMessage("Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = poordService.saveDetail(poordDetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save order detail");
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("poorddetailtable", "/procurements/poord/poorddetail/" + poordDetail.getXpornum());
		responseHelper.setSecondReloadSectionIdWithUrl("poordheaderform", "/procurements/poord/poordheaderform/" + poordDetail.getXpornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/poorddetail/{xpornum}")
	public String reloadPoordDetailTabble(@PathVariable String xpornum, Model model) {
		List<PoordDetail> detailList = poordService.findPoorddetailByXpornum(xpornum);
		model.addAttribute("poorddetailsList", detailList);
		model.addAttribute("poordheader", poordService.findPoordHeaderByXpornum(xpornum));
		return "pages/purchasing/poord/poord::poorddetailtable";
	}

	@GetMapping("/poordheaderform/{xpornum}")
	public String reloadPoordheaderForm(@PathVariable String xpornum, Model model) {
		model.addAttribute("poprefix", xtrnService.findByXtypetrn(TransactionCodeType.PURCHASE_ORDER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodesService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("postatusList", xcodesService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("poordheader", poordService.findPoordHeaderByXpornum(xpornum));
		return "pages/purchasing/poord/poord::poordheaderform";
	}

	@PostMapping("{xpornum}/poorddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xpornum, @PathVariable String xrow, Model model) {
		PoordDetail pd = poordService.findPoorddetailByXpornumAndXrow(xpornum, Integer.parseInt(xrow));
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
		responseHelper.setReloadSectionIdWithUrl("poorddetailtable", "/procurements/poord/poorddetail/" + xpornum);
		responseHelper.setSecondReloadSectionIdWithUrl("poordheaderform", "/procurements/poord/poordheaderform/" + xpornum);
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xpornum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String xpornum) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		PoordHeader oh = poordService.findPoordHeaderByXpornum(xpornum);

		if (oh == null) {
			message = "Purchase Order not found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Cacus cacus = cacusService.findByXcus(oh.getXcus());

		//SalesOrderChalanReport orderReport = new SalesOrderChalanReport();

		PurchaseReport report = new PurchaseReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Purchase Order Report");
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setToDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));

		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setOrderNumber(oh.getXpornum());
		purchaseOrder.setSupplier(cacus.getXcus());
		purchaseOrder.setSupplierName(cacus.getXorg());
		purchaseOrder.setSupplierAddress(cacus.getXmadd());
		purchaseOrder.setWarehouse(oh.getXwh());
		purchaseOrder.setDate(sdf.format(oh.getXdate()));
		purchaseOrder.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());

		List<PoordDetail> items = poordService.findPoorddetailByXpornum(oh.getXpornum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemdesc());
				item.setItemQty(it.getXqtyord() != null ? it.getXqtyord().toString() : BigDecimal.ZERO.toString());
				item.setItemUnit(it.getXunitpur());
				item.setItemCategory(it.getXcatitem());
				item.setItemGroup(it.getXgitem());
				item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : BigDecimal.ZERO.toString());
				item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				purchaseOrder.getItems().add(item);
			});
		}

		report.getPurchaseorders().add(purchaseOrder);

		byte[] byt = getPDFByte(report, "purchaseorderreport.xsl");
		if (byt == null) {
			message = "Can't print report for Purchase Order : " + xpornum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@GetMapping("/caitem/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		Caitem centralCaitem = caitemService.findByXitem(xitem);
		return centralCaitem;
	}

	@PostMapping("/creategrn/{xpornum}")
	public @ResponseBody Map<String, Object> confirmPurchase(@PathVariable String xpornum){
		try {
			return poordService.createPurchaseOrderToGRN(responseHelper, xpornum);
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}
	}
}
