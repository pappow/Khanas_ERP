package com.asl.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Cacus;
import com.asl.entity.Caitem;
import com.asl.entity.Opdoheader;
import com.asl.entity.Opordheader;
import com.asl.enums.TransactionCodeType;
import com.asl.model.SearchSuggestResult;
import com.asl.service.CacusService;
import com.asl.service.CaitemService;
import com.asl.service.ImstockService;
import com.asl.service.OpdoService;
import com.asl.service.OpordService;
import com.asl.service.PogrnService;

import com.asl.service.ProductionSuggestionService;

import com.asl.service.BmbomService;


/**
 * @author Zubayer Ahamed
 * @since Mar 2, 2021
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController extends ASLAbstractController {

	@Autowired private CacusService cacusService;
	@Autowired private CaitemService caitemService;
	@Autowired private ProductionSuggestionService productionSuggestionService;
	@Autowired private PogrnService pogrnService;
	@Autowired private ImstockService imstockService;
	@Autowired private OpordService opordService;

	@Autowired private OpdoService opdoService;

	@Autowired private BmbomService bmbomService;


	@GetMapping("/supplier/{hint}")
	public @ResponseBody List<SearchSuggestResult> getSuppliers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cacusList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}
	
	@GetMapping("/confirmedinvoice/{hint}")
	public @ResponseBody List<SearchSuggestResult> getInvoices(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Opdoheader> opdoList = opdoService.searchOpdoHeader(TransactionCodeType.SALES_AND_INVOICE_NUMBER.getCode(), "Confirmed", hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		opdoList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXdornum(), c.getXdornum())));
		return list;
	}


	@GetMapping("/customer/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCustomers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();
		List<Cacus> cacusList = cacusService.searchCacus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}


	@GetMapping("/caitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}


	@GetMapping("/caitem/central/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCentralCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCentralCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/requisition/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCentralCaitemsForRequisitions(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchCentralCaitemForRequisition(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}


	@GetMapping("/caitem/finishednprod/{hint}")
	public @ResponseBody List<SearchSuggestResult> getFinsihedNProductionCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchFinishedProductionCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/rawmaterialprod/{hint}")
	public @ResponseBody List<SearchSuggestResult> getRawMaterialCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.searchRawMaterialsCaitem(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/caitem/withoutproduction/{hint}")
	public @ResponseBody List<SearchSuggestResult> getWithoutProductionCaitems(@PathVariable String hint){
		List<Caitem> caitemList = caitemService.getWithoutProductionCaitems(hint);
		List<SearchSuggestResult> list = new ArrayList<>();
		caitemList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/salesorderchalan/confirmed/{hint}")
	public @ResponseBody List<SearchSuggestResult> getOnlyConfirmedSalesOrderChalan(@PathVariable String hint){
		List<Opordheader> chalans = opordService.searchOpordheaderByXtypetrnAndXtrnAndXordernum(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), hint, "Confirmed");
		List<SearchSuggestResult> list = new ArrayList<>();
		chalans.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXordernum(), c.getXordernum())));
		return list;
	}

	
	// REPORT
	@GetMapping("/report/party/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllCustomerAndSupplier(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();

		List<Cacus> supList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		supList.parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		List<Cacus> cusList = cacusService.searchCacus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		cusList.parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		return list;
	}
	
	@GetMapping("/report/chalan/{hint}")
	public @ResponseBody List<SearchSuggestResult> getChalan(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		productionSuggestionService.searchClananNumbers(hint).parallelStream().forEach(c -> list.add(new SearchSuggestResult(c, c)));

		return list;
	}
	
	@GetMapping("/report/ponumber/{hint}")
	public @ResponseBody List<SearchSuggestResult> getPonumber(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		pogrnService.searchPoord(hint).parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXpornum(),c.getXpornum())));
		return list;
	}
	
	@GetMapping("/report/cus/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllCustomer(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();

		List<Cacus> cusList = cacusService.searchCacus(TransactionCodeType.CUSTOMER_NUMBER.getCode(), hint);
		cusList.parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		return list;
	}
	
	@GetMapping("/report/sup/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllSupplier(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();

		List<Cacus> supList = cacusService.searchCacus(TransactionCodeType.SUPPLIER_NUMBER.getCode(), hint);
		supList.parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus(), c.getXcus() + " - " + c.getXorg())));

		return list;
	}
	
	@GetMapping("/report/xorg/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXorg(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusService.searchXorg(hint).parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXorg(),c.getXorg())));
		return list;
	}
	
	@GetMapping("/report/xgcus/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXgcus(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusService.searchXgcus(hint).parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXgcus(),c.getXgcus())));
		return list;
	}
	
	@GetMapping("/report/stock/xitem/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXitem(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		imstockService.searchXitem(hint).parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem(),c.getXitem())));
		return list;
	}
	
	@GetMapping("/report/xbomkey/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAllXbomkey(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<SearchSuggestResult> list = new ArrayList<>();
		bmbomService.searchXbom(hint).parallelStream().forEach(c -> list.add(new SearchSuggestResult(c.getXbomkey(),c.getXbomkey())));
		return list;
	}
}
