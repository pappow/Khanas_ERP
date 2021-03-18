package com.asl.controller;

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

import com.asl.entity.Bmbomheader;
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
		
		
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	} 
}
