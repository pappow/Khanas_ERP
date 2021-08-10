package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import java.util.List;
import com.asl.entity.Caitem;
import com.asl.entity.Opreqdetail;
import com.asl.entity.Opreqheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.OpreqService;
import com.asl.service.XtrnService;


import com.asl.service.CaitemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/salesninvoice/opreq")
public class SalesRequisitionController extends ASLAbstractController {

	@Autowired private OpreqService opreqService;
	@Autowired private XtrnService xtrnService;
	@Autowired private CaitemService caitemService;


	@GetMapping
	public String loadSalesOrderPage(Model model) {

		model.addAttribute("opreqheader", getDefaultOpordHeader());
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqheader());
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqheader());
		model.addAttribute("opreqprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode(), Boolean.TRUE));

		return "pages/salesninvoice/reqorder/reqorder";
	}

	@GetMapping("/{xdoreqnum}")
	public String loadSalesOrderPage(@PathVariable String xdoreqnum, Model model) {
		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if (data == null) return "redirect:/salesninvoice/opreq";

		model.addAttribute("opreqheader", data);
		model.addAttribute("allOpreqHeader", opreqService.getAllOpreqheader());
		model.addAttribute("allOpenOpreqHeader", opreqService.getAllStatusOpenOpreqheader());
		model.addAttribute("opreqDetailsList", opreqService.findOpreqDetailByXdoreqnum(xdoreqnum));


		List<Opreqdetail> opreqDetails = opreqService.findOpreqDetailByXdoreqnum(xdoreqnum);
		BigDecimal totalQuantity = BigDecimal.ZERO;
		BigDecimal totalLineAmount = BigDecimal.ZERO;
		if(opreqDetails != null && !opreqDetails.isEmpty()) {
			for(Opreqdetail pd : opreqDetails) {
				totalQuantity = totalQuantity.add(pd.getXqtyord() == null ? BigDecimal.ZERO : pd.getXqtyord());
				totalLineAmount = totalLineAmount.add(pd.getXlineamt() == null ? BigDecimal.ZERO : pd.getXlineamt());
			}
		}
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalLineAmount", totalLineAmount);
		
		return "pages/salesninvoice/reqorder/reqorder";
	}

	private Opreqheader getDefaultOpordHeader() {
		Opreqheader opreqheader = new Opreqheader();
		opreqheader.setXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode());
		opreqheader.setXtrn(TransactionCodeType.SALES_REQUESTION_ORDER.getdefaultCode());
		opreqheader.setXstatus("Open");
		opreqheader.setXstatusreq("Open");
		opreqheader.setXamount(BigDecimal.ZERO);

		return opreqheader;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Opreqheader opreqHeader, BindingResult bindingResult) {
		if (opreqHeader == null || StringUtils.isBlank(opreqHeader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate
		if (StringUtils.isBlank(opreqHeader.getXcus())) {
			responseHelper.setErrorStatusAndMessage("Please select a customer to create Sales Order");
			return responseHelper.getResponse();
		}
		
		opreqHeader.setXstatusreq("Open");
		opreqHeader.setXwh("01");

		// if existing record
		Opreqheader existOpreqHeader = opreqService.findOpreqHeaderByXdoreqnum(opreqHeader.getXdoreqnum());
		if (existOpreqHeader != null) {
			BeanUtils.copyProperties(opreqHeader, existOpreqHeader, "xdoreqnum", "xtypetrn", "xtrn");
			long count = opreqService.updateOpreqheader(existOpreqHeader);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Sales Requisition updated successfully");
			responseHelper.setRedirectUrl("/salesninvoice/opreq/" + opreqHeader.getXdoreqnum());
			return responseHelper.getResponse();
		}

		// If new
		long count = opreqService.saveOpreqheader(opreqHeader);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Sales Requisition order created successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opreq/" + opreqHeader.getXdoreqnum());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xdoreqnum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xdoreqnum){
		Opreqheader opreqheader = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
		if(opreqheader == null) {
			responseHelper.setErrorStatusAndMessage("Can't find sales requisition : " + xdoreqnum);
			return responseHelper.getResponse();
		}

		long count = opreqService.deleteOpreqheader(xdoreqnum);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't find sales requisition : " + xdoreqnum);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Sales requisition deleted successfully");
		responseHelper.setRedirectUrl("/salesninvoice/opreq/");
		return responseHelper.getResponse();
	}

	
	@GetMapping("/{xdoreqnum}/opreqdetail/{xrow}/show")
	public String openOpordDetailModal(@PathVariable String xdoreqnum, @PathVariable String xrow, Model model) {

		if ("new".equalsIgnoreCase(xrow)) {
			Opreqdetail opreqdetails = new Opreqdetail();
			opreqdetails.setXdoreqnum(xdoreqnum);
			opreqdetails.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			opreqdetails.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			model.addAttribute("opreqdetail", opreqdetails);
		} else {
			Opreqdetail opreqdetails = opreqService.findOpreqdetailByXordernumAndXrow(xdoreqnum, Integer.parseInt(xrow));
			if (opreqdetails == null) {
				opreqdetails = new Opreqdetail();
				opreqdetails.setXdoreqnum(xdoreqnum);
				opreqdetails.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
				opreqdetails.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("opreqdetail", opreqdetails);
		}
		return "pages/salesninvoice/reqorder/opreqdetailmodal::opreqdetailmodal";
	}
	
	@PostMapping("/opreqdetail/save")
	public @ResponseBody Map<String, Object> saveOporddetail(Opreqdetail opreqdetail, BindingResult bindingResult) {
		if (opreqdetail == null || StringUtils.isBlank(opreqdetail.getXdoreqnum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		if(StringUtils.isBlank(opreqdetail.getXitem())) {
			responseHelper.setErrorStatusAndMessage("Item not selected! Please select an item");
			return responseHelper.getResponse();
		}

		Caitem caitem = caitemService.findByXitem(opreqdetail.getXitem());
		if(caitem == null) {
			responseHelper.setErrorStatusAndMessage("Item not found in the system");
			return responseHelper.getResponse();
		}

		if(opreqdetail.getXqtyord() == null || opreqdetail.getXqtyord().compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Item quantity can't be less then zero");
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if (opreqdetail.getXrow() == 0 && opreqService.findOpreqdetailByXdoreqnumAndXitem(opreqdetail.getXdoreqnum(), opreqdetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		// modify line amount
		opreqdetail.setXdesc(caitem.getXdesc());
		opreqdetail.setXcatitem(caitem.getXcatitem());
		opreqdetail.setXgitem(caitem.getXgitem());
		opreqdetail.setXlineamt(opreqdetail.getXqtyord().multiply(opreqdetail.getXrate()).setScale(2, RoundingMode.DOWN));

		// if existing
		Opreqdetail existDetail = opreqService.findOpreqdetailByXordernumAndXrow(opreqdetail.getXdoreqnum(), opreqdetail.getXrow());
		if (existDetail != null) {
			BeanUtils.copyProperties(opreqdetail, existDetail, "xdoreqnum", "xrow");
			long count = opreqService.updateOpreqdetail(existDetail);
			if (count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setReloadSectionIdWithUrl("opreqdetailtable", "/salesninvoice/opreq/opreqdetail/" + opreqdetail.getXdoreqnum());
			//responseHelper.setSecondReloadSectionIdWithUrl("opreqheaderform", "/salesninvoice/opreq/opreqheaderform/" + opreqDetail.getXdoreqnum());
			responseHelper.setSuccessStatusAndMessage("Sales Order item updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = opreqService.saveOpreqdetail(opreqdetail);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setReloadSectionIdWithUrl("opreqdetailtable", "/salesninvoice/opreq/opreqdetail/" + opreqdetail.getXdoreqnum());
		//responseHelper.setSecondReloadSectionIdWithUrl("opreqheaderform", "/salesninvoice/opreq/opreqheaderform/" + opreqDetail.getXdoreqnum());
		responseHelper.setSuccessStatusAndMessage("Sales Order item saved successfully");
		return responseHelper.getResponse();
	}
	
	 
	@GetMapping("/opreqdetail/{xdoreqnum}")
	public String reloadOpordDetailTable(@PathVariable String xdoreqnum, Model model) {
		model.addAttribute("opreqDetailsList", opreqService.findOpreqDetailByXdoreqnum(xdoreqnum));
		return "pages/salesninvoice/reqorder/opreqdetailmodal::opreqdetailmodal";
	}

//	@GetMapping("/opreqheaderform/{xdoreqnum}")
//	public String reloadOpdoheaderform(@PathVariable String xdoreqnum, Model model) {
//		Opreqheader data = opreqService.findOpreqHeaderByXdoreqnum(xdoreqnum);
//		if (data == null) return "redirect:/salesninvoice/opord";
//
//		model.addAttribute("opreqheader", data);
//		model.addAttribute("opreqprefix", xtrnService.findByXtypetrn(TransactionCodeType.SALES_REQUESTION_ORDER.getCode(), Boolean.TRUE));
//
//		return "pages/salesninvoice/reqorder/reqorder::opreqheaderform";
//	}
	
	@PostMapping("{xdoreqnum}/opreqdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteOpreqDetail(@PathVariable String xdoreqnum, @PathVariable String xrow, Model model) {
		Opreqdetail pd = opreqService.findOpreqdetailByXordernumAndXrow(xdoreqnum, Integer.parseInt(xrow));
		if (pd == null) {
			responseHelper.setErrorStatusAndMessage("Detail item can't found to do delete");
			return responseHelper.getResponse();
		}

		long count = opreqService.deleteOpreqdetail(pd);
		if (count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("opreqdetailtable", "/salesninvoice/opreq/opreqdetail/" + xdoreqnum);
		//responseHelper.setSecondReloadSectionIdWithUrl("opreqheaderform", "/salesninvoice/opreq/opreqheaderform/" + xdoreqnum);
		return responseHelper.getResponse();
	}

	
	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}

}
