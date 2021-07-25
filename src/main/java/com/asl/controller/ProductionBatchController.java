package com.asl.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Bmbomdetail;
import com.asl.entity.Bmbomheader;
import com.asl.entity.Caitem;
import com.asl.entity.DailyProductionBatchDetail;
import com.asl.entity.Imstock;
import com.asl.entity.ImtorDetail;
import com.asl.entity.Imtrn;
import com.asl.entity.Modetail;
import com.asl.entity.Moheader;
import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.PSV;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProductionSuggestion;
import com.asl.model.report.DailyProductionBatchReport;
import com.asl.model.report.FinishedGood;
import com.asl.model.report.ProductionBatchReport;
import com.asl.model.report.RawMaterial;
import com.asl.service.BmbomService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.ImtorService;
import com.asl.service.ImtrnService;
import com.asl.service.MoService;
import com.asl.service.OpordService;
import com.asl.service.PSVService;
import com.asl.service.ProductionSuggestionService;

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
	@Autowired private PSVService psvService;
	@Autowired private ImstockService imstockService;
	@Autowired private ProductionSuggestionService productionSuggestionService;
	@Autowired private ImtrnService imtrnService;
	@Autowired private CaitemService caitemService;
	@Autowired private ImtorService imtorService;

	@GetMapping
	public String loadChalanBatchPage(Model model) {
		model.addAttribute("chalan", new Opordheader());
		model.addAttribute("productioncompleted", true);
		return "pages/production/batch/batch";
	}

	@GetMapping("/{chalan}")
	public String loadChalanBatchPageWithChalan(@PathVariable String chalan, Model model) {
		Opordheader ch = opordService.findOpordHeaderByXordernum(chalan);
		if(ch == null) ch = new Opordheader();

		model.addAttribute("chalan", ch);
		model.addAttribute("productioncompleted", moService.isProductionProcessCompleted(ch.getXordernum()));
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
				batch.setXqtyreq(item.getXqtyord());
				if(StringUtils.isBlank(batch.getXbomkey())) {
					batch.setXqtyprd(item.getXqtyord());
					batch.setXqtycom(item.getXqtyord());
				}

				batch.setXproduction(BigDecimal.ZERO);
				ProductionSuggestion ps = productionSuggestionService.getProductionSuggestionByXitemAndChalan(chalan.getXordernum(), item.getXitem());
				if(ps != null) {
					batch.setXproduction(ps.getXrawqty() != null ? ps.getXrawqty() : BigDecimal.ZERO);
				}

				batch.setXstatusmor("Open");
				batch.setXwh("02");
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

		allBatches.stream().forEach(a -> {
			if(StringUtils.isBlank(a.getXbomkey())) a.setXbomkey("");
		});
		allBatches.sort(Comparator.comparing(Moheader::getXbomkey).thenComparing(Moheader::getXbatch).reversed());

		model.addAttribute("batchList", allBatches);
		model.addAttribute("chalan", chalan);
		model.addAttribute("productioncompleted", moService.isProductionProcessCompleted(chalan.getXordernum()));

		boolean allBatchBomExploaded = true;
		List<Moheader> mhlist = moService.findMoheaderByXchalan(xordernum);
		for(Moheader m : mhlist) {
			if(!m.isBomexploaded()) {
				allBatchBomExploaded = false;
				break;
			}
		}
		model.addAttribute("allBatchBomExploaded", allBatchBomExploaded);
		return "pages/production/batch/batch::batchdetailtable";
	}

	@PostMapping("/update/xqtyprd/{xbatch}")
	public @ResponseBody Map<String, Object> updateBatchXqtyprd(@PathVariable String xbatch, BigDecimal xqtyprd, Model model){
		if(xqtyprd == null || xqtyprd.compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please enter valid default production quantity");
			return responseHelper.getResponse();
		}

		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) {
			responseHelper.setErrorStatusAndMessage("Batch not found");
			return responseHelper.getResponse();
		}

		batch.setXqtyprd(xqtyprd);
		long count = moService.updateMoHeader(batch);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update batch");
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" + batch.getXchalan());
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@PostMapping("/update/xqtycom/{xbatch}")
	public @ResponseBody Map<String, Object> updateBatchCompletedQty(@PathVariable String xbatch, BigDecimal xqtycom, Model model){
		if(xqtycom == null || xqtycom.compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please enter valid final production quantity");
			return responseHelper.getResponse();
		}

		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) {
			responseHelper.setErrorStatusAndMessage("Batch not found");
			return responseHelper.getResponse();
		}

		// Update batch now
		batch.setXqtycom(xqtycom.setScale(2, RoundingMode.DOWN));
		batch.setBomexploaded(true);
		long count = moService.updateMoHeader(batch);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update batch : " + xbatch);
			return responseHelper.getResponse();
		}

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" + batch.getXchalan());
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@PostMapping("/update/xproduction/{xbatch}")
	public @ResponseBody Map<String, Object> updateBatchXproduction(@PathVariable String xbatch, BigDecimal xproduction, Model model){
		if(xproduction == null || xproduction.compareTo(BigDecimal.ZERO) == -1) {
			responseHelper.setErrorStatusAndMessage("Please enter valid production quantity");
			return responseHelper.getResponse();
		}

		// find batch first
		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) {
			responseHelper.setErrorStatusAndMessage("Batch not found");
			return responseHelper.getResponse();
		}

		
		// find bom detail
		List<Bmbomdetail> bomdetails = bmbomService.findBmbomdetailByXbomkey(batch.getXbomkey());
		Bmbomdetail bd = bomdetails.stream().findFirst().orElse(null);

		// validate
		// add or update validation table
		if(StringUtils.isNotBlank(batch.getXbomkey()) && bd != null) {
			Imstock imstock = imstockService.findByXitemAndXwh(bd.getXbomcomp(), "02");
			BigDecimal usedRaw = psvService.getTotalRawUsedExceptCurrentBatch(batch.getXchalan(), bd.getXbomcomp(), xbatch);

			// Stock validation
			if(imstock.getXavail().subtract(usedRaw).compareTo(xproduction) == -1) {
				responseHelper.setErrorStatusAndMessage(bd.getXbomcomp() + " - stock not available. Already used : " + usedRaw + ", Available : " + imstock.getXavail().subtract(usedRaw));
				return responseHelper.getResponse();
			}

			PSV psv = psvService.findByXchalanAndXbatchAndXrawitem(batch.getXchalan(), xbatch, bd.getXbomcomp());
			if(psv == null) {
				// save new
				psv = new PSV();
				psv.setXchalan(batch.getXchalan());
				psv.setXbatch(xbatch);
				psv.setXrawitem(bd.getXbomcomp());
				psv.setXprod(xproduction);
				long scount = psvService.savePSV(psv);
				if(scount == 0) {
					responseHelper.setErrorStatusAndMessage("Stock qty validation not saved");
					return responseHelper.getResponse();
				}
			} else {
				// update existing
				psv.setXrawitem(bd.getXbomcomp());
				psv.setXprod(xproduction);
				long ucount = psvService.updatePSV(psv);
				if(ucount == 0) {
					responseHelper.setErrorStatusAndMessage("Stock qty validation not updated");
					return responseHelper.getResponse();
				}
			}
		}


		// now update batch with xproduction qty
		batch.setXproduction(xproduction);
		if(StringUtils.isBlank(batch.getXbomkey())) batch.setBomexploaded(true);
		long count = moService.updateMoHeader(batch);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update batch");
			return responseHelper.getResponse();
		}


		// if has BOM setting, then create default
		if(StringUtils.isNotBlank(batch.getXbomkey())) {
			if(bd != null) {
				// update previous modetail if exist 
				Modetail exisdet =  moService.findDefaultModetailByXbatch(batch.getXbatch());
				if(exisdet != null) {
					exisdet.setXitem(bd.getXbomcomp());
					exisdet.setXqtyreq(xproduction.multiply(BigDecimal.valueOf(1000)));
					exisdet.setXqtyactual(xproduction);
					long ucount = moService.updateMoDetail(exisdet);
					if(ucount == 0) {
						responseHelper.setErrorStatusAndMessage("Can't update batch default");
						return responseHelper.getResponse();
					}
				} else {
					Modetail modetail = new Modetail();
					modetail.setXitem(bd.getXbomcomp());
					modetail.setXqtyreq(xproduction.multiply(BigDecimal.valueOf(1000)));
					modetail.setXwh("02");
					modetail.setXtype("Default");
					modetail.setXbatch(batch.getXbatch());
					modetail.setXunit("gm");
					modetail.setXbomrow(1);
					modetail.setXqtyactual(xproduction);

					long scount = moService.saveMoDetail(modetail);
					if(scount == 0) {
						responseHelper.setErrorStatusAndMessage("Can't create batch default");
						return responseHelper.getResponse();
					}
				}
			}

			// Now expload bom and get default production and complited production
			String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			bmbomService.explodeBom2(batch.getXbatch(), batch.getXbomkey(), "Explode", errorCode);
			String em = getProcedureErrorMessages(errorCode);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage("BOM expload failed");
				return responseHelper.getResponse();
			}
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
	public String openOthersBatchDetailModal(@PathVariable String xbatch, @RequestParam(required = false) boolean withoutbom, Model model) {
		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) return "redirect:/production/batch";

		List<Modetail> batchdetails = moService.findModetailByXbatch(xbatch);
		if(batchdetails == null || batchdetails.isEmpty()) batchdetails = new ArrayList<>();

		model.addAttribute("batch", batch);
		model.addAttribute("xbatch", xbatch);
		model.addAttribute("batchdetails", batchdetails);
		model.addAttribute("withoutbom", withoutbom);
		return "pages/production/batch/othersbatchdetailmodal::othersbatchdetailmodal";
	}

	@GetMapping("{xbatch}/batchdetail/{xrow}/show")
	public String openBatchDetailModal(@PathVariable String xbatch, @PathVariable String xrow, @RequestParam(required = false) boolean withoutbom, Model model) {

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

		model.addAttribute("withoutbom", withoutbom);
		model.addAttribute("batchdetail", mod);
		return "pages/production/batch/batchdetailmodal::batchdetailmodal";
	}

	@PostMapping("/batchdetail/save")
	public @ResponseBody Map<String, Object> saveBatchDetail(Modetail modetail, Model model){
		if(modetail == null || StringUtils.isBlank(modetail.getXbatch())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// qty input validation
		if(modetail.getXqtyactual() == null || modetail.getXqtyactual().compareTo(BigDecimal.ONE) == -1) {
			responseHelper.setErrorStatusAndMessage("Please enter valid quantity");
			return responseHelper.getResponse();
		}
		

		// check item already exist in detail list
		boolean bomRawWastage = false;
		if(modetail.getXrow() == 0) {
			Modetail dupe = moService.findModetailByXbatchAndXitem(modetail.getXbatch(), modetail.getXitem());
			if(dupe != null && !"Default".equalsIgnoreCase(dupe.getXtype())) {
				responseHelper.setErrorStatusAndMessage("Item already added into detail list. Please add another one or update existing");
				return responseHelper.getResponse();
			}
			if(dupe != null && "Default".equalsIgnoreCase(dupe.getXtype())) {
				bomRawWastage = true;
			}
		}

		modetail.setXwh("02");
		modetail.setXqtyreq(modetail.getXqtyactual().multiply(BigDecimal.valueOf(1000)));
		if(bomRawWastage) modetail.setXqtyreq(BigDecimal.ZERO);

		// Stock validation
		Moheader batch = moService.findMoHeaderByXbatch(modetail.getXbatch());
		if(batch == null) {
			responseHelper.setErrorStatusAndMessage("Batch not found in this system");
			return responseHelper.getResponse();
		}
		Imstock imstock = imstockService.findByXitemAndXwh(modetail.getXitem(), "02");
		BigDecimal usedRaw = psvService.getTotalRawUsedExceptCurrentBatch(batch.getXchalan(), modetail.getXitem(), batch.getXbatch());
		// Stock validation
		if(imstock.getXavail().subtract(usedRaw).compareTo(modetail.getXqtyactual()) == -1) {
			responseHelper.setErrorStatusAndMessage(modetail.getXitem() + " - stock not available. Already used : " + usedRaw + ", Available : " + imstock.getXavail().subtract(usedRaw));
			return responseHelper.getResponse();
		}

		PSV psv = psvService.findByXchalanAndXbatchAndXrawitem(batch.getXchalan(), batch.getXbatch(), modetail.getXitem());
		if(psv == null) {
			// save new
			psv = new PSV();
			psv.setXchalan(batch.getXchalan());
			psv.setXbatch(batch.getXbatch());
			psv.setXrawitem(modetail.getXitem());
			psv.setXprod(modetail.getXqtyactual());
			long scount = psvService.savePSV(psv);
			if(scount == 0) {
				responseHelper.setErrorStatusAndMessage("Stock qty validation not saved");
				return responseHelper.getResponse();
			}
		} else {
			// update existing
			psv.setXrawitem(modetail.getXitem());
			psv.setXprod(modetail.getXqtyactual());
			long ucount = psvService.updatePSV(psv);
			if(ucount == 0) {
				responseHelper.setErrorStatusAndMessage("Stock qty validation not updated");
				return responseHelper.getResponse();
			}
		}



		// if existing
		Modetail md = moService.findModetailByXrowAndXbatch(modetail.getXrow(), modetail.getXbatch());
		if(md != null) {
			BigDecimal prvqty = md.getXqtyactual();
			BeanUtils.copyProperties(modetail, md);
			long count = moService.updateMoDetail(modetail);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Cant update item");
				return responseHelper.getResponse();
			}

			// update batch if it has no bom setting
			if("Used".equalsIgnoreCase(md.getXtype())){
				batch.setXproduction(batch.getXproduction().subtract(prvqty).add(md.getXqtyactual()));
				batch.setBomexploaded(true);
				long ubcount = moService.updateMoHeader(batch);
				if(ubcount == 0) {
					responseHelper.setErrorStatusAndMessage("Cant update batch production quantity");
					return responseHelper.getResponse();
				}
			}

			responseHelper.setTriggerModalUrl("othersbatchdetailmodal", "/production/batch/batchdetail/"+ modetail.getXbatch() +"/others/show?withoutbom=" + "Used".equalsIgnoreCase(md.getXtype()));
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		long count = moService.saveMoDetail(modetail);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Cant save item");
			return responseHelper.getResponse();
		}
		// update batch if it has no bom setting
		if("Used".equalsIgnoreCase(modetail.getXtype())){
			batch.setXproduction(batch.getXproduction().add(modetail.getXqtyactual()));
			batch.setBomexploaded(true);
			long ubcount = moService.updateMoHeader(batch);
			if(ubcount == 0) {
				responseHelper.setErrorStatusAndMessage("Cant update batch production quantity");
				return responseHelper.getResponse();
			}
		}

		responseHelper.setTriggerModalUrl("othersbatchdetailmodal", "/production/batch/batchdetail/"+ modetail.getXbatch() +"/others/show?withoutbom=" + "Used".equalsIgnoreCase(modetail.getXtype()));
		responseHelper.setSuccessStatusAndMessage("Saved successfully");
		return responseHelper.getResponse();
	}

	@PostMapping("{xbatch}/batchdetail/{xrow}/delete")
	public @ResponseBody Map<String, Object> deleteBatchDetail(@PathVariable String xbatch, @PathVariable String xrow, @RequestParam(required = false) boolean withoutbom, Model model){
		Modetail detail = moService.findModetailByXrowAndXbatch(Integer.parseInt(xrow), xbatch);
		if(detail == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		Moheader batch = moService.findMoHeaderByXbatch(xbatch);
		if(batch == null) {
			responseHelper.setErrorStatusAndMessage("Batch not found");
			return responseHelper.getResponse();
		}

		BigDecimal qty = detail.getXqtyactual();
		String type = detail.getXtype();
		String xitem = detail.getXitem();

		long count = moService.deleteModetail(detail);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// remove production qty from batch
		if(batch != null && StringUtils.isBlank(batch.getXbomkey()) && "Used".equalsIgnoreCase(type)) {
			batch.setXproduction(batch.getXproduction().subtract(qty));
			long ucount = moService.updateMoHeader(batch);
			if(ucount == 0) {
				responseHelper.setErrorStatusAndMessage("Batch production quantity not updated");
				return responseHelper.getResponse();
			}
		}

		// Clear psv data
		PSV psv = psvService.findByXchalanAndXbatchAndXrawitem(batch.getXchalan(), batch.getXbatch(), xitem);
		long dcount = psvService.deletePSV(psv);
		if(dcount == 0) {
			responseHelper.setErrorStatusAndMessage("Batch production quantity validation not deleted");
			return responseHelper.getResponse();
		}

		responseHelper.setTriggerModalUrl("othersbatchdetailmodal", "/production/batch/batchdetail/"+ detail.getXbatch() +"/others/show?withoutbom=" + withoutbom);
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

		// Check chalan has all process completed
		moService.isProductionProcessCompleted(mh.getXchalan());

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" + mh.getXchalan());
		responseHelper.setSuccessStatusAndMessage("Process successfull");
		return responseHelper.getResponse();
	}

	@PostMapping("/bulkprocessproduction/{xchalan}")
	public @ResponseBody Map<String, Object> bulkProcessProduction(@PathVariable String xchalan, Model model){
		Opordheader chalan = opordService.findOpordHeaderByXordernum(xchalan);
		if(chalan == null) {
			responseHelper.setErrorStatusAndMessage("Chalan not found");
			return responseHelper.getResponse();
		}

		List<Moheader> mhList = moService.findMoheaderByXchalan(xchalan);
		if(mhList == null || mhList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No Batch information found to make bulk process");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(chalan.getRawxtornum())) {
			String errorCode = xtrnService.generateAndGetXtrnNumber(TransactionCodeType.PROC_ERROR.getCode(), TransactionCodeType.PROC_ERROR.getdefaultCode(), 6);
			List<String> batchList = new ArrayList<>();
			for(Moheader mh : mhList) {
				if(!"Open".equalsIgnoreCase(mh.getXstatusmor())) continue;
				batchList.add(mh.getXbatch());
			}
			moService.bulkProcessProduction(batchList, "Process", errorCode);
			String em = getProcedureErrorMessages(errorCode);
			if(StringUtils.isNotBlank(em)) {
				responseHelper.setErrorStatusAndMessage(em);
				return responseHelper.getResponse();
			}
		} else {
			// check transfer order already confirmed
			if("Open".equalsIgnoreCase(imtorService.findImtorHeaderByXtornum(chalan.getRawxtornum()).getXstatustor())) {
				responseHelper.setErrorStatusAndMessage("Transfer order " + chalan.getRawxtornum() + " not confirmed yet, Please confirm this transfer order first");
				return responseHelper.getResponse();
			}

			// Add finished goods to inventory first
			List<Imtrn> finishedGoods = new ArrayList<>();
			for(Moheader moh : mhList) {
				Imtrn imtrn = new Imtrn();
				imtrn.setXtype(TransactionCodeType.TRANSACTION_TRANSFER.getCode());
				imtrn.setXtrn(TransactionCodeType.TRANSACTION_TRANSFER.getdefaultCode());
				imtrn.setXitem(moh.getXitem());
				imtrn.setXwh("02");
				imtrn.setXdate(new Date());
				imtrn.setXqty(moh.getXqtycom() == null ? BigDecimal.ZERO : moh.getXqtycom());
				imtrn.setXval(BigDecimal.ZERO);
				imtrn.setXvalpost(BigDecimal.ZERO);
				imtrn.setXdocnum(moh.getXbatch());
				imtrn.setXdocrow(0);
				imtrn.setXnote("Receive from Processing");
				imtrn.setXsign(1);
				Caitem c = caitemService.findByXitem(moh.getXitem());
				imtrn.setXunit(c.getXunitpur());
				imtrn.setXrate(c.getXrate() != null ? c.getXrate() : BigDecimal.ZERO);
				imtrn.setXref("");
				imtrn.setXstatusjv("Open");
				finishedGoods.add(imtrn);
			}

			if(!chalan.isFinishedtocomp()) {
				long count = imtrnService.save(finishedGoods);
				if(count == 0) {
					responseHelper.setErrorStatusAndMessage("Can't add finished good into inventory");
					return responseHelper.getResponse();
				}

				// update finished transfer status
				chalan.setFinishedtocomp(true);
				long countfmoh = opordService.updateOpordHeader(chalan);
				if(countfmoh == 0) {
					responseHelper.setErrorStatusAndMessage("Can't Update Chalan status for finished goods trnasfer");
					return responseHelper.getResponse();
				}
			}

			// Remove raw transfered material from inventory
			List<ImtorDetail> toDetails = imtorService.findImtorDetailByXtornum(chalan.getRawxtornum());
			if(toDetails == null || toDetails.isEmpty()) {
				responseHelper.setErrorStatusAndMessage("Can't find any transfer order details");
				return responseHelper.getResponse();
			}

			List<Imtrn> rawItems = new ArrayList<>();
			for(ImtorDetail detail : toDetails) {
				Imtrn imtrn = new Imtrn();
				imtrn.setXtype(TransactionCodeType.INVENTORY_TRANSACTION.getCode());
				imtrn.setXtrn("IS--");
				imtrn.setXitem(detail.getXitem());
				imtrn.setXwh("02");
				imtrn.setXdate(new Date());
				imtrn.setXqty(detail.getXqtyord() == null ? BigDecimal.ZERO : detail.getXqtyord());
				imtrn.setXval(BigDecimal.ZERO);
				imtrn.setXvalpost(BigDecimal.ZERO);
				imtrn.setXdocnum(chalan.getXordernum());
				imtrn.setXdocrow(detail.getXrow());
				imtrn.setXnote("Transfer from Process");
				imtrn.setXsign(-1);
				imtrn.setXunit(detail.getXunit());
				imtrn.setXrate(detail.getXrate() != null ? detail.getXrate() : BigDecimal.ZERO);
				imtrn.setXref("");
				imtrn.setXstatusjv("Open");
				rawItems.add(imtrn);
			}

			if(!chalan.isRawtocomp()) {
				long countr = imtrnService.save(rawItems);
				if(countr == 0) {
					responseHelper.setErrorStatusAndMessage("Can't removed raw items of transfer order "+ chalan.getRawxtornum() +" from inventory");
					return responseHelper.getResponse();
				}

				// update raw transfer status
				chalan.setRawtocomp(true);
				long countfmoh = opordService.updateOpordHeader(chalan);
				if(countfmoh == 0) {
					responseHelper.setErrorStatusAndMessage("Can't Update Chalan status for finished goods trnasfer");
					return responseHelper.getResponse();
				}
			}

			// update batch status
			if(chalan.isRawtocomp() && chalan.isFinishedtocomp()) {
				for(Moheader moh : mhList) moh.setXstatusmor("Completed");
				long countmoh = moService.updateMoHeader(mhList);
				if(countmoh == 0) {
					responseHelper.setErrorStatusAndMessage("Can't Update Batch status");
					return responseHelper.getResponse();
				}
			}

		}

		// Set production complete flag to chalan
		moService.isProductionProcessCompleted(xchalan);

		responseHelper.setReloadSectionIdWithUrl("batchdetailtable", "/production/batch/chalantobatch/" +xchalan);
		responseHelper.setSuccessStatusAndMessage("Process successfull");
		return responseHelper.getResponse();
	}

	@GetMapping("/print/{xchalan}")
	public ResponseEntity<byte[]> printChalan(@PathVariable String xchalan) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader chalan = opordService.findOpordHeaderByXordernum(xchalan);
		if(chalan == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Moheader> batchList = moService.findMoheaderByXchalan(xchalan);
		if(batchList == null || batchList.isEmpty()) {
			message = "No Item batch found for this chalan " + xchalan;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		for(Moheader batch : batchList) {
			if(batch.getXqtyprd() != null && batch.getXqtycom() != null) {
				batch.setDeviation(batch.getXqtycom().subtract(batch.getXqtyprd()));
			}
			List<Modetail> details = moService.findModetailByXbatch(batch.getXbatch());
			if(details == null || details.isEmpty()) continue;
			details.sort(Comparator.comparing(Modetail::getXrow));
			batch.getModetails().addAll(details);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		ProductionBatchReport report = new ProductionBatchReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Production Batch Report of Chalan - " + xchalan);
		report.setFromDate(sdf.format(chalan.getXdate()));
		report.setToDate(sdf.format(chalan.getXdate()));
		report.setPrintDate(sdf.format(new Date()));

		report.getBatches().addAll(batchList);

		byte[] byt = getPDFByte(report, "productionbatch.xsl");
		if(byt == null) {
			message = "Can't generate report for chalan : " + xchalan;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@GetMapping("/print/dailyproduction/{xchalan}")
	public ResponseEntity<byte[]> printDailyProductionReport(@PathVariable String xchalan) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader chalan = opordService.findOpordHeaderByXordernum(xchalan);
		if(chalan == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<DailyProductionBatchDetail> batchDetails = moService.dailyProductionReport(xchalan);
		if(batchDetails == null || batchDetails.isEmpty()) {
			message = "No Item batch found for this chalan " + xchalan;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");

		DailyProductionBatchReport report = new DailyProductionBatchReport();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Daily Production Report");
		report.setFromDate(sdf.format(chalan.getXdate()));
		report.setToDate(sdf.format(chalan.getXdate()));
		report.setPrintDate(sdf.format(new Date()));

		Map<String, RawMaterial> group = new HashMap<>();
		for(DailyProductionBatchDetail b : batchDetails) {
			String key = StringUtils.isNotBlank(b.getXbomcomp()) ? b.getXbomcomp() : b.getXitem();
			String name = StringUtils.isNotBlank(b.getXbomcomp()) ? b.getRawmaterial() : b.getFinishedgood();
			if(group.get(key) != null) {
				RawMaterial rm = group.get(key);

				FinishedGood fg = new FinishedGood();
				fg.setCode(b.getXitem());
				fg.setNamee(b.getFinishedgood());
				fg.setProductionRawQty(b.getXproduction() != null ? b.getXproduction() : BigDecimal.ZERO);
				fg.setProductionQty(b.getXqtyprd() != null ? b.getXqtyprd() : BigDecimal.ZERO);
				fg.setCompletedQuantity(b.getXqtycom() != null ? b.getXqtycom() : BigDecimal.ZERO);
				fg.setWastage(b.getWastage() != null ? b.getWastage() : BigDecimal.ZERO);
				fg.setDeviation(b.getDeviation() != null ? b.getDeviation() : BigDecimal.ZERO);
				rm.getFinishedGoods().add(fg);

				rm.setWastage(rm.getWastage().add(fg.getWastage()));
				rm.setQty(rm.getQty().add(fg.getProductionRawQty()));
			} else {
				RawMaterial rm = new RawMaterial();
				rm.setCode(key);
				rm.setName(name);

				FinishedGood fg = new FinishedGood();
				fg.setCode(b.getXitem());
				fg.setNamee(b.getFinishedgood());
				fg.setProductionRawQty(b.getXproduction() != null ? b.getXproduction() : BigDecimal.ZERO);
				fg.setProductionQty(b.getXqtyprd() != null ? b.getXqtyprd() : BigDecimal.ZERO);
				fg.setCompletedQuantity(b.getXqtycom() != null ? b.getXqtycom() : BigDecimal.ZERO);
				fg.setWastage(b.getWastage() != null ? b.getWastage() : BigDecimal.ZERO);
				fg.setDeviation(b.getDeviation() != null ? b.getDeviation() : BigDecimal.ZERO);
				rm.getFinishedGoods().add(fg);

				rm.setWastage(fg.getWastage());
				rm.setQty(fg.getProductionRawQty());

				group.put(key, rm);
			}

		}

		for(Map.Entry<String, RawMaterial> r : group.entrySet()) {
			report.getRawMaterials().add(r.getValue());
		}

		byte[] byt = getPDFByte(report, "productionbatchreport.xsl");
		if(byt == null) {
			message = "Can't generate report for chalan : " + xchalan;
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

}
