package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.report.GRNOrder;
import com.asl.model.report.GrnReport;
import com.asl.model.report.ItemDetails;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.PogrnService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/procurements/pogrndirect")
public class DirectPurchaseController extends ASLAbstractController {

	@Autowired
	private PogrnService pogrnService;
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private CacusService cacusService;
	
	@Autowired private CaitemService caitemService;

	@GetMapping
	public String loadGRNPage(Model model) {

		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllDirectPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));

		return "pages/purchasing/pogrndirect/pogrndirect";
	}

	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null)
			data = getDefaultPogrnHeader();
		if (data.getXtotamt() == null)
			data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllDirectPogrnHeader());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));

		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));

		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		BigDecimal totalQuantityReceived = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if (details != null && !details.isEmpty()) {
			for (PogrnDetail pd : details) {
				totalQuantityReceived = totalQuantityReceived.add(pd.getXqtygrn() == null ? BigDecimal.ZERO : pd.getXqtygrn());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantityReceived", totalQuantityReceived);
		model.addAttribute("totalLineAmount", totalLineAmount);

		return "pages/purchasing/pogrndirect/pogrndirect";
	}

	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXdate(new Date());
		pogrn.setXstatusgrn("Open");
		pogrn.setXtotamt(BigDecimal.ZERO);
		pogrn.setXvoucher("");
		return pogrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult) {

		// Validate
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please Select Supplier");
			return responseHelper.getResponse();
		}
		if (StringUtils.isBlank(pogrnHeader.getXinvnum())) {
			responseHelper.setErrorStatusAndMessage("Bill Number Required");
			return responseHelper.getResponse();
		}
		
		pogrnHeader.setXtype("Cash");

		// if existing record
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if (existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtypetrn", "xtrn", "xstatusgrn",
					"xpronum", "xcus");
			long count = pogrnService.update(existPogrnHeader);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update GRN");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/procurements/pogrndirect/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pogrnService.save(pogrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save GRN");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("GRN saved successfully");
		responseHelper.setRedirectUrl("/procurements/pogrndirect/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/delete/{xgrnnum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xgrnnum) {
		PogrnHeader header = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (header == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN in the system");
			return responseHelper.getResponse();
		}

		// check grn has details
		List<PogrnDetail> grndetails = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if (grndetails != null && !grndetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete GRN details first");
			return responseHelper.getResponse();
		}

		// 2nd now delete GRN
		long hcount = pogrnService.deletePogrnheader(xgrnnum);
		if (hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("GRN deleted successfully");
		responseHelper.setRedirectUrl("/procurements/pogrndirect/");
		return responseHelper.getResponse();
	}

	//Start of Details Section

	@GetMapping("{xgrnnum}/pogrndirectdetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {

		model.addAttribute("purchaseUnit", xcodesService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));
		if("new".equalsIgnoreCase(xrow)) {
			PogrnDetail detail = new PogrnDetail();
			detail.setXgrnnum(xgrnnum);
			detail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			detail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			detail.setXlineamt(detail.getXqtygrn().multiply(detail.getXrate()));
			model.addAttribute("pogrndetail", detail);
		} else {
			PogrnDetail detail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
			if(detail == null) {
			detail = new PogrnDetail();
			detail.setXgrnnum(xgrnnum);
			detail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			detail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			
			}
			model.addAttribute("pogrndetail", detail);
		}

		return "pages/purchasing/pogrndirect/pogrndirectdetailmodal::pogrndirectdetailmodal";
	}

	@PostMapping("/pogrndirectdetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail pogrnDetail){
		if(pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// validation
		if(StringUtils.isBlank(pogrnDetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an item");
			return responseHelper.getResponse();
		}
		if(pogrnDetail.getXqtygrn() == null || BigDecimal.ZERO.equals(pogrnDetail.getXqtygrn()) || pogrnDetail.getXqtygrn().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Direct Purchase quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// modify line amount
		// first get item vat rate
		Caitem caitem = caitemService.findByXitem(pogrnDetail.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found");
			return responseHelper.getResponse();
		}
		if(caitem.getXvatrate() == null) caitem.setXvatrate(BigDecimal.ZERO);

		pogrnDetail.setXlineamt(pogrnDetail.getXqtygrn().multiply(pogrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		PogrnDetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrnDetail.getXgrnnum(), pogrnDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(pogrnDetail, existDetail, "xgrnnum", "xrow");
			long count = pogrnService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update order detail");
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("pogrndirectdetailtable", "/procurements/pogrndirect/pogrndirectdetail/" + pogrnDetail.getXgrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrndirect/pogrnheaderform/" + pogrnDetail.getXgrnnum());
			responseHelper.setSuccessStatusAndMessage("Direct Purchase details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrnDetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save order detail");
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("pogrndirectdetailtable", "/procurements/pogrndirect/pogrndirectdetail/" + pogrnDetail.getXgrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrndirect/pogrnheaderform/" + pogrnDetail.getXgrnnum());
		responseHelper.setSuccessStatusAndMessage("GRN details saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/pogrndirectdetail/{xgrnnum}")
	public String reloadPoordDetailTabble(@PathVariable String xgrnnum, Model model) {
		List<PogrnDetail> detailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		model.addAttribute("pogrnDetailsList", detailList);
		model.addAttribute("pogrnheader", pogrnService.findPogrnHeaderByXgrnnum(xgrnnum));
		
		BigDecimal totalQuantityReceived = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if (detailList != null && !detailList.isEmpty()) {
			for (PogrnDetail pd : detailList) {
				totalQuantityReceived = totalQuantityReceived.add(pd.getXqtygrn() == null ? BigDecimal.ZERO : pd.getXqtygrn());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantityReceived", totalQuantityReceived);
		model.addAttribute("totalLineAmount", totalLineAmount);
		return "pages/purchasing/pogrndirect/pogrndirect::pogrndirectdetailtable";
	}

	@GetMapping("/pogrnheaderform/{xgrnnum}")
	public String reloadPoordheaderForm(@PathVariable String xgrnnum, Model model) {
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.STORE.getCode(), Boolean.TRUE));
		return "pages/purchasing/pogrndirect/pogrndirect::pogrnheaderform";
	}

	@PostMapping("{xgrnnum}/pogrndirectdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnDetail pd = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
		if(pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pogrnService.deleteDetail(pd);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("pogrndirectdetailtable", "/procurements/pogrndirect/pogrndirectdetail/" + xgrnnum);
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrndirect/pogrnheaderform/" + xgrnnum);
		return responseHelper.getResponse();
	}
	
	@PostMapping("/confirm/{xgrnnum}")
	public @ResponseBody Map<String, Object> confirm(@PathVariable String xgrnnum){

		PogrnHeader ph = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(ph == null) {
			responseHelper.setErrorStatusAndMessage("Purchase order " + xgrnnum + " not found");
			return responseHelper.getResponse();
		}

		if(!"Open".equalsIgnoreCase(ph.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("Purchase order " + xgrnnum + " is not Open");
			return responseHelper.getResponse();
		}

		// check puchase order has details list
		List<PogrnDetail> details = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Purchase order details is empty");
			return responseHelper.getResponse();
		}
		ph.setXstatusgrn("Confirmed");

		long count = pogrnService.update(ph);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't confirmed purchase order " + xgrnnum);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrndirect/pogrnheaderform/" + ph.getXgrnnum());
		responseHelper.setSuccessStatusAndMessage("Purchase order confirmed successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xgrnnum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String xgrnnum, HttpServletRequest request) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		PogrnHeader oh = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (oh == null) {
			message = "Good Receipt Note not found to print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Cacus cacus = cacusService.findByXcus(oh.getXcus());

		// SalesOrderChalanReport orderReport = new SalesOrderChalanReport();

		GrnReport report = new GrnReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Good Receipt Note");
		report.setFromDate(sdf.format(oh.getXdate()));
		report.setToDate(sdf.format(oh.getXdate()));
		report.setPrintDate(sdf.format(new Date()));
		report.setReportLogo(appConfig.getReportLogo());

		GRNOrder grn = new GRNOrder();
		grn.setOrderNumber(oh.getXgrnnum());
		grn.setPoNumber(oh.getXpornum());
		grn.setReturnNumber(oh.getXcrnnum());
		grn.setSupplier(cacus.getXcus());
		grn.setSupplierName(cacus.getXorg());
		grn.setSupplierAddress(cacus.getXmadd());
		grn.setWarehouse(oh.getXwh());
		grn.setDate(sdf.format(oh.getXdate()));
		grn.setVatAit(oh.getXvatait());
		grn.setTotalAmount(oh.getXtotamt() != null ? oh.getXtotamt().toString() : BigDecimal.ZERO.toString());
		grn.setVatAmount(oh.getXvatamt() != null ? oh.getXvatamt().toString() : BigDecimal.ZERO.toString());
		grn.setTaxAmount(oh.getXaitamt() != null ? oh.getXaitamt().toString() : BigDecimal.ZERO.toString());
		grn.setDiscountAmount(oh.getXdiscprime() != null ? oh.getXdiscprime().toString() : BigDecimal.ZERO.toString());
		grn.setGrandTotalAmount(oh.getXgrandtot() != null ? oh.getXgrandtot().toString() : BigDecimal.ZERO.toString());

		List<PogrnDetail> items = pogrnService.findPogrnDetailByXgrnnum(oh.getXgrnnum());
		if (items != null && !items.isEmpty()) {
			items.stream().forEach(it -> {
				ItemDetails item = new ItemDetails();
				item.setItemCode(it.getXitem());
				item.setItemName(it.getXitemdesc());
				item.setItemQty(it.getXqtygrn() != null ? it.getXqtygrn().toString() : BigDecimal.ZERO.toString());
				item.setItemRate(it.getXrate() != null ? it.getXrate().toString() : BigDecimal.ZERO.toString());
				item.setItemTotalAmount(it.getXlineamt() != null ? it.getXlineamt().toString() : BigDecimal.ZERO.toString());
				item.setItemUnit(it.getXunitpur());
				item.setItemCategory(it.getXcatitem());
				item.setItemGroup(it.getXgitem());
				grn.getItems().add(item);
			});
		}

		report.getGrnorders().add(grn);

		byte[] byt = getPDFByte(report, "grnreport.xsl", request);
		if (byt == null) {
			message = "Can't print report for GRN : " + xgrnnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
}
