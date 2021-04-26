package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.GRNOrder;
import com.asl.model.report.GrnReport;
import com.asl.model.report.ItemDetails;
import com.asl.service.CacusService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/purchasing/pogrn")
public class PogrnController extends ASLAbstractController {

	@Autowired private PogrnService pogrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private VataitService vataitService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;

	@GetMapping
	public String loadGRNPage(Model model) {

		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());

		return "pages/purchasing/pogrn/pogrn";
	}

	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null) data = getDefaultPogrnHeader();

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));

		return "pages/purchasing/pogrn/pogrn";
	}

	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtype(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXtrngrn(TransactionCodeType.GRN_NUMBER.getdefaultCode());
		pogrn.setXdate(new Date());
		pogrn.setXstatusgrn("Open");
		pogrn.setXtypetrn("Purchase");
		pogrn.setXvatait("No Vat");
		pogrn.setXtotamt(BigDecimal.ZERO);
		pogrn.setXvatamt(BigDecimal.ZERO);
		pogrn.setXaitamt(BigDecimal.ZERO);
		pogrn.setXdiscprime(BigDecimal.ZERO);
		pogrn.setXgrandtot(BigDecimal.ZERO);
		return pogrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult) {

		if ((pogrnHeader == null || StringUtils.isBlank(pogrnHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please Select Supplier");
			return responseHelper.getResponse();
		}
//		if (StringUtils.isBlank(pogrnHeader.getXinvnum())) {
//			responseHelper.setErrorStatusAndMessage("Please provide bill info");
//			return responseHelper.getResponse();
//		}

		if (pogrnHeader.getXtotamt() == null) pogrnHeader.setXtotamt(BigDecimal.ZERO);
		if (pogrnHeader.getXdiscprime() == null) pogrnHeader.setXdiscprime(BigDecimal.ZERO);
		if (pogrnHeader.getXaitamt() == null) pogrnHeader.setXaitamt(BigDecimal.ZERO);
		BigDecimal grandTotal = pogrnHeader.getXtotamt().add(pogrnHeader.getXvatamt()).add(pogrnHeader.getXaitamt()).subtract(pogrnHeader.getXdiscprime());
		pogrnHeader.setXgrandtot(grandTotal);

		// if existing record
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if (existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtype", "xdate");
			long count = pogrnService.update(existPogrnHeader);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update GRN");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/purchasing/pogrn/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pogrnService.save(pogrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xgrnnum}")
	public  @ResponseBody Map<String, Object> archive(@PathVariable String xgrnnum) {
		PogrnHeader header = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(header == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN in the system");
			return responseHelper.getResponse();
		}

		// first remove all item details
		if(pogrnService.countOfPogrndetailByXgrnnum(xgrnnum) > 0 && pogrnService.archiveDetailsByXgrnnum(xgrnnum) < 1) {
			responseHelper.setErrorStatusAndMessage("Can't archive GRN item details");
			return responseHelper.getResponse();
		}

		// 2nd remove reference from purchase order if have reference
		if(StringUtils.isNotBlank(header.getXpornum())) {
			PoordHeader ph = poordService.findPoordHeaderByXpornum(header.getXpornum());
			if(ph != null) {
				ph.setXstatuspor("Open");
				ph.setXgrnnum(null);
				long phcount = poordService.update(ph);
				if(phcount == 0) {
					responseHelper.setErrorStatusAndMessage("Can't remove Purchase Order reference from GRN");
					return responseHelper.getResponse();
				}
			}
		}

		// 3rd now archive GRN
		header.setZactive(Boolean.FALSE);
		long hcount = pogrnService.update(header);
		if(hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't archive GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("GRN archived successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/");
		return responseHelper.getResponse();
	}

	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		model.addAttribute("customer", data.getXcus());
		model.addAttribute("pornum", data.getXpornum());
		model.addAttribute("purUnitList", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if ("new".equalsIgnoreCase(xrow)) {
			PogrnDetail pogrndetail = new PogrnDetail();
			pogrndetail.setXgrnnum(xgrnnum);
			pogrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			pogrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			pogrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("pogrndetail", pogrndetail);
		} else {
			PogrnDetail pogrndetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
			if (pogrndetail == null) {
				pogrndetail = new PogrnDetail();
				pogrndetail.setXgrnnum(xgrnnum);
				pogrndetail.setXqtygrn(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				pogrndetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				pogrndetail.setXqtyprn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("pogrndetail", pogrndetail);
		}

		return "pages/purchasing/pogrn/pogrndetailmodal::pogrndetailmodal";
	}

	@PostMapping("/pogrndetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail pogrnDetail) {
		if (pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(pogrnDetail.getXqtygrn() == null || pogrnDetail.getXqtygrn().equals(BigDecimal.ZERO) || pogrnDetail.getXqtygrn().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("Quantity must be greater then 0");
			return responseHelper.getResponse();
		}

		// modify line amount
		pogrnDetail.setXlineamt(pogrnDetail.getXqtygrn().multiply(pogrnDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		PogrnDetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrnDetail.getXgrnnum(), pogrnDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(pogrnDetail, existDetail, "xgrnnum", "xrow");
			long count = pogrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/purchasing/pogrn/pogrndetail/" + pogrnDetail.getXgrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/purchasing/pogrn/pogrnheaderform/" + pogrnDetail.getXgrnnum());
			responseHelper.setSuccessStatusAndMessage("GRN Item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrnDetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/purchasing/pogrn/pogrndetail/" + pogrnDetail.getXgrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/purchasing/pogrn/pogrnheaderform/" + pogrnDetail.getXgrnnum());
		responseHelper.setSuccessStatusAndMessage("GRN Item saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/pogrndetail/{xgrnnum}")
	public String reloadPogrnDetailTable(@PathVariable String xgrnnum, Model model) {
		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));
		model.addAttribute("pogrnheader", pogrnService.findPogrnHeaderByXgrnnum(xgrnnum));
		return "pages/purchasing/pogrn/pogrn::pogrndetailtable";
	}

	@GetMapping("/pogrnheaderform/{xgrnnum}")
	public String loadPogrnheaderform(@PathVariable String xgrnnum, Model model) {
		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));
		model.addAttribute("postatusList", xcodeService.findByXtype(CodeType.PURCHASE_ORDER_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("grnStatusList", xcodeService.findByXtype(CodeType.GRN_STATUS.getCode(), Boolean.TRUE));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		return "pages/purchasing/pogrn/pogrn::pogrnheaderform";
	}

	@PostMapping("{xgrnnum}/pogrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePogrnDetail(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnDetail pd = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = pogrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/purchasing/pogrn/pogrndetail/" + xgrnnum);
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/purchasing/pogrn/pogrnheaderform/" + xgrnnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/confirmgrn/{xgrnnum}")
	public @ResponseBody Map<String, Object> confirmgrn(@PathVariable String xgrnnum) {
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("GRN not found in this system");
			return responseHelper.getResponse();
		}

		// Validate
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Supplier required");
			return responseHelper.getResponse();
		}
		if ("Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			responseHelper.setErrorStatusAndMessage("GRN already confirmed");
			return responseHelper.getResponse();
		}

		List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if (pogrnDetailList == null || pogrnDetailList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("GRN has no item details");
			return responseHelper.getResponse();
		}

		String p_seq;
		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusgrn())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procInventory(pogrnHeader.getXgrnnum(), pogrnHeader.getXpornum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		if (!"Confirmed".equalsIgnoreCase(pogrnHeader.getXstatusap())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			pogrnService.procTransferPOtoAP(pogrnHeader.getXgrnnum(), p_seq);
			String em = getProcedureErrorMessages(p_seq);
			if (StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("GRN Confirmed successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/" + xgrnnum);
		return responseHelper.getResponse();
	}

	@PostMapping("/returngrn/{xgrnnum}")
	public @ResponseBody Map<String, Object> returngrn(@PathVariable String xgrnnum) {
		// Get PoordHeader record by Xpornum
		PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(pogrnHeader == null) {
			responseHelper.setErrorStatusAndMessage("GRN not found in the system to do return");
			return responseHelper.getResponse();
		}

		// validation
		if("GRN Returned".equalsIgnoreCase(pogrnHeader.getXstatusgrn())){
			responseHelper.setErrorStatusAndMessage("This GRN already returned. Return number is : " + pogrnHeader.getXcrnnum());
			return responseHelper.getResponse();
		}

		Pocrnheader pocrnHeader = new Pocrnheader();
		BeanUtils.copyProperties(pogrnHeader, pocrnHeader, "xdate", "xtype", "xtrngrn", "xnote");
		pocrnHeader.setXtype(TransactionCodeType.PRN_NUMBER.getCode());
		pocrnHeader.setXtrncrn(TransactionCodeType.PRN_NUMBER.getdefaultCode());
		pocrnHeader.setXgrnnum(xgrnnum);
		pocrnHeader.setXstatuscrn("Open");
		pocrnHeader.setXdate(new Date());
		pocrnHeader.setXsup(pogrnHeader.getXcus());
		// pocrnHeader.setXtypetrn("??");

		long count = pocrnService.save(pocrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN Return");
			return responseHelper.getResponse();
		}

		pocrnHeader = pocrnService.findPocrnHeaderByXgrnnum(xgrnnum);

		// Get GRN items to copy them in CRN.
		List<PogrnDetail> pogrnDetailList = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		List<Pocrndetail> detailsList = new ArrayList<>();
		for (int i = 0; i < pogrnDetailList.size(); i++) {
			Pocrndetail pocrnDetail = new Pocrndetail();
			// Copying PO items to GRN items.
			BeanUtils.copyProperties(pogrnDetailList.get(i), pocrnDetail, "xrow", "xnote");
			pocrnDetail.setXcrnnum(pocrnHeader.getXcrnnum());
			pocrnDetail.setXqtygrn(pogrnDetailList.get(i).getXqtygrn());
			detailsList.add(pocrnDetail);
		}

		try {
			long dcount = pocrnService.saveDetails(detailsList);
			if(dcount == 0) {
				responseHelper.setErrorStatusAndMessage("GRN Return detail not saved");
				return responseHelper.getResponse();
			}
		} catch (ServiceException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// Update PoGRNHeader Status with pocrn reference
		pogrnHeader.setXcrnnum(pocrnHeader.getXcrnnum());
		pogrnHeader.setXstatusgrn("GRN Returned"); // Is it final?
		long pCount = pogrnService.update(pogrnHeader);
		if (pCount == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("GRN Returned successfully");
		responseHelper.setRedirectUrl("/purchasing/pogrn/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xgrnnum}")
	public ResponseEntity<byte[]> printDeliveryOrderWithDetails(@PathVariable String xgrnnum) {
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

		byte[] byt = getPDFByte(report, "grnreport.xsl");
		if (byt == null) {
			message = "Can't print report for GRN : " + xgrnnum;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}
