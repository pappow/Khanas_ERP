package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.asl.entity.Bmbomheader;
import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.entity.Oporddetail;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.BmbomService;
import com.asl.service.MoService;
import com.asl.service.OpordService;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Controller
@RequestMapping("/production/batch")
public class ProductionProcessBatchController extends ASLAbstractController {

	@Autowired private BmbomService bmbomService;
	@Autowired private OpordService opordService;
	@Autowired private MoService moService;

	@GetMapping
	public String loadProductionBatchPage(Model model) {
		return "redirect:/production/batch/chalantobatch/new/new";
	}

	@GetMapping("/{xbatch}")
	public String loadProductionBatchPage(@PathVariable String xbatch, Model model) {
		Moheader mh = moService.findMoHeaderByXbatch(xbatch);
		return "redirect:/production/batch/chalantobatch/" + mh.getXchalan() + "/" + mh.getXitem();
	}

	private Moheader getDefaultMoHeader() {
		Moheader mh = new Moheader();
		mh.setXtypetrn(TransactionCodeType.BATCH_NUMBER.getCode());
		mh.setXtrn(TransactionCodeType.BATCH_NUMBER.getdefaultCode());
		mh.setXstatusmor("Open");
		return mh;
	}

	@GetMapping("/chalantobatch/{xordernum}/{xitem}")
	public String loadProductionBatchPage(@PathVariable String xordernum, @PathVariable String xitem, Model model) {
		Moheader mh = moService.findMoheaderByXchalanAndXitem(xordernum, xitem);
		if(mh == null) {
			mh = getDefaultMoHeader();
			mh.setXchalan(xordernum);
			mh.setXitem(xitem);

			// Search bom for this item
			Bmbomheader bh = bmbomService.findBmBomHeaderByXitem(xitem);
			if(bh != null) mh.setXbomkey(bh.getXbomkey()); 
		}

		// Search item Product Wise Quantity
		Oporddetail chalanItem = opordService.findOporddetailByXordernumAndXitem(xordernum, xitem);
		if(chalanItem != null) {
			mh.setXqtyprd(chalanItem.getXqtyord());
			mh.setXqtyprdunit(chalanItem.getXunit());
		}

		model.addAttribute("batch", mh);
		model.addAttribute("batchPrefix", xtrnService.findByXtypetrn(TransactionCodeType.BATCH_NUMBER.getCode(), Boolean.TRUE));
		model.addAttribute("batchList", moService.getAllMoheader());

		List<Modetail> batchDetails = moService.findModetailByXbatch(mh.getXbatch());
		Modetail defaultModetail = batchDetails.stream().filter(bd -> "Default".equals(bd.getXtype())).collect(Collectors.toList()).stream().findFirst().orElse(null);
		model.addAttribute("batchDetails", batchDetails);
		model.addAttribute("defaultBatchDetail", defaultModetail);
		return "pages/production/batch/batch";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> save(Moheader moheader, BindingResult bindingResult, Model model){
		if(moheader == null || StringUtils.isBlank(moheader.getXtypetrn())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// if existing
		Moheader existMh = moService.findMoHeaderByXbatch(moheader.getXbatch());
		if(existMh != null) {
			BeanUtils.copyProperties(moheader, existMh, "xbatch");
			long count = moService.updateMoHeader(existMh);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Batch not exist to do update");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Batch Updated successfully");
			responseHelper.setRedirectUrl("/production/batch/" + existMh.getXbatch());
			return responseHelper.getResponse();
		}

		// If new
		long count = moService.saveMoHeader(moheader);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't create batch");
			return responseHelper.getResponse();
		}
		responseHelper.setSuccessStatusAndMessage("Batch Updated successfully");
		responseHelper.setRedirectUrl("/production/batch/chalantobatch/" + moheader.getXchalan() + "/" + moheader.getXitem());
		return responseHelper.getResponse();
	}

	@PostMapping("/explodebom/{xbatch}")
	public @ResponseBody Map<String, Object> expload(@PathVariable String xbatch, Model model){
		String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);

		// call bom expload proc
		bmbomService.explodeBom(xbatch, "Explode", errorCode);
		String em = getProcedureErrorMessages(errorCode);
		if(StringUtils.isNotBlank(em)) {
			responseHelper.setErrorStatusAndMessage(em);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("BOM Expload successfully");
		responseHelper.setRedirectUrl("/production/batch/" + xbatch);
		return responseHelper.getResponse();
	}

	@GetMapping("{xbatch}/batchdetail/{xrow}/show")
	public String openBatchDetailModal(@PathVariable String xbatch, @PathVariable String xrow, Model model) {

		Modetail mod = null;

		if("new".equalsIgnoreCase(xrow)) {
			mod = new Modetail();
			mod.setXbatch(xbatch);
			mod.setXqtyreq(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
		} else {
			mod = moService.findModetailByXrowAndXbatch(Integer.parseInt(xrow), xbatch);
			if(mod == null) {
				mod = new Modetail();
				mod.setXbatch(xbatch);
				mod.setXqtyreq(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
			}
		}

		model.addAttribute("batchdetail", mod);
		return "pages/production/batch/batchdetailmodal::batchdetailmodal";
	}

	@PostMapping("/batchdetail/save")
	public @ResponseBody Map<String, Object> saveBatchDetail(Modetail modetail, Model model){
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// check item already exist in detail list
		if(modetail.getXrow() == 0) {
			Modetail dupe = moService.findModetailByXbatchAndXitem(modetail.getXbatch(), modetail.getXitem());
			if(dupe != null && !"Default".equalsIgnoreCase(dupe.getXtype())) {
				responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
				return responseHelper.getResponse();
			}
		}

		// if existing
		Modetail md = moService.findModetailByXrowAndXbatch(modetail.getXrow(), modetail.getXbatch());
		if(md != null) {
			BeanUtils.copyProperties(modetail, md);
			long count = moService.updateMoDetail(modetail);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Cant update item");
				return responseHelper.getResponse();
			}
			responseHelper.setRedirectUrl("/production/batch/" + modetail.getXbatch());
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		long count = moService.saveMoDetail(modetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Cant save item");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/production/batch/" + modetail.getXbatch());
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/processproduction/{xbatch}")
	public @ResponseBody Map<String, Object> processProduction(@PathVariable String xbatch, Model model){

		String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);

		// call bom expload proc
		moService.processProduction(xbatch, "Process", errorCode);
		String em = getProcedureErrorMessages(errorCode);
		if(StringUtils.isNotBlank(em)) {
			responseHelper.setErrorStatusAndMessage(em);
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Production Processed successfully");
		responseHelper.setRedirectUrl("/production/batch/" + xbatch);
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{xbatch}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String xbatch, Model model){
		Moheader mh = moService.findMoHeaderByXbatch(xbatch);
		if(mh == null) {
			responseHelper.setErrorStatusAndMessage("Batch not found to do archive");
			return responseHelper.getResponse();
		}

		mh.setZactive(false);
		long count = moService.updateMoHeader(mh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Batch not deleted");
			return responseHelper.getResponse();
		}

		responseHelper.setRedirectUrl("/production/batch");
		responseHelper.setSuccessStatusAndMessage("Batch archived successfully");
		return responseHelper.getResponse();
	}
}
