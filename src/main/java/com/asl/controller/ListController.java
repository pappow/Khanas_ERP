package com.asl.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;
import com.asl.enums.ResponseStatus;
import com.asl.service.ListService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 *
 */
@Slf4j
@Controller
@RequestMapping("system/list")
public class ListController extends ASLAbstractController {

	@Autowired private ListService listService;

	@GetMapping
	public String loadListPage(Model model) {
		model.addAttribute("listHead", new ListHead());
		model.addAttribute("listHeads", listService.getAllListHead());
		return "pages/list/list";
	}

	@GetMapping("/{listHeadId}")
	public String loadListPage(@PathVariable String listHeadId, Model model) {
		ListHead lh = null;
		try {
			lh = listService.findListHeadById(Long.valueOf(listHeadId));
			if(lh == null) {
				return "redirect:/system/list";
			}
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return "redirect:/system/list";
		}

		model.addAttribute("listHead", lh);
		model.addAttribute("listLines", listService.findDataListByListCode(lh.getListCode()));
		model.addAttribute("listHeads", listService.getAllListHead());
		return "pages/list/list";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveListHead(ListHead listHead, BindingResult bindingResult){
		if(listHead == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify List code first
		listHead.setListCode(modifiedListCode(listHead.getListCode()));

		// Validate listhead data
		modelValidator.validateListHead(listHead, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// If new listhead
		if(listHead.getListHeadId() == null) {
			long count = listService.save(listHead);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Listhead saved successfully");
			responseHelper.setRedirectUrl("/system/list/");
			return responseHelper.getResponse();
		}

		// If previous listhead
		ListHead lh = listService.findListHeadById(listHead.getListHeadId());
		if(lh == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}
		setNewListHeadDataToOldListHead(listHead, lh);
		long count = listService.update(lh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update listhead");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Listhead update successful");
		responseHelper.setRedirectUrl("/system/list/" + listHead.getListHeadId());
		return responseHelper.getResponse();
	}

	private String modifiedListCode(String pc) {
		if(StringUtils.isBlank(pc)) return "";
		pc = pc.toUpperCase();
		pc = pc.replace(" ", "_");
		return pc;
	}

	@PostMapping("/archive/{listHeadId}")
	public @ResponseBody Map<String, Object> archive(@PathVariable Long listHeadId, ListHead listHead){
		return doArchiveOrRestore(listHeadId, listHead, true);
	}

	@PostMapping("/restore/{listHeadId}")
	public @ResponseBody Map<String, Object> restore(@PathVariable Long listHeadId, ListHead listHead){
		return doArchiveOrRestore(listHeadId, listHead, false);
	}

	@GetMapping("/{listHeadId}/copy")
	public String copy(@PathVariable Long listHeadId, Model model) {
		if(listHeadId == null) {
			model.addAttribute("listHead", new ListHead());
			return "redirect:/system/list";
		}

		ListHead lh = listService.findListHeadById(Long.valueOf(listHeadId));
		if(lh == null) {
			model.addAttribute("listHead", new ListHead());
			return "redirect:/system/list";
		}

		//lh.setCopyId(String.valueOf(lh.getListHeadId()));
		lh.setListCode("Copy of " + lh.getListCode());
		lh.setDescription("Copy of " + lh.getDescription());
		lh.setListHeadId(null);
		model.addAttribute("listHead", lh);
		return "pages/list/list";
	}

	@GetMapping("{listHeadId}/listline/{listLineId}/show")
	public String openModalToAddOrUpdateList(@PathVariable String listHeadId, @PathVariable String listLineId, Model model) {
		ListHead lh = null;
		try {
			lh = listService.findListHeadById(Long.valueOf(listHeadId));
		} catch (Exception e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
		}

		if(lh == null) return "redirect:/system/list";

		model.addAttribute("listHead", lh);

		if("new".equalsIgnoreCase(listLineId)) {
			model.addAttribute("dataList", new DataList());
		} else {
			DataList dl = null;
			try {
				dl = listService.findDataListById(Long.valueOf(listLineId));
			} catch (Exception e) {
				log.error("Error is : {}, {}", e.getMessage(), e);
			}

			if(dl == null) return "redirect:/system/list";

			model.addAttribute("dataList", dl);
		}

		return "pages/list/listmodal::listmodal";
	}

	@PostMapping("/listline/save")
	public @ResponseBody Map<String, Object> saveListLine(DataList dataList, String listHeadId){
		if(StringUtils.isBlank(listHeadId) || dataList == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		ListHead lh = null;
		try {
			lh = listService.findListHeadById(Long.valueOf(listHeadId));
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
		}
		if(lh == null || StringUtils.isBlank(lh.getListCode())) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		try {
			DataList dl = null;
			if(dataList.getListId() != null) {
				dl = listService.findDataListById(dataList.getListId());
				if(dl != null) {
					setNewDataListToOldDataList(dataList, dl);
					long count = listService.update(dl);
					if(count == 0) {
						responseHelper.setStatus(ResponseStatus.ERROR);
						return responseHelper.getResponse();
					}

					responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + dl.getListCode());
					responseHelper.setSuccessStatusAndMessage("List updated successfully");
					return responseHelper.getResponse();
				}
			} 

			dataList.setListCode(lh.getListCode());
		//	dataList.setStatus(RecordStatus.L);
			long count = listService.save(dataList);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + dataList.getListCode());
			responseHelper.setSuccessStatusAndMessage("List saved successfully");
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setStatus(ResponseStatus.ERROR);
		}

		return responseHelper.getResponse();
	}

	@GetMapping("/listlines/{listCode}")
	public String getReloadedListLineSection(@PathVariable String listCode, Model model){
		model.addAttribute("listHead", listService.findListHeadByListCode(listCode));
		model.addAttribute("listLines", listService.findDataListByListCode(listCode));
		return "pages/list/list::listtable";
	}

	@PostMapping("{listHeadId}/listline/{listLineId}/archive")
	public @ResponseBody Map<String, Object> archiveListLine(@PathVariable String listHeadId, @PathVariable String listLineId, Model model) {
		ListHead lh = null;
		try {
			lh = listService.findListHeadById(Long.valueOf(listHeadId));
		} catch (Exception e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
		}

		if(lh == null || lh.getListHeadId() == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		try {
			DataList dl = listService.findDataListById(Long.valueOf(listLineId));
			if(dl == null || dl.getListId() == null) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			dl.setZactive(Boolean.FALSE);
			long count = listService.update(dl);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

			responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + lh.getListCode());
			responseHelper.setSuccessStatusAndMessage("List archived successfully");
		} catch (Exception e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
			responseHelper.setStatus(ResponseStatus.ERROR);
		}
		return responseHelper.getResponse();
	}

	private Map<String, Object> doArchiveOrRestore(Long listHeadId, ListHead listHead, boolean doArchive) {
		if(listHeadId == null) {
			responseHelper.setErrorStatusAndMessage("Can't update listhead");
			return responseHelper.getResponse();
		}

		ListHead lh = listService.findListHeadById(listHeadId);
		if(lh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find data");
			return responseHelper.getResponse();
		}

		if(!doArchive && listService.findListHeadByListCode(lh.getListCode()) != null) {
			lh.setListCode("XXX_" + lh.getListCode());
		}
		lh.setZactive(doArchive ? Boolean.FALSE : Boolean.FALSE);
		long count = listService.update(lh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update listhead");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Listhead update successful");
		responseHelper.setRedirectUrl("/system/list/" + listHeadId);
		return responseHelper.getResponse();
	}

	private void setNewDataListToOldDataList(DataList nl, DataList ol) {
		ol.setListValue1(nl.getListValue1());
		ol.setListValue2(nl.getListValue2());
		ol.setListValue3(nl.getListValue3());
		ol.setListValue4(nl.getListValue4());
		ol.setListValue5(nl.getListValue5());
		ol.setListValue6(nl.getListValue6());
		ol.setListValue7(nl.getListValue7());
		ol.setListValue8(nl.getListValue8());
		ol.setListValue9(nl.getListValue9());
		ol.setListValue10(nl.getListValue10());
		ol.setListValue11(nl.getListValue11());
		ol.setListValue12(nl.getListValue12());
		ol.setListValue13(nl.getListValue13());
		ol.setListValue14(nl.getListValue14());
		ol.setListValue15(nl.getListValue15());
		ol.setListValue16(nl.getListValue16());
	}

	private void setNewListHeadDataToOldListHead(ListHead nl, ListHead ol) {
		ol.setListCode(nl.getListCode());
		ol.setDescription(nl.getDescription());
		ol.setPrompt1(nl.getPrompt1());
		ol.setPrompt2(nl.getPrompt2());
		ol.setPrompt3(nl.getPrompt3());
		ol.setPrompt4(nl.getPrompt4());
		ol.setPrompt5(nl.getPrompt5());
		ol.setPrompt6(nl.getPrompt6());
		ol.setPrompt7(nl.getPrompt7());
		ol.setPrompt8(nl.getPrompt8());
		ol.setPrompt9(nl.getPrompt9());
		ol.setPrompt10(nl.getPrompt10());
		ol.setPrompt11(nl.getPrompt11());
		ol.setPrompt12(nl.getPrompt12());
		ol.setPrompt13(nl.getPrompt13());
		ol.setPrompt14(nl.getPrompt14());
		ol.setPrompt15(nl.getPrompt15());
		ol.setPrompt16(nl.getPrompt16());
		ol.setNotes(nl.getNotes());
	}
}
