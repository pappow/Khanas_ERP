package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Bmbomheader;
import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.service.BmbomService;
import com.asl.service.MoService;
import com.asl.service.OpordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Slf4j
@Controller
@RequestMapping("/production/batch")
public class ProductionBatchController extends ASLAbstractController {

	@Autowired private BmbomService bmbomService;
	@Autowired private OpordService opordService;
	@Autowired private MoService moService;

	@GetMapping
	public String loadChalanBatchPage(Model model) {
		model.addAttribute("chalan", new Opordheader());
		return "pages/production/batch/batch";
	}

	@GetMapping("/{chalan}")
	public String loadChalanBatchPageWithChalan(@PathVariable String chalan, Model model) {
		Opordheader ch = opordService.findOpordHeaderByXordernum(chalan);
		if(ch == null) ch = new Opordheader();

		model.addAttribute("chalan", ch);
		return "pages/production/batch/batch";
	}

	@PostMapping("/search/chalan")
	public @ResponseBody Map<String, Object> searchAndLoadChalan(String chalanNumber, Model model) {
		Opordheader chalan = opordService.findOpordHeaderByXordernum(chalanNumber);
		if(chalan == null || "Open".equals(chalan.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Chalan not found");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" + chalanNumber);
		responseHelper.setSuccessStatusAndMessage("Search successfull");
		return responseHelper.getResponse();
	}

	@GetMapping("/chalantobatch/{xordernum}")
	public String loadChalanBatch(@PathVariable String xordernum, Model model) {
		// get chalan details
		Opordheader chalan = opordService.findOpordHeaderByXordernum(xordernum);
		if(chalan == null) return "redirect:/production/batch";

		// if batch not created, then create batch first
		List<Oporddetail> chalanItems = opordService.findOporddetailByXordernum(xordernum);
		if(!chalan.isBatchcreated()) {
			// get chalan details
			if(chalanItems == null || chalanItems.isEmpty()) return "redirect:/production/batch";

			// create batch for each item details;
			List<Moheader> batcList = new ArrayList<>();
			for(Oporddetail item : chalanItems) {
				Bmbomheader bom = bmbomService.findBmBomHeaderByXitem(item.getXitem());

				Moheader batch = new Moheader();
				batch.setXtypetrn(TransactionCodeType.BATCH_NUMBER.getCode());
				batch.setXtrn(TransactionCodeType.BATCH_NUMBER.getdefaultCode());
				batch.setXchalan(chalan.getXordernum());
				batch.setXitem(item.getXitem());
				batch.setXdesc(item.getXdesc());
				batch.setXbomkey(bom != null ? bom.getXbomkey() : "");
				batch.setXdate(new Date());
				batch.setXqtyprd(item.getXqtyord());
				batch.setXqtycom(BigDecimal.ZERO);
				batch.setXstatusmor("Open");
				batcList.add(batch);
			}

			long count = moService.saveBatchMoHeader(batcList);
			if(count == 0) return "redirect:/production/batch";

			// Update chalan now with batch created flag
			chalan.setBatchcreated(true);
			long chalanUpdatecount = opordService.updateOpordHeader(chalan);
			if(chalanUpdatecount == 0) return "redirect:/production/batch";
		}

		// if batch already created, then retreive all chalan details
		// get all batch which is recently or previously created and make default item list using batch number and procedure
		List<Moheader> allBatches = new ArrayList<>();
		for(Oporddetail item : chalanItems) {
			Moheader batch = moService.findMoheaderByXchalanAndXitem(chalan.getXordernum(), item.getXitem());
			if(batch != null) allBatches.add(batch);
			List<Modetail> details = moService.findModetailByXbatch(batch.getXbatch());
			if(details != null && !details.isEmpty()) {
				batch.getModetails().addAll(details);
			}
		}

		allBatches.sort(Comparator.comparing(Moheader::getXbatch).reversed());
		model.addAttribute("batchList", allBatches);
		model.addAttribute("chalan", chalan);
		return "pages/production/batch/batch::batchdetailtable";
	}

	@PostMapping("/update/xqtycom/{xbatch}")
	public @ResponseBody Map<String, Object> updateBatchCompletedQty(@PathVariable String xbatch, String xqtycom, Model model){
		// Quantity validation
		if(StringUtils.isBlank(xqtycom)) {
			responseHelper.setErrorStatusAndMessage("Please enter valid completed quantity");
			return responseHelper.getResponse();
		}

		double qty = 0;
		try {
			qty = Double.parseDouble(xqtycom);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage("Please enter valid completed quantity");
			return responseHelper.getResponse();
		}

		if(BigDecimal.valueOf(qty).compareTo(BigDecimal.ZERO) == -1 || BigDecimal.valueOf(qty).compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("Please enter valid completed quantity");
			return responseHelper.getResponse();
		}

		// Now find batch first
		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) {
			responseHelper.setErrorStatusAndMessage("Batch " + xbatch + " not found in this system");
			return responseHelper.getResponse();
		}

		// Expload bom now
		if(StringUtils.isNotBlank(batch.getXbomkey())) {
			String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			bmbomService.explodeBom(batch.getXbatch(), "Explode", errorCode);
			String em = getProcedureErrorMessages(errorCode);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage("Can't expload bom for this Batch " + xbatch);
				return responseHelper.getResponse();
			}

			// set bom exploaded flag to batch
			batch.setBomexploaded(true);
		}

