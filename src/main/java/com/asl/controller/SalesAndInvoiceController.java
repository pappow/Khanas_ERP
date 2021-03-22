package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

import com.asl.entity.Opdodetail;
import com.asl.entity.Opdoheader;
import com.asl.entity.PogrnDetail;
import com.asl.entity.PogrnHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpdoService;
import com.asl.service.VataitService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/salesninvoice/salesandinvoice")
public class SalesAndInvoiceController extends ASLAbstractController {
	
	@Autowired
	private OpdoService opdoService;
	@Autowired
	private XcodesService xcodeService;
	@Autowired
	private XtrnService xtrnService;
	@Autowired
	private VataitService vataitService;
	
	@GetMapping
	public String loadInvoicePage(Model model) {
		
		model.addAttribute("opdoheader", getDefaultOpdoHeader());
		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode()));
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));
		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));		
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode()));
		model.addAttribute("payStatusList", new ArrayList<>());
		model.addAttribute("arStatusList", new ArrayList<>());		
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode()));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		
		return "pages/salesninvoice/salesandinvoice/opdo";
	}
	
	@GetMapping("/{xdornum}")
	public String loadInvoicePage(@PathVariable String xdornum, Model model) {
		
		Opdoheader data = opdoService.findOpdoHeaderByXdornum(xdornum); 
		if(data == null) data = getDefaultOpdoHeader();

		model.addAttribute("opdoheader", data);
		model.addAttribute("opdoprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode()));
		model.addAttribute("allOpdoHeader", opdoService.findAllOpdoHeaderByXtypetrnAndXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode()));
		model.addAttribute("paymentTypeList", xcodeService.findByXtype(CodeType.PAYMENT_TYPE.getCode()));
		model.addAttribute("jvStatusList", xcodeService.findByXtype(CodeType.JOURNAL_VOUCHER_STATUS.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("ordStatusList", xcodeService.findByXtype(CodeType.STATUS.getCode()));
		model.addAttribute("payStatusList", new ArrayList<>());
		model.addAttribute("arStatusList", new ArrayList<>());		
		model.addAttribute("currencyList", xcodeService.findByXtype(CodeType.CURRENCY_OF_PRICE.getCode()));
		model.addAttribute("vataitList", vataitService.getAllVatait());
		model.addAttribute("opdoDetailsList", opdoService.findOpdoDetailByXdornum(xdornum));
		
		return "pages/salesninvoice/salesandinvoice/opdo";
	}
	
	private Opdoheader getDefaultOpdoHeader() {
		Opdoheader opdoheader = new Opdoheader();
		opdoheader.setXtype(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		//pogrn.setXtypetrn("Purchase");
		opdoheader.setXstatusord("Open");
		opdoheader.setXtotamt(BigDecimal.ZERO);
		opdoheader.setXtrnopdo(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		opdoheader.setXtypetrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode());
		opdoheader.setXtrn(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getdefaultCode());
		return opdoheader;
	}
	
	
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opdoheader opdoHeader, BindingResult bindingResult){
				
		if((opdoHeader == null || StringUtils.isBlank(opdoHeader.getXtype()))) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// if existing record
		
		Opdoheader existOpdoHeader = opdoService.findOpdoHeaderByXdornum(opdoHeader.getXdornum());
		if(existOpdoHeader != null) {
			BeanUtils.copyProperties(opdoHeader, existOpdoHeader, "xdornum", "xtype", "xdate");
			long count = opdoService.update(existOpdoHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Invoice updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + opdoHeader.getXdornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = opdoService.save(opdoHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Invoice created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + opdoHeader.getXdornum());
		return responseHelper.getResponse();
		
	}
	
	@GetMapping("/opdodetail/{xdornum}")
	public String reloadOpdoDetailTable(@PathVariable String xdornum, Model model) {
		List<Opdodetail> detailList = opdoService.findOpdoDetailByXdornum(xdornum);
		model.addAttribute("opdoDetailsList", detailList);
		Opdoheader header = new Opdoheader();
		header.setXdornum(xdornum);
		model.addAttribute("opdoheader", header);
		return "pages/salesninvoice/salesandinvoice/opdo::opdodetailtable";
	}
	
	@GetMapping("/{xdornum}/opdodetail/{xrow}/show")
	public String openOpdoDetailModal(@PathVariable String xdornum, @PathVariable String xrow, Model model) {

		if("new".equalsIgnoreCase(xrow)) {
			Opdodetail opdodetail= new Opdodetail();
			opdodetail.setXdornum(xdornum);
			opdodetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			opdodetail.setXqtyord(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opdodetail", opdodetail);
		} else {
			Opdodetail opdodetail = opdoService.findOpdoDetailByXdornumAndXrow(xdornum, Integer.parseInt(xrow));
			if(opdodetail == null) {
				opdodetail = new Opdodetail();
				opdodetail.setXdornum(xdornum);
				opdodetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				opdodetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				opdodetail.setXqtycrn(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("opdodetail", opdodetail);
		}
		return "pages/salesninvoice/salesandinvoice/opdodetailmodal::opdodetailmodal";
	}
	
	@PostMapping("/opdodetail/save")
	public @ResponseBody Map<String, Object> saveOpdodetail(Opdodetail opdoDetail){
		
		if(opdoDetail == null || StringUtils.isBlank(opdoDetail.getXdornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// modify line amount
		opdoDetail.setXlineamt(opdoDetail.getXqtyord().multiply(opdoDetail.getXrate().setScale(2, RoundingMode.DOWN)));

		// if existing
		Opdodetail existDetail = opdoService.findOpdoDetailByXdornumAndXrow(opdoDetail.getXdornum(), opdoDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(opdoDetail, existDetail, "xdornum", "xrow");
			long count = opdoService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" +  opdoDetail.getXdornum());
			responseHelper.setSuccessStatusAndMessage("Invoice item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opdoService.saveDetail(opdoDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" +  opdoDetail.getXdornum());
		responseHelper.setSuccessStatusAndMessage("Invoice item saved successfully");		
		return responseHelper.getResponse();
	}
	
	@PostMapping("{xdornum}/opdodetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpdoDetail(@PathVariable String xdornum, @PathVariable String xrow, Model model) {
		Opdodetail pd = opdoService.findOpdoDetailByXdornumAndXrow(xdornum, Integer.parseInt(xrow));
		if(pd == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = opdoService.deleteDetail(pd);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" +  xdornum);
		return responseHelper.getResponse();
	}
	
	@GetMapping("/confirmopdo/{xdornum}")
	public @ResponseBody Map<String, Object> confirmOpdo(@PathVariable String xdornum){
		if(StringUtils.isBlank(xdornum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		Opdoheader opdoHeader = opdoService.findOpdoHeaderByXdornum(xdornum);
		List<Opdodetail> opdoDetailList = opdoService.findOpdoDetailByXdornum(xdornum);
		Integer grandTot = ((opdoHeader.getXtotamt().subtract(opdoHeader.getXdiscamt())).add(opdoHeader.getXvatamt())).intValue();
		
		if(opdoDetailList.size()==0){
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(!"Other".equalsIgnoreCase(opdoHeader.getXpaymenttype())) {
			Integer paid = ((opdoHeader.getXtotamt().subtract(opdoHeader.getXdiscamt())).add(opdoHeader.getXvatamt())).intValue();
			opdoHeader.setXpaid(BigDecimal.valueOf(paid));
		}
		Integer xpaid99 = opdoHeader.getXpaid().add(BigDecimal.valueOf(0.99)).intValue();
		
		if(grandTot > xpaid99 && !"Other".equalsIgnoreCase(opdoHeader.getXpaymenttype())) {
			responseHelper.setErrorStatusAndMessage("Paid amount not to be less than Receivable!");
			return responseHelper.getResponse();
		}
		
		//Opdoheader opdoHeader = opdoService.findOpdoHeaderByXdornum(xdornum);
		//PogrnHeader pogrnHeader = pogrnService.findPogrnHeaderByXgrnnum(xgrnnum);
		
		String p_seq;
		if(!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusord())) {
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procConfirmDO(xdornum, p_seq);
			//Error check here for procConfrimDo
			
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procIssuePricing(xdornum, opdoHeader.getXwh(), p_seq);
			//Error check here for procIssuePricing
			
		}
		if(!"Confirmed".equalsIgnoreCase(opdoHeader.getXstatusar())){
			p_seq = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			opdoService.procTransferOPtoAR(xdornum, "opdoheader", p_seq);
			//Error check here for procTransferOPtoAR
			
		}
			
		responseHelper.setSuccessStatusAndMessage("Invoice Confirmed successfully");

		responseHelper.setRedirectUrl("/salesninvoice/salesandinvoice/" + xdornum);
		return responseHelper.getResponse();
	}


}
