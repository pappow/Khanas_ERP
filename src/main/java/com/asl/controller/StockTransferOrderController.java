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

import com.asl.entity.ImtorDetail;
import com.asl.entity.ImtorHeader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.ImtorService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/inventory/transferorder")
public class StockTransferOrderController extends ASLAbstractController {
	
	@Autowired private ImtorService imtorService;
	@Autowired private XcodesService xcodeService;
	@Autowired private XtrnService xtrnService;

	@GetMapping
	public String loadTransferOrderdPage(Model model) {
		model.addAttribute("imtorheader", getDefaultImtorHeader());
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeader());
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		//model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		
		return "pages/inventory/transferorder/imtor";
	}

	@GetMapping("/{xtornum}")
	public String loadPoordPage(@PathVariable String xtornum, Model model) {
		ImtorHeader data = imtorService.findImtorHeaderByXtornum(xtornum); 
		if(data == null) data = getDefaultImtorHeader();

		model.addAttribute("imtorheader", data);
		model.addAttribute("allImtorHeaders", imtorService.getAllImtorHeader());
		model.addAttribute("imtorprefix", xtrnService.findByXtypetrn(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode()));
		model.addAttribute("warehouses", xcodeService.findByXtype(CodeType.WAREHOUSE.getCode()));
		model.addAttribute("imtordetailsList", imtorService.findImtorDetailByXtornum(xtornum));
		return "pages/inventory/transferorder/imtor";
	}

	private ImtorHeader getDefaultImtorHeader() {
		ImtorHeader imtorHeader = new ImtorHeader();
		//imtorHeader.setXtype(TransactionCodeType.PURCHASE_ORDER.getCode());
		//imtorHeader.setXtotamt(BigDecimal.ZERO);
		return imtorHeader;
	}
	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(ImtorHeader imtorHeader, BindingResult bindingResult){
		if((imtorHeader == null) || StringUtils.isBlank(imtorHeader.getXtrntor()) && StringUtils.isBlank(imtorHeader.getXtornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate
		
		imtorHeader.setXtype(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getCode());
		imtorHeader.setXtrntor(TransactionCodeType.INVENTORY_TRANSFER_ORDER.getdefaultCode());

		// if existing record
		ImtorHeader existImtorHeader = imtorService.findImtorHeaderByXtornum(imtorHeader.getXtornum());
		if(existImtorHeader != null) {
			BeanUtils.copyProperties(imtorHeader, existImtorHeader, "xtornum", "xdate");
			long count = imtorService.update(existImtorHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Transfer Order updated successfully");
			responseHelper.setRedirectUrl("/inventory/transferorder/" + imtorHeader.getXtornum());
			return responseHelper.getResponse();
		}

		// If new
		long count = imtorService.save(imtorHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Purchase Order created successfully");
		responseHelper.setRedirectUrl("/inventory/transferorder/" + imtorHeader.getXtornum());
		return responseHelper.getResponse();
	}
	/*
	@PostMapping("/archive/{xpornum}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, true);
	}

	@PostMapping("/restore/{xpornum}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xpornum){
		return doArchiveOrRestore(xpornum, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xpornum, boolean archive){
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader == null || "GRN Created".equalsIgnoreCase(poordHeader.getXstatuspor())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		poordHeader.setZactive(archive ? Boolean.FALSE : Boolean.TRUE);
		long count = poordService.update(poordHeader);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Purchase order updated successfully");
		responseHelper.setRedirectUrl("/purchasing/poord/" + poordHeader.getXpornum());
		return responseHelper.getResponse();
	}
	*/
	
	@GetMapping("{xtornum}/imtordetail/{xrow}/show")
	public String openImtorDetailModal(@PathVariable String xtornum, @PathVariable String xrow, Model model) {

		//model.addAttribute("purchaseUnit", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode()));

		if("new".equalsIgnoreCase(xrow)) {
			ImtorDetail imtordetail = new ImtorDetail();
			imtordetail.setXtornum(xtornum);
			imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			model.addAttribute("imtordetail", imtordetail);
		} else {
			ImtorDetail imtordetail = imtorService.findImtorDetailByXtornumAndXrow(xtornum, Integer.parseInt(xrow));
			if(imtordetail == null) {
				imtordetail = new ImtorDetail();
				imtordetail.setXtornum(xtornum);
				imtordetail.setXqtyord(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
				imtordetail.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
			}
			model.addAttribute("imtordetail", imtordetail);
		}

		return "pages/inventory/transferorder/imtordetailmodal::imtordetailmodal";
	}
	

	@PostMapping("/imtordetail/save")
	public @ResponseBody Map<String, Object> savePoorddetail(ImtorDetail imtorDetail){
		if(imtorDetail == null || StringUtils.isBlank(imtorDetail.getXtornum())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Check item already exist in detail list
		if(imtorDetail.getXrow() == 0 && imtorService.findImtorDetailByXtornumAndXitem(imtorDetail.getXtornum(), imtorDetail.getXitem()) != null) {
			responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
			return responseHelper.getResponse();
		}

		
		// if existing
		ImtorDetail existDetail = imtorService.findImtorDetailByXtornumAndXrow(imtorDetail.getXtornum(), imtorDetail.getXrow());
		if(existDetail != null) {
			BeanUtils.copyProperties(imtorDetail, existDetail, "xtornum", "xrow");
			long count = imtorService.updateDetail(existDetail);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/inventory/transferorder/" +  imtorDetail.getXtornum());
			responseHelper.setSuccessStatusAndMessage("Transfer Order detail updated successfully");
			return responseHelper.getResponse();
		}

		// if new detail
		long count = imtorService.saveDetail(imtorDetail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setRedirectUrl("/inventory/transferorder/" +  imtorDetail.getXtornum());
		responseHelper.setSuccessStatusAndMessage("Order detail saved successfully");
		return responseHelper.getResponse();
	}

	@GetMapping("/imtordetail/{xtornum}")
	public String reloadImtorDetailTabble(@PathVariable String xtornum, Model model) {
		List<ImtorDetail> detailList = imtorService.findImtorDetailByXtornum(xtornum);
		model.addAttribute("imtordetailsList", detailList);
		ImtorHeader header = new ImtorHeader();
		header.setXtornum(xtornum);
		model.addAttribute("imtorheader", header);
		return "pages/inventory/transferorder/imtor::imtordetailtable";
	}
	
	/*
	@PostMapping("{xpornum}/poorddetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xpornum, @PathVariable String xrow, Model model) {
		PoordDetail pd = poordService.findPoorddetailByXportNumAndXrow(xpornum, Integer.parseInt(xrow));
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
		responseHelper.setRedirectUrl("/purchasing/poord/" +  xpornum);
		return responseHelper.getResponse();
	}
	
	
	@GetMapping("/creategrn/{xpornum}")
	public @ResponseBody Map<String, Object> creategrn(@PathVariable String xpornum){
		if(StringUtils.isBlank(xpornum)) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		// Validate

		// Get PoordHeader record by Xpornum
		PoordHeader poordHeader = poordService.findPoordHeaderByXpornum(xpornum);
		if(poordHeader != null) {
			PogrnHeader pogrnHeader = new PogrnHeader();
			BeanUtils.copyProperties(poordHeader, pogrnHeader, "xdate", "xtype", "xtrngrn", "xnote");
			pogrnHeader.setXdate(new Date());
			pogrnHeader.setXtype(TransactionCodeType.PO_GRN_NUMBER.getCode());
			pogrnHeader.setXtrngrn(TransactionCodeType.PO_GRN_NUMBER.getdefaultCode());
			
			long count = pogrnService.save(pogrnHeader);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			
			pogrnHeader = pogrnService.findPogrnHeaderByXpornum(xpornum);
			//Get PO items to copy them in GRN.
			List<PoordDetail> poordDetailList = poordService.findPoorddetailByXpornum(xpornum);
			PogrnDetail pogrnDetail;
			for(int i=0; i< poordDetailList.size(); i++) {
				pogrnDetail = new PogrnDetail();
				//Copying PO items to GRN items.
				BeanUtils.copyProperties(poordDetailList.get(i), pogrnDetail, "xrow", "xnote");
				pogrnDetail.setXgrnnum(pogrnHeader.getXgrnnum());
				pogrnDetail.setXqtygrn(poordDetailList.get(i).getXqtyord());
				long nCount = pogrnService.saveDetail(pogrnDetail);
				
				// Update Inventory				
				if(nCount == 0) {
					responseHelper.setStatus(ResponseStatus.ERROR);
					return responseHelper.getResponse();
				}				
			}
			
			//Update PoordHeader Status
			poordHeader.setXstatuspor("GRN Created");
			long pCount = poordService.update(poordHeader);
			if(pCount == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}			
			 
			responseHelper.setSuccessStatusAndMessage("GRN created successfully");
			responseHelper.setRedirectUrl("/purchasing/poord/" + poordHeader.getXpornum());
			return responseHelper.getResponse();
		}	
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}
*/
}