		// Update batch now
		batch.setXqtycom(BigDecimal.valueOf(qty).setScale(2, RoundingMode.DOWN));
		long count = moService.updateMoHeader(batch);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update batch : " + xbatch);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" + batch.getXchalan());
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@GetMapping("/batchdetail/{xbatch}/default/show")
	public String openDefaultBatchDetailModal(@PathVariable String xbatch, Model model) {
		Modetail defaultBatchDetail = moService.findDefaultModetailByXbatch(xbatch);
		if(defaultBatchDetail == null) defaultBatchDetail = new Modetail();

		model.addAttribute("batchdetail", defaultBatchDetail);
		return "pages/production/batch/defaultbatchdetailmodal::defaultbatchdetailmodal";
	}

	@GetMapping("/batchdetail/{xbatch}/others/show")
	public String openOthersBatchDetailModal(@PathVariable String xbatch, Model model) {
		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) return "redirect:/production/batch";

		List<Modetail> batchdetails = moService.findModetailByXbatch(xbatch);
		if(batchdetails == null || batchdetails.isEmpty()) batchdetails = new ArrayList<>();

		model.addAttribute("batch", batch);
		model.addAttribute("xbatch", xbatch);
		model.addAttribute("batchdetails", batchdetails);
		return "pages/production/batch/othersbatchdetailmodal::othersbatchdetailmodal";
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
			responseHelper.setTriggerModalUrl("othersbatchdetailmodal", "/production/batch/batchdetail/"+ modetail.getXbatch() +"/others/show");
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		long count = moService.saveMoDetail(modetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Cant save item");
			return responseHelper.getResponse();
		}

		responseHelper.setTriggerModalUrl("othersbatchdetailmodal", "/production/batch/batchdetail/"+ modetail.getXbatch() +"/others/show");
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("{xbatch}/batchdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteBatchDetail(@PathVariable String xbatch, @PathVariable String xrow, Model model){
		Modetail detail = moService.findModetailByXrowAndXbatch(Integer.parseInt(xrow), xbatch);
		if(detail == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		long count = moService.deleteModetail(detail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		responseHelper.setTriggerModalUrl("othersbatchdetailmodal", "/production/batch/batchdetail/"+ detail.getXbatch() +"/others/show");
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("/processproduction/{xbatch}")
	public @ResponseBody Map<String, Object> processProduction(@PathVariable String xbatch, Model model){
		Moheader mh = moService.findMoHeaderByXbatch(xbatch);
		if(mh == null || !"Open".equals(mh.getXstatusmor())) {
			responseHelper.setErrorStatusAndMessage("Can't process this batch : " + xbatch);
			return responseHelper.getResponse();
		}

		String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);

		// call bom expload proc
		moService.processProduction(xbatch, "Process", errorCode);
		String em = getProcedureErrorMessages(errorCode);
		if(StringUtils.isNotBlank(em)) {
			responseHelper.setErrorStatusAndMessage(em);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" + mh.getXchalan());
		responseHelper.setSuccessStatusAndMessage("Process successfull");
		return responseHelper.getResponse();
	}

//	@GetMapping
//	public String loadProductionBatchPage(Model model) {
//		return "redirect:/production/batch/chalantobatch/new/new";
//	}
//
//	@GetMapping("/{xbatch}")
//	public String loadProductionBatchPage(@PathVariable String xbatch, Model model) {
//		Moheader mh = moService.findMoHeaderByXbatch(xbatch);
//		return "redirect:/production/batch/chalantobatch/" + mh.getXchalan() + "/" + mh.getXitem();
//	}
//
//	private Moheader getDefaultMoHeader() {
//		Moheader mh = new Moheader();
//		mh.setXtypetrn(TransactionCodeType.BATCH_NUMBER.getCode());
//		mh.setXtrn(TransactionCodeType.BATCH_NUMBER.getdefaultCode());
//		mh.setXstatusmor("Open");
//		return mh;
//	}
//
//	@GetMapping("/chalantobatch/{xordernum}/{xitem}")
//	public String loadProductionBatchPage(@PathVariable String xordernum, @PathVariable String xitem, Model model) {
//		Moheader mh = moService.findMoheaderByXchalanAndXitem(xordernum, xitem);
//		if(mh == null) {
//			mh = getDefaultMoHeader();
//			mh.setXchalan(xordernum);
//			mh.setXitem(xitem);
//
//			// Search bom for this item
//			Bmbomheader bh = bmbomService.findBmBomHeaderByXitem(xitem);
//			if(bh != null) mh.setXbomkey(bh.getXbomkey()); 
//		}
//
//		// Search item Product Wise Quantity
//		Oporddetail chalanItem = opordService.findOporddetailByXordernumAndXitem(xordernum, xitem);
//		if(chalanItem != null) {
//			mh.setXqtyprd(chalanItem.getXqtyord());
//			mh.setXqtyprdunit(chalanItem.getXunit());
//		}
//
//		model.addAttribute("batch", mh);
//		model.addAttribute("batchPrefix", xtrnService.findByXtypetrn(TransactionCodeType.BATCH_NUMBER.getCode(), Boolean.TRUE));
//		model.addAttribute("batchList", moService.getAllMoheader());
//
//		List<Modetail> batchDetails = moService.findModetailByXbatch(mh.getXbatch());
//		Modetail defaultModetail = batchDetails.stream().filter(bd -> "Default".equals(bd.getXtype())).collect(Collectors.toList()).stream().findFirst().orElse(null);
//		model.addAttribute("batchDetails", batchDetails);
//		model.addAttribute("defaultBatchDetail", defaultModetail);
//		return "pages/production/batch/batch";
//	}
//
//	@PostMapping("/save")
//	public @ResponseBody Map<String, Object> save(Moheader moheader, BindingResult bindingResult, Model model){
//		if(moheader == null || StringUtils.isBlank(moheader.getXtypetrn())) {
//			responseHelper.setStatus(ResponseStatus.ERROR);
//			return responseHelper.getResponse();
//		}
//
//		// if existing
//		Moheader existMh = moService.findMoHeaderByXbatch(moheader.getXbatch());
//		if(existMh != null) {
//			BeanUtils.copyProperties(moheader, existMh, "xbatch");
//			long count = moService.updateMoHeader(existMh);
//			if(count == 0) {
//				responseHelper.setErrorStatusAndMessage("Batch not exist to do update");
//				return responseHelper.getResponse();
//			}
//			responseHelper.setSuccessStatusAndMessage("Batch Updated successfully");
//			responseHelper.setRedirectUrl("/production/batch/" + existMh.getXbatch());
//			return responseHelper.getResponse();
//		}
//
//		// If new
//		long count = moService.saveMoHeader(moheader);
//		if(count == 0) {
//			responseHelper.setErrorStatusAndMessage("Can't create batch");
//			return responseHelper.getResponse();
//		}
//		responseHelper.setSuccessStatusAndMessage("Batch Updated successfully");
//		responseHelper.setRedirectUrl("/production/batch/chalantobatch/" + moheader.getXchalan() + "/" + moheader.getXitem());
//		return responseHelper.getResponse();
//	}
//
//	@PostMapping("/explodebom/{xbatch}")
//	public @ResponseBody Map<String, Object> expload(@PathVariable String xbatch, Model model){
//		String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
//
//		// call bom expload proc
//		bmbomService.explodeBom(xbatch, "Explode", errorCode);
//		String em = getProcedureErrorMessages(errorCode);
//		if(StringUtils.isNotBlank(em)) {
//			responseHelper.setErrorStatusAndMessage(em);
//			return responseHelper.getResponse();
//		}
//
//		responseHelper.setSuccessStatusAndMessage("BOM Expload successfully");
//		responseHelper.setRedirectUrl("/production/batch/" + xbatch);
//		return responseHelper.getResponse();
//	}
//
//	@GetMapping("{xbatch}/batchdetail/{xrow}/show")
//	public String openBatchDetailModal(@PathVariable String xbatch, @PathVariable String xrow, Model model) {
//
//		Modetail mod = null;
//
//		if("new".equalsIgnoreCase(xrow)) {
//			mod = new Modetail();
//			mod.setXbatch(xbatch);
//			mod.setXqtyreq(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN));
//		} else {
//			mod = moService.findModetailByXrowAndXbatch(Integer.parseInt(xrow), xbatch);
//			if(mod == null) {
//				mod = new Modetail();
//				mod.setXbatch(xbatch);
//				mod.setXqtyreq(BigDecimal.ONE.setScale(2, RoundingMode.DOWN));
//			}
//		}
//
//		model.addAttribute("batchdetail", mod);
//		return "pages/production/batch/batchdetailmodal::batchdetailmodal";
//	}

//	@PostMapping("/batchdetail/save")
//	public @ResponseBody Map<String, Object> saveBatchDetail(Modetail modetail, Model model){
//		if(modetail == null || StringUtils.isBlank(modetail.getXbatch())) {
//			responseHelper.setStatus(ResponseStatus.ERROR);
//			return responseHelper.getResponse();
//		}
//
//		// check item already exist in detail list
//		if(modetail.getXrow() == 0) {
//			Modetail dupe = moService.findModetailByXbatchAndXitem(modetail.getXbatch(), modetail.getXitem());
//			if(dupe != null && !"Default".equalsIgnoreCase(dupe.getXtype())) {
//				responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
//				return responseHelper.getResponse();
//			}
//		}
//
//		// if existing
//		Modetail md = moService.findModetailByXrowAndXbatch(modetail.getXrow(), modetail.getXbatch());
//		if(md != null) {
//			BeanUtils.copyProperties(modetail, md);
//			long count = moService.updateMoDetail(modetail);
//			if(count == 0) {
//				responseHelper.setErrorStatusAndMessage("Cant update item");
//				return responseHelper.getResponse();
//			}
//			responseHelper.setRedirectUrl("/production/batch/" + modetail.getXbatch());
//			responseHelper.setSuccessStatusAndMessage("Saved successfully");
//			return responseHelper.getResponse();
//		}
//
//		long count = moService.saveMoDetail(modetail);
//		if(count == 0) {
//			responseHelper.setErrorStatusAndMessage("Cant save item");
//			return responseHelper.getResponse();
//		}
//
//		responseHelper.setRedirectUrl("/production/batch/" + modetail.getXbatch());
//		responseHelper.setSuccessStatusAndMessage("Saved successfully");
//		return responseHelper.getResponse();
//	}
//

//
//	@PostMapping("/archive/{xbatch}")
//	public @ResponseBody Map<String, Object> archive(@PathVariable String xbatch, Model model){
//		Moheader mh = moService.findMoHeaderByXbatch(xbatch);
//		if(mh == null) {
//			responseHelper.setErrorStatusAndMessage("Batch not found to do archive");
//			return responseHelper.getResponse();
//		}
//
//		mh.setZactive(false);
//		long count = moService.updateMoHeader(mh);
//		if(count == 0) {
//			responseHelper.setErrorStatusAndMessage("Batch not deleted");
//			return responseHelper.getResponse();
//		}
//
//		responseHelper.setRedirectUrl("/production/batch");
//		responseHelper.setSuccessStatusAndMessage("Batch archived successfully");
//		return responseHelper.getResponse();
//	}
}
