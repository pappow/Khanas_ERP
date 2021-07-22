package com.asl.controller;

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
@RequestMapping("/system/list")
public class ListController extends ASLAbstractController {

	@Autowired private ListService listService;

	@GetMapping
	public String loadListPage(Model model) {
		ListHead lh = new ListHead();
		lh.setNewData(true);
		model.addAttribute("listHead", lh);
		model.addAttribute("listHeads", listService.getAllListHead());
		return "pages/system/list/list";
	}

	@GetMapping("/{listcode}")
	public String loadListPage(@PathVariable String listcode, Model model) {
		ListHead lh = listService.findListHeadByListcode(listcode);
		if(lh == null) return "redirect:/system/list";

		model.addAttribute("listHead", lh);
		model.addAttribute("listHeads", listService.getAllListHead());
		model.addAttribute("listLines", listService.findDataListByListcode(listcode));

		return "pages/system/list/list";
	}

	@PostMapping("/save")
	public @ResponseBody Map<String, Object> saveListHead(ListHead listHead, BindingResult bindingResult){
		if(listHead == null) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Modify List code first
		listHead.setListcode(listService.modifiedListcode(listHead.getListcode()));

		// Validate listhead data
		modelValidator.validateListHead(listHead, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// If new listhead
		if(listHead.isNewData()) {
			long count = listService.saveListHead(listHead);
			if(count == 0) {
				responseHelper.setErrorStatusAndMessage("Can't save list");
				return responseHelper.getResponse();
			}
			responseHelper.setSuccessStatusAndMessage("Listhead saved successfully");
			responseHelper.setRedirectUrl("/system/list/" + listHead.getListcode());
			return responseHelper.getResponse();
		}

		// If previous listhead
		ListHead lh = listService.findListHeadByListcode(listHead.getListcode());
		if(lh == null) {
			responseHelper.setErrorStatusAndMessage("List not found in this system");
			return responseHelper.getResponse();
		}

		BeanUtils.copyProperties(listHead, lh, "listcode");
		long count = listService.updateListHead(lh);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't update list");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("Listhead updateed successful");
		responseHelper.setRedirectUrl("/system/list/" + lh.getListcode());
		return responseHelper.getResponse();
	}

	@PostMapping("/archive/{listcode}")
	public @ResponseBody Map<String, Object> archive(@PathVariable String listcode){
		return doArchiveOrRestore(listcode, true);
	}


	@GetMapping("/{listHeadId}/copy")
	public String copy(@PathVariable Long listHeadId, Model model) {
		if(listHeadId == null) {
			model.addAttribute("listHead", new ListHead());
			return "redirect:/system/list";
		}

//		ListHead lh = listService.findListHeadByListcode(Long.valueOf(listHeadId));
//		if(lh == null) {
//			model.addAttribute("listHead", new ListHead());
//			return "redirect:/system/list";
//		}
//
//		//lh.setCopyId(String.valueOf(lh.getListHeadId()));
//		lh.setListCode("Copy of " + lh.getListCode());
//		lh.setDescription("Copy of " + lh.getDescription());
////		lh.setListHeadId(null);
//		model.addAttribute("listHead", lh);
		return "pages/list/list";
	}

	@GetMapping("{listHeadId}/listline/{listLineId}/show")
	public String openModalToAddOrUpdateList(@PathVariable String listHeadId, @PathVariable String listLineId, Model model) {
		ListHead lh = null;
//		try {
//			lh = listService.findListHeadByListcode(Long.valueOf(listHeadId));
//		} catch (Exception e) {
//			log.error(ERROR, e.getMessage(), e);
//		}

		if(lh == null) return "redirect:/system/list";

		model.addAttribute("listHead", lh);

		if("new".equalsIgnoreCase(listLineId)) {
			model.addAttribute("dataList", new DataList());
		} else {
			DataList dl = null;
//			try {
//				dl = listService.findDataListByListcodeAndListid(Long.valueOf(listLineId));
//			} catch (Exception e) {
//				log.error("Error is : {}, {}", e.getMessage(), e);
//			}
//
//			if(dl == null) return "redirect:/system/list";

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
//		try {
//			lh = listService.findListHeadByListcode(Long.valueOf(listHeadId));
//		} catch (Exception e) {
//			log.error(ERROR, e.getMessage(), e);
//		}
//		if(lh == null || StringUtils.isBlank(lh.getListCode())) {
//			responseHelper.setStatus(ResponseStatus.ERROR);
//			return responseHelper.getResponse();
//		}

//		try {
//			DataList dl = null;
//			if(dataList.getListId() != null) {
//				dl = listService.findDataListById(dataList.getListId());
//				if(dl != null) {
//					setNewDataListToOldDataList(dataList, dl);
//					long count = listService.update(dl);
//					if(count == 0) {
//						responseHelper.setStatus(ResponseStatus.ERROR);
//						return responseHelper.getResponse();
//					}
//
//					responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + dl.getListCode());
//					responseHelper.setSuccessStatusAndMessage("List updated successfully");
//					return responseHelper.getResponse();
//				}
//			} 

//			dataList.setListCode(lh.getListCode());
		//	dataList.setStatus(RecordStatus.L);
			long count = listService.saveDataList(dataList);
			if(count == 0) {
				responseHelper.setStatus(ResponseStatus.ERROR);
				return responseHelper.getResponse();
			}

//			responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + dataList.getListCode());
//			responseHelper.setSuccessStatusAndMessage("List saved successfully");
//		} catch (Exception e) {
//			log.error(ERROR, e.getMessage(), e);
//			responseHelper.setStatus(ResponseStatus.ERROR);
//		}

		return responseHelper.getResponse();
	}

	@GetMapping("/listlines/{listCode}")
	public String getReloadedListLineSection(@PathVariable String listCode, Model model){
//		model.addAttribute("listHead", listService.findListHeadByListCode(listCode));
		model.addAttribute("listLines", listService.findDataListByListcode(listCode));
		return "pages/list/list::listtable";
	}

	@PostMapping("{listHeadId}/listline/{listLineId}/archive")
	public @ResponseBody Map<String, Object> archiveListLine(@PathVariable String listHeadId, @PathVariable String listLineId, Model model) {
		ListHead lh = null;
//		try {
//			lh = listService.findListHeadByListcode(Long.valueOf(listHeadId));
//		} catch (Exception e) {
//			log.error("Error is : {}, {}", e.getMessage(), e);
//		}

//		if(lh == null || lh.getListHeadId() == null) {
//			responseHelper.setStatus(ResponseStatus.ERROR);
//			return responseHelper.getResponse();
//		}

		try {
//			DataList dl = listService.findDataListByListcodeAndListid(Long.valueOf(listLineId));
//			if(dl == null || dl.getListId() == null) {
//				responseHelper.setStatus(ResponseStatus.ERROR);
//				return responseHelper.getResponse();
//			}

//			dl.setZactive(Boolean.FALSE);
//			long count = listService.updateDataList(dl);
//			if(count == 0) {
//				responseHelper.setStatus(ResponseStatus.ERROR);
//				return responseHelper.getResponse();
//			}
//
//			responseHelper.setReloadSectionIdWithUrl("listtable", "/system/list/listlines/" + lh.getListCode());
//			responseHelper.setSuccessStatusAndMessage("List archived successfully");
		} catch (Exception e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
			responseHelper.setStatus(ResponseStatus.ERROR);
		}
		return responseHelper.getResponse();
	}

	private Map<String, Object> doArchiveOrRestore(String listcode, boolean doArchive) {
		if(StringUtils.isBlank(listcode)) {
			responseHelper.setErrorStatusAndMessage("Can't delete list");
			return responseHelper.getResponse();
		}

		ListHead lh = listService.findListHeadByListcode(listcode);
		if(lh == null) {
			responseHelper.setErrorStatusAndMessage("Can't find list in this system");
			return responseHelper.getResponse();
		}

		// check list has list lines
		List<DataList> dlist = listService.findDataListByListcode(listcode);
		if(dlist != null && !dlist.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Delete all list lines first");
			return responseHelper.getResponse();
		}
		

		long count = listService.deleteListHead(listcode);
		if(count == 0) {
			responseHelper.setErrorStatusAndMessage("Can't delete list");
			return responseHelper.getResponse();
		}

		responseHelper.setSuccessStatusAndMessage("List deleted successfully");
		responseHelper.setRedirectUrl("/system/list");
		return responseHelper.getResponse();
	}
}
