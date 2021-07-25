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
import com.asl.entity.Caitem;
import com.asl.entity.Pocrndetail;
import com.asl.entity.Pocrnheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.entity.PoordDetail;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ServiceException;
import com.asl.model.report.GRNOrder;
import com.asl.model.report.GrnReport;
import com.asl.model.report.ItemDetails;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.PocrnService;
import com.asl.service.PogrnService;
import com.asl.service.PoordService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/procurements/pogrn")
public class GRNController extends ASLAbstractController {

	@Autowired private PogrnService pogrnService;
	@Autowired private PocrnService pocrnService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CacusService cacusService;
	@Autowired private PoordService poordService;
	@Autowired private CaitemService caitemService;

	@GetMapping
	public String loadGRNPage(Model model) {

		model.addAttribute("pogrnheader", getDefaultPogrnHeader());
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));

		return "pages/purchasing/pogrn/pogrn";
	}

	@GetMapping("/{xgrnnum}")
	public String loadGRNPage(@PathVariable String xgrnnum, Model model) {

		PogrnHeader data = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if (data == null) data = getDefaultPogrnHeader();
		if (data.getXtotamt() == null) data.setXtotamt(BigDecimal.ZERO);

		model.addAttribute("pogrnheader", data);
		model.addAttribute("grnprefix", xtrnService.findByXtypetrn(TransactionCodeType.GRN_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("allPogrnHeader", pogrnService.getAllPogrnHeaders());
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE));

		model.addAttribute("pogrnDetailsList", pogrnService.findPogrnDetailByXgrnnum(xgrnnum));

		return "pages/purchasing/pogrn/pogrn";
	}

	private PogrnHeader getDefaultPogrnHeader() {
		PogrnHeader pogrn = new PogrnHeader();
		pogrn.setXtypetrn(TransactionCodeType.GRN_NUMBER.getCode());
		pogrn.setXdate(new Date());
		pogrn.setXstatusgrn("Open");
		pogrn.setXtotamt(BigDecimal.ZERO);
		return pogrn;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(PogrnHeader pogrnHeader, BindingResult bindingResult) {

		// Validate
		if (StringUtils.isBlank(pogrnHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please Select Supplier");
			return responseHelper.getResponse();
		}

		// if existing record
		PogrnHeader existPogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(pogrnHeader.getXgrnnum());
		if (existPogrnHeader != null) {
			BeanUtils.copyProperties(pogrnHeader, existPogrnHeader, "xgrnnum", "xtypetrn","xtrn","xstatusgrn");
			long count = pogrnService.update(existPogrnHeader);
			if (count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update GRN");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("GRN updated successfully");
			responseHelper.setRedirectUrl("/procurements/pogrn/" + pogrnHeader.getXgrnnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = pogrnService.save(pogrnHeader);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create GRN");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("GRN created successfully");
		responseHelper.setRedirectUrl("/procurements/pogrn/" + pogrnHeader.getXgrnnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xgrnnum}")
	public  @ResponseBody Map<String, Object> archive(@PathVariable String xgrnnum) {
		PogrnHeader header = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		if(header == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN in the system");
			return responseHelper.getResponse();
		}

		// check grn has details
		List<PogrnDetail> grndetails = pogrnService.findPogrnDetailByXgrnnum(xgrnnum);
		if(grndetails != null && !grndetails.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete GRN details first");
			return responseHelper.getResponse();
		}

		// 2nd now delete GRN
		long hcount = pogrnService.deletePogrnheader(xgrnnum);
		if(hcount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete GRN");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("GRN deleted successfully");
		responseHelper.setRedirectUrl("/procurements/pogrn/");
		return responseHelper.getResponse();
	}

	@GetMapping("{xgrnnum}/pogrndetail/{xrow}/show")
	public String openPogrnDetailModal(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		model.addAttribute("pogrndetail", pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow)));
		return "pages/purchasing/pogrn/pogrndetailmodal::pogrndetailmodal";
	}

	@PostMapping("/pogrndetail/save")
	public @ResponseBody Map<String, Object> savePogrndetail(PogrnDetail pogrnDetail) {
		if (pogrnDetail == null || StringUtils.isBlank(pogrnDetail.getXgrnnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		if(pogrnDetail.getXqtygrn() == null || BigDecimal.ZERO.equals(pogrnDetail.getXqtygrn()) || pogrnDetail.getXqtygrn().compareTo(BigDecimal.ZERO) == -1){
			responseHelper.setErrorStatusAndMessage("GRN quantity should be greater then 0");
			return responseHelper.getResponse();
		}

		// validate grn quantity
		PogrnHeader pgh = pogrnService.findPogrnHeaderByXgrnnum(pogrnDetail.getXgrnnum());
		if(pgh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN");
			return responseHelper.getResponse();
		}
		PoordDetail podetail = poordService.findPoorddetailByXpornumAndXrow(pgh.getXpornum(), pogrnDetail.getXdocrow());
		if(podetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		// calculate and update poordetail xqtygrn 
		BigDecimal totalGrnQty = pogrnService.getTotalGRNQtyOfConfirmedGRNDetail(podetail.getXrow(), podetail.getXpornum());
		BigDecimal finalValue = totalGrnQty.add(pogrnDetail.getXqtygrn());
		if(finalValue.compareTo(podetail.getXqtyord()) == 1) {
			responseHelper.setErrorStatusAndMessage("GRN quantity can't be greater then purchase order quantity");
			return responseHelper.getResponse();
		}
		podetail.setXqtygrn(finalValue);
		long count2 = poordService.updateDetail(podetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update GRN qty to purchase detail");
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
		pogrnDetail.setXlineamt(pogrnDetail.getXlineamt().add((pogrnDetail.getXlineamt().multiply(caitem.getXvatrate())).divide(BigDecimal.valueOf(100))));

		// if existing
		PogrnDetail existDetail = pogrnService.findPogrnDetailByXgrnnumAndXrow(pogrnDetail.getXgrnnum(), pogrnDetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(pogrnDetail, existDetail, "xgrnnum", "xrow");
			long count = pogrnService.updateDetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/procurements/pogrn/pogrndetail/" + pogrnDetail.getXgrnnum());
			responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrn/pogrnheaderform/" + pogrnDetail.getXgrnnum());
			responseHelper.setSuccessStatusAndMessage("GRN details updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = pogrnService.saveDetail(pogrnDetail);
		if (count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save GRN details");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/procurements/pogrn/pogrndetail/" + pogrnDetail.getXgrnnum());
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrn/pogrnheaderform/" + pogrnDetail.getXgrnnum());
		responseHelper.setSuccessStatusAndMessage("GRN details saved successfully");
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
		return "pages/purchasing/pogrn/pogrn::pogrnheaderform";
	}

	@PostMapping("{xgrnnum}/pogrndetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePogrnDetail(@PathVariable String xgrnnum, @PathVariable String xrow, Model model) {
		PogrnDetail pd = pogrnService.findPogrnDetailByXgrnnumAndXrow(xgrnnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// calculate and update poordetail xqtygrn 
		PogrnHeader pgh = pogrnService.findPogrnHeaderByXgrnnum(pd.getXgrnnum());
		if(pgh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find GRN");
			return responseHelper.getResponse();
		}
		PoordDetail podetail = poordService.findPoorddetailByXpornumAndXrow(pgh.getXpornum(), pd.getXdocrow());
		if(podetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find Puchase order detail for this item row");
			return responseHelper.getResponse();
		}
		BigDecimal totalGrnQty = pogrnService.getTotalGRNQtyOfConfirmedGRNDetail(podetail.getXrow(), podetail.getXpornum());
		podetail.setXqtygrn(totalGrnQty);
		long count2 = poordService.updateDetail(podetail);
		if(count2 == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update GRN qty to purchase detail");
			return responseHelper.getResponse();
		}


		long count = pogrnService.deleteDetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("pogrndetailtable", "/procurements/pogrn/pogrndetail/" + xgrnnum);
		responseHelper.setSecondReloadSectionIdWithUrl("pogrnheaderform", "/procurements/pogrn/pogrnheaderform/" + xgrnnum);
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
		responseHelper.setRedirectUrl("/procurements/pogrn/" + xgrnnum);
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
		if(pogrnDetailList != null && !pogrnDetailList.isEmpty()) {
			for (int i = 0; i < pogrnDetailList.size(); i++) {
				Pocrndetail pocrnDetail = new Pocrndetail();
				// Copying PO items to GRN items.
				BeanUtils.copyProperties(pogrnDetailList.get(i), pocrnDetail, "xrow", "xnote");
				pocrnDetail.setXcrnnum(pocrnHeader.getXcrnnum());
				pocrnDetail.setXqtygrn(pogrnDetailList.get(i).getXqtygrn());
				pocrnDetail.setXunit(pogrnDetailList.get(i).getXunitpur());
				detailsList.add(pocrnDetail);
			}
		}

		if(!detailsList.isEmpty()) {
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
		responseHelper.setRedirectUrl("/procurements/pogrn/" + pogrnHeader.getXgrnnum());
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
