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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Immofgdetail;
import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImmofgdetailService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.XtrnService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/deliveryorderchalan")
public class DeliveryOrderChalanController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private OpdoService opdoService;
	@Autowired private XtrnService xtrnService;
	@Autowired private ImmofgdetailService immofgdetailService;

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
		if(oh == null) return "redirect:/salesninvoice/deliveryorderchalan";

		model.addAttribute("deliveryorderchalan", oh);
		model.addAttribute("deliveryorderchalanprefix", xtrnService.findByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), Boolean.TRUE));
		model.addAttribute("deliveryorderchalanList", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode()));
		
		List<Opdoheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		if("Open".equalsIgnoreCase(oh.getXstatusord()))
			allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrder(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), "Open", new Date()));
		
		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum));
		
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
	public @ResponseBody Map<String, Object> save(Opdoheader opdoheader, BindingResult bindingResult, Model model){
		if(opdoheader == null || StringUtils.isBlank(opdoheader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Opdoheader existOh = opdoService.findOpdoHeaderByXdornum(opdoheader.getXdornum());
		if(existOh != null) {
			BeanUtils.copyProperties(opdoheader, existOh, "xdate", "xstatusord");
			long count = opdoService.update(existOh);
			if(count == 0) {
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
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Chalan not created");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan");
		responseHelper.setSuccessStatusAndMessage("Chalan created successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/opendeliveryorder/query")
	public String reloadTableWithData(@RequestParam String xdornum, @RequestParam String date, Model model) throws ParseException {
		model.addAttribute("deliveryorderchalan", opdoService.findOpdoHeaderByXdornum(xdornum));
		List<Opdoheader> allOpenAndConfirmesSalesOrders = new ArrayList<>();
		
		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrder(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), "Open", new Date()));
		
		allOpenAndConfirmesSalesOrders.addAll(opdoService.findAllInvoiceOrderByChalan(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode(), xdornum));
		model.addAttribute("openinvoiceorders", allOpenAndConfirmesSalesOrders);
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderchalan::opendeliveryorderstable";
	}

	@PostMapping("/opendeliveryorder/query")
	public @ResponseBody Map<String, Object> queryForrequistionDetails(String xdornum, Date xdate, Model model){
		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable", "/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum="+ xdornum +"&date=" + sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@GetMapping("/ordreqdetails/{xdornum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String xdornum, Model model) {
		model.addAttribute("opdodetailsList", opdoService.findOpdoDetailByXdornum(xdornum));
		return "pages/salesninvoice/deliveryorderchalan/deliveryorderdetailmodal::deliveryorderdetailmodal";
	}

	@PostMapping("/deliveryorderconfirm/{chalan}/{xdornum}")
	public @ResponseBody Map<String, Object> confirmSalesOrderAndCreateChalanDetail(@PathVariable String chalan, @PathVariable String xdornum, Model model){
		
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if(oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isNotBlank(oh.getXdocnum())) {
			responseHelper.setErrorStatusAndMessage("Sales order already added to chalan : " + oh.getXdocnum() + " . Please reload this page again");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xdornum + " Sales Order has no item to add this chalan");
			return responseHelper.getResponse();
		}

		// create or update chalan detail first
		for(Opdodetail pd : details) {
			// check chalan detail already exist using item
			Opdodetail existChalanDetail = opdoService.findOpdoDetailByXdornumAndXitem(chalan, pd.getXitem());
			if(existChalanDetail != null) {  // update existing with qty
				existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().add(pd.getXqtyord()));
				long countChalanDetail = opdoService.updateDetail(existChalanDetail);
				if(countChalanDetail == 0) {
					responseHelper.setErrorStatusAndMessage("Can't update chalan detail");
					return responseHelper.getResponse();
				}
			} else {  // create new detail
				Opdodetail opdodetail = new Opdodetail();
				opdodetail.setXdornum(chalan);
				opdodetail.setXitem(pd.getXitem());
				opdodetail.setXunitsel(pd.getXunitsel());
				opdodetail.setXqtyord(pd.getXqtyord());
				opdodetail.setXrate(pd.getXrate());
				long countChalanDetail = opdoService.saveDetail(opdodetail);
				if(countChalanDetail == 0) {
					responseHelper.setErrorStatusAndMessage("Can't create chalan detail");
					return responseHelper.getResponse();
				}
			}
		}

		// now update sales order with chalan reference
		oh.setXdocnum(chalan);
		oh.setXstatusord("Confirmed");
		long count = opdoService.update(oh);
		
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable", "/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum="+ chalan +"&date=" + sdf.format(oh.getXdate()));
		responseHelper.setSecondReloadSectionIdWithUrl("deliveryorderchalandetailtable", "/salesninvoice/deliveryorderchalan/chalandetail/" + chalan);
		responseHelper.setSuccessStatusAndMessage("Sales order confirmed");
		return responseHelper.getResponse();
	}

	@PostMapping("/deliveryorderrevoke/{chalan}/{xdornum}")
	public @ResponseBody Map<String, Object> revokeSalesOrder(@PathVariable String chalan, @PathVariable String xdornum, Model model){
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if(oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdoService.findOpdoDetailByXdornum(xdornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("This " + xdornum + " Sales Order has no item to remove from chalan");
			return responseHelper.getResponse();
		}

		// create or update chalan detail first
		for(Opdodetail pd : details) {
			// check chalan detail already exist using item
			Opdodetail existChalanDetail = opdoService.findOpdoDetailByXdornumAndXitem(chalan, pd.getXitem());
			if(existChalanDetail == null) continue;

			// update existing with qty
			existChalanDetail.setXqtyord(existChalanDetail.getXqtyord().subtract(pd.getXqtyord()));
			long countChalanDetail = 0;
			if(BigDecimal.ZERO.equals(existChalanDetail.getXqtyord())) {
				countChalanDetail = opdoService.deleteDetail(existChalanDetail);
			} else {
				countChalanDetail = opdoService.updateDetail(existChalanDetail);
			}
			if(countChalanDetail == 0) {
				responseHelper.setErrorStatusAndMessage("Can't update chalan detail");
				return responseHelper.getResponse();
			}
		}

		// now update sales order with chalan reference
		oh.setXdocnum(null);
		oh.setXstatusord("Open");
		long count = opdoService.update(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't Update Sales Order");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("opendeliveryorderstable", "/salesninvoice/deliveryorderchalan/opendeliveryorder/query?xdornum="+ chalan +"&date=" + sdf.format(oh.getXdate()));
		responseHelper.setSecondReloadSectionIdWithUrl("deliveryorderchalandetailtable", "/salesninvoice/deliveryorderchalan/chalandetail/" + chalan);
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
	public @ResponseBody Map<String, Object> lockChalan(@PathVariable String xdornum, Model model){
		
		Opdoheader oh = opdoService.findOpdoHeaderByXdornum(xdornum);
		if(oh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// transfer all chalan deails to immofgdetail
		List<Opdodetail> chalandetails = opdoService.findOpdoDetailByXdornum(oh.getXdornum());
		for(Opdodetail c : chalandetails) {
			/*
			Immofgdetail id = new Immofgdetail();
			id.setXtornum(c.getXordernum());
			id.setXrow(c.getXrow());
			id.setXunit(c.getXunit());
			id.setXitem(c.getXitem());
			id.setXqtyord(c.getXqtyord());
			id.setXnote(c.getXlong());
			long count = immofgdetailService.save(id);
			if(count == 0) {
				log.error("ERROR is : {}", "Can't insert chaland details to Immofgdetail table for chalan " + c.getXordernum());
			}
			*/
		}

		// now lock chalan
		oh.setXstatusord("Confirmed");
		long count = opdoService.update(oh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't lock Chalan");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Chalan locked successfully");
		responseHelper.setRedirectUrl("/salesninvoice/deliveryorderchalan/" + xdornum);
		return responseHelper.getResponse();
	}


//	@GetMapping
//	public String loadSalesOrderPage(Model model) {
//		Opordheader chalan = opordService.findOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), new Date());
//		List<Oporddetail> chalanDetails = new ArrayList<>();
//		if(chalan != null) {
//			chalanDetails = opordService.findOporddetailByXordernum(chalan.getXordernum());
//			if(chalanDetails == null) chalanDetails = new ArrayList<>();
//		}
//		if(chalan == null) chalan = new Opordheader();
//		model.addAttribute("chalan", chalan);
//		model.addAttribute("chalanDetails", chalanDetails);
//		return "pages/salesninvoice/salesorderchalan/salesorderchalan";
//	}
//
//	@GetMapping("/query")
//	public String reloadTableWithData(@RequestParam String date, Model model) throws ParseException {
//		Opordheader chalan = opordService.findOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), sdf.parse(date));
//		List<Oporddetail> chalanDetails = new ArrayList<>();
//		if(chalan != null) {
//			chalanDetails = opordService.findOporddetailByXordernum(chalan.getXordernum());
//			if(chalanDetails == null) chalanDetails = new ArrayList<>();
//		}
//		if(chalan == null) chalan = new Opordheader();
//		model.addAttribute("chalan", chalan);
//		model.addAttribute("chalanDetails", chalanDetails);
//		return "pages/salesninvoice/salesorderchalan/salesorderchalan::salesorderchalansection";
//	}
//
//	@PostMapping("/query")
//	public @ResponseBody Map<String, Object> queryForChalanDetails(Date xdate, Model model){
//		responseHelper.setReloadSectionIdWithUrl("salesorderchalansection", "/salesninvoice/salesorderchalan/query?date=" + sdf.format(xdate));
//		responseHelper.setStatus(ResponseStatus.SUCCESS);
//		return responseHelper.getResponse();
//	}
}
