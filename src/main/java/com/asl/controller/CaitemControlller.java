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

import com.asl.entity.Caitem;
import com.asl.entity.Caitemdetail;
import com.asl.enums.CodeType;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.CaitemService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;

@Controller
@RequestMapping("/mastersetup/caitem")
public class CaitemControlller extends ASLAbstractController {

	@Autowired private XcodesService xcodeService;
	@Autowired private CaitemService caitemService;
	@Autowired private XtrnService xtrnService;

	@GetMapping
	public String loadCaitemPage(Model model) {
		model.addAttribute("caitem", getDefaultCaitem());
		model.addAttribute("itemxtrnnumbers", xtrnService.findByXtypetrn(TransactionCodeType.ITEM_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("xitemCategories", xcodeService.findByXtype(CodeType.ITEM_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("xitemGroups", xcodeService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("xunitPurs", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode(), Boolean.TRUE));
		model.addAttribute("xunitSels", xcodeService.findByXtype(CodeType.SELLING_UNIT.getCode(), Boolean.TRUE));
		model.addAttribute("landItemCategories", xcodeService.findByXtype(CodeType.LANDITEM_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("allCaitems", caitemService.getAllCaitems());
		
		if(isBoshila()) {
			return "pages/land/mastersetup/caitem";
		}
		else if(isGarments()) {
			return "pages/merchandising/caitem";
		}
		return "pages/mastersetup/caitem/caitem";
	}

	@GetMapping("/{xitem}")
	public String loadCaitemPage(@PathVariable String xitem, Model model) {
		Caitem data = caitemService.findByXitem(xitem); 
		if(data == null) data = getDefaultCaitem();

		model.addAttribute("caitem", data);
		model.addAttribute("itemxtrnnumbers", xtrnService.findByXtypetrn(TransactionCodeType.ITEM_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("xitemCategories", xcodeService.findByXtype(CodeType.ITEM_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("xitemGroups", xcodeService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE));
		model.addAttribute("xunitPurs", xcodeService.findByXtype(CodeType.PURCHASE_UNIT.getCode(), Boolean.TRUE));
		model.addAttribute("xunitSels", xcodeService.findByXtype(CodeType.SELLING_UNIT.getCode(), Boolean.TRUE));
		model.addAttribute("landItemCategories", xcodeService.findByXtype(CodeType.LANDITEM_CATEGORY.getCode(), Boolean.TRUE));
		model.addAttribute("allCaitems", caitemService.getAllCaitems());
		model.addAttribute("caitemdetails", caitemService.findCaitemdetailByXitem(xitem));
		if(isBoshila()) {
			data.setXmember("");
			return "pages/land/mastersetup/caitem";
		}
		else if(isGarments()) {
			data.setXmember("");
			return "pages/merchandising/caitem";
		}
		return "pages/mastersetup/caitem/caitem";
	}

	private Caitem getDefaultCaitem() {
		Caitem caitem = new Caitem();
		caitem.setXrate(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		caitem.setXminqty(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
		caitem.setXtype(TransactionCodeType.ITEM_NUMBER.getCode());
		caitem.setXseqn(Long.valueOf(999999));
		return caitem;
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Caitem caitem, BindingResult bindingResult){
		if(caitem == null || StringUtils.isBlank(caitem.getXtype())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Validate caitem
		caitem.setXunitsel(caitem.getXunitpur());
		caitem.setXmember(caitem.getXmember());
		// if existing record
		Caitem existCaitem = caitemService.findByXitem(caitem.getXitem());
		if(existCaitem != null) {
			BeanUtils.copyProperties(caitem, existCaitem, "xitem", "xtype");
			long count = caitemService.update(existCaitem);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setSuccessStatusAndMessage("Item Master updated successfully");
			responseHelper.setRedirectUrl("/mastersetup/caitem/" + caitem.getXitem());
			return responseHelper.getResponse();
		}

		// If new caitem
		long count = caitemService.save(caitem);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Item Master saved successfully");
		responseHelper.setRedirectUrl("/mastersetup/caitem/" + caitem.getXitem());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xitem}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xitem){
		return doArchiveOrRestore(xitem, true);
	}

	@PostMapping("/restore/{xitem}")
	public @ResponseBody Map<String, Object> restore(@PathVariable String xitem){
		return doArchiveOrRestore(xitem, false);
	}

	public Map<String, Object> doArchiveOrRestore(String xitem, boolean archive){
		Caitem caitem = caitemService.findByXitem(xitem);
		if(caitem == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = caitemService.deleteCaitem(xitem);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Item Master updated successfully");
		responseHelper.setRedirectUrl("/mastersetup/caitem/" + xitem);
		return responseHelper.getResponse();
	}

	@GetMapping("/{xitem}/caitemdetail/{xsubitem}/show")
	public String openCaitemDetailModal(@PathVariable String xitem, @PathVariable String xsubitem, Model model) {
		Caitemdetail caitemDetail = new Caitemdetail();
		caitemDetail.setXitem(xitem);

		if(!"new".equalsIgnoreCase(xsubitem)) {
			caitemDetail = caitemService.findCaitemdetailByXitemAndXsubitem(xitem, xsubitem);
		}

		model.addAttribute("caitemDetail", caitemDetail);
		return "pages/mastersetup/caitem/caitemdetailmodal::caitemdetailmodal";
	}

	@PostMapping("/caitemdetail/save")
	public @ResponseBody Map<String, Object> saveCaitemdetail(Caitemdetail caitemdetail){
		if(caitemdetail == null) {
			responseHelper.setErrorStatusAndMessage("Can't find details");
			return responseHelper.getResponse();
		}

		// validation
		if(StringUtils.isBlank(caitemdetail.getXsubitem())) {
			responseHelper.setErrorStatusAndMessage("Please select an sub item");
			return responseHelper.getResponse();
		}

		Caitemdetail exist = caitemService.findCaitemdetailByXitemAndXsubitem(caitemdetail.getXitem(), caitemdetail.getXsubitem());
		if(exist != null) {
			responseHelper.setErrorStatusAndMessage("Sub Item already exist");
			return responseHelper.getResponse();
		}

		long count = caitemService.saveCaitemdetail(caitemdetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't save subitem");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Subitem saved successfully");
		responseHelper.setReloadSectionIdWithUrl("caitemdetailtable", "/mastersetup/caitem/caitemdetail/" + caitemdetail.getXitem());
		return responseHelper.getResponse();
	}

	@GetMapping("/caitemdetail/{xitem}")
	public String loadItemDetailTable(@PathVariable String xitem, Model model) {
		model.addAttribute("caitem", caitemService.findByXitem(xitem));
		model.addAttribute("caitemdetails", caitemService.findCaitemdetailByXitem(xitem));
		return "pages/mastersetup/caitem/caitem::caitemdetailtable";
	}

	@PostMapping("{xitem}/caitemdetail/{xsubitem}/delete")
	public @ResponseBody Map<String, Object> deletePoordDetail(@PathVariable String xitem, @PathVariable String xsubitem, Model model) {
		Caitemdetail caitemdetail = caitemService.findCaitemdetailByXitemAndXsubitem(xitem, xsubitem);
		if(caitemdetail == null) {
			responseHelper.setErrorStatusAndMessage("Item detail not found");
			return responseHelper.getResponse();
		}

		long count = caitemService.deleteCaitemDetail(caitemdetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete subitem");
			return responseHelper.getResponse(); 
		}

		responseHelper.setSuccessStatusAndMessage("Subitem deleted successfully");
		responseHelper.setReloadSectionIdWithUrl("caitemdetailtable", "/mastersetup/caitem/caitemdetail/" + xitem);
		return responseHelper.getResponse();
	}

	@GetMapping("/centralitem/purunit/{xitem}")
	public @ResponseBody Caitem getCentralItemPurchaseUnit(@PathVariable String xitem) {
		return caitemService.findCentralItemByXitem(xitem);
	}

	@GetMapping("/purunit/{xitem}")
	public @ResponseBody String getItemPurchaseUnit(@PathVariable String xitem) {
		Caitem centralCaitem = caitemService.findByXitem(xitem);
		if(centralCaitem == null) return "";
		return centralCaitem.getXunitpur();
	}

	@GetMapping("/itemdetail/{xitem}")
	public @ResponseBody Caitem getCentralItemDetail(@PathVariable String xitem){
		return caitemService.findByXitem(xitem);
	}
}
