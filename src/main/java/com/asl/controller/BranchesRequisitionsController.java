package com.asl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;
import com.asl.entity.PoordDetail;
import com.asl.entity.PoordHeader;
import com.asl.enums.ResponseStatus;
import com.asl.enums.TransactionCodeType;
import com.asl.model.BranchesRequisitions;
import com.asl.model.report.BranchItem;
import com.asl.model.report.BranchRow;
import com.asl.model.report.MatrixReport;
import com.asl.model.report.MatrixReportData;
import com.asl.model.report.TableColumn;
import com.asl.model.report.Total;
import com.asl.service.OpordService;
import com.asl.service.PoordService;
import com.asl.service.RequisitionListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/purchasing/bqls")
public class BranchesRequisitionsController extends ASLAbstractController {

	@Autowired private RequisitionListService requisitionListService;
	@Autowired private PoordService poordService;
	@Autowired private OpordService opordService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping
	public String loadBqls(Model model) {
		model.addAttribute("bqlsList", requisitionListService.getAllBranchesRequisitions(new Date()));
		return "pages/purchasing/branchesrequisitions/bqls";
	}

	@GetMapping("/allopenreq")
	public String loadAllOpenRequisition(Model model) {
		model.addAttribute("bqlsList", requisitionListService.getAllOpenBranchesRequisitions());
		return "pages/purchasing/branchesrequisitions/openbqls";
	}

	@GetMapping("/allopenreq/reload")
	public String reloadAllOpenRequisition(Model model) {
		model.addAttribute("bqlsList", requisitionListService.getAllOpenBranchesRequisitions());
		return "pages/purchasing/branchesrequisitions/openbqls::branchesorderrequisitiontable";
	}

	@GetMapping("/query")
	public String reloadTableWithData(@RequestParam String date, Model model) throws ParseException {
		model.addAttribute("bqlsList", requisitionListService.getAllBranchesRequisitions(sdf.parse(date)));
		return "pages/purchasing/branchesrequisitions/bqls::branchesorderrequisitiontable";
	}

	@PostMapping("/query")
	public @ResponseBody Map<String, Object> queryForrequistionDetails(Date xdate, Model model){
		responseHelper.setReloadSectionIdWithUrl("branchesorderrequisitiontable", "/purchasing/bqls/query?date=" + sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@GetMapping("/query/matrix")
	public String reloadTableWithDataMatrix(@RequestParam String date, Model model) throws ParseException {
		generateMatrixData(sdf.parse(date), model);
		return "pages/purchasing/branchesrequisitions/bqlsdetail::branchesorderreqmatrixtable";
	}

	@PostMapping("/query/matrix")
	public @ResponseBody Map<String, Object> queryForrequistionDetailsMatrix(Date xdate, Model model){
		responseHelper.setReloadSectionIdWithUrl("branchesorderreqmatrixtable", "/purchasing/bqls/query/matrix?date=" + sdf.format(xdate));
		responseHelper.setStatus(ResponseStatus.SUCCESS);
		return responseHelper.getResponse();
	}

	@GetMapping("/ordreqdetails/{branchzid}/{xpornum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String branchzid, @PathVariable String xpornum, Model model) {
		model.addAttribute("poorddetailsList", requisitionListService.getDetailListByXpornumAndBranchZid(xpornum, branchzid));
		return "pages/purchasing/branchesrequisitions/branchorderreqdetailmodal::branchorderreqdetailmodal";
	}

	@GetMapping("/details")
	public String loadRqlsDetails(Model model) {
		generateMatrixData(new Date(), model);
		return "pages/purchasing/branchesrequisitions/bqlsdetail";
	}

	@GetMapping("/details/{date}/print")
	public ResponseEntity<byte[]> printMatrix(@PathVariable String date, Model model) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			message = "Chalan not parse date";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		MatrixReport mr = new MatrixReport();
		mr.setBusinessName(sessionManager.getZbusiness().getZorg());
		mr.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		mr.setReportName("Branch Requisitions");
		mr.setFromDate(SDF2.format(d));
		mr.setPrintDate(SDF2.format(new Date()));
		generateMatrixData2(d, mr, model);

		byte[] byt = getPDFByte(mr, "matrixreport.xsl");
		if(byt == null) {
			message = "Can't generate pdf";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}

	@PostMapping("/ordreqconfirm/allopen/{branchzid}/{xpornum}")
	public @ResponseBody Map<String, Object> confirmReqOrderAndCreateSOAndChalanFromAllOpen(@PathVariable String branchzid, @PathVariable String xpornum, Model model){
		PoordHeader ph = poordService.findBranchPoordHeaderByXpornumForCentral(xpornum, branchzid);
		if(ph == null) {
			responseHelper.setErrorStatusAndMessage("Can't find any requisition in the system");
			return responseHelper.getResponse();
		}

		// find all order requisition details first
		List<PoordDetail> poordDetailsList = poordService.findPoordDetailsByXpornumAndBranchZid(xpornum, branchzid);
		if(poordDetailsList == null || poordDetailsList.isEmpty()) {  // if no detail exist
			responseHelper.setErrorStatusAndMessage("Requisition has no item added");
			return responseHelper.getResponse();
		}

		ph.setXstatuspor("Confirmed");
		long count = poordService.update(ph);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Create sales order header
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.SALES_ORDER.getCode());
		oh.setXtrn(TransactionCodeType.SALES_ORDER.getdefaultCode());
		oh.setXpornum(ph.getXpornum());
		oh.setXdate(new Date());
		oh.setXcus(ph.getZid());
		oh.setXstatus("Open");
		long ohCount = opordService.saveOpordHeader(oh);
		if(ohCount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't crete sales order");
			return responseHelper.getResponse();
		}

		// if header saved successfully, then find it again from db to get xordernum
		// find oh by  xpornum, xdate, xtypetrn and xcus 
		Opordheader savedoh = opordService.findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(oh.getXtypetrn(), oh.getXpornum(), oh.getXcus(), oh.getXdate());
		if(savedoh == null) {
			responseHelper.setErrorStatusAndMessage("Can't found any sales order");
			return responseHelper.getResponse();
		}

		// if detail data exist
		for(PoordDetail pd : poordDetailsList) {
			// create all sales details from requisition details
			Oporddetail od = new Oporddetail();
			od.setXordernum(savedoh.getXordernum());
			od.setXitem(pd.getXitem());
			od.setXunit(pd.getXunitpur());
			od.setXqtyord(pd.getXqtyord());
			od.setXrate(pd.getXrate());
			long countOD = opordService.saveOpordDetail(od);
			if(countOD == 0) {
				//TODO: need to revoke all previous transaction from database
				responseHelper.setErrorStatusAndMessage("Can't create sales order detail");
				return responseHelper.getResponse();
			}
		}

		// reload page
		responseHelper.setSuccessStatusAndMessage("Requisition confirmed successfully");
		responseHelper.setReloadSectionIdWithUrl("branchesorderrequisitiontable", "/purchasing/bqls/allopenreq/reload");
		return responseHelper.getResponse();
	}

	@PostMapping("/ordreqconfirm/{branchzid}/{xpornum}")
	public @ResponseBody Map<String, Object> confirmReqOrderAndCreateSOAndChalan(@PathVariable String branchzid, @PathVariable String xpornum, Model model){
		// Change requisition order status
		PoordHeader ph = poordService.findBranchPoordHeaderByXpornumForCentral(xpornum, branchzid);
		if(ph == null) {
			responseHelper.setErrorStatusAndMessage("Can't find any requisition in the system");
			return responseHelper.getResponse();
		}

		// find all order requisition details first
		List<PoordDetail> poordDetailsList = poordService.findPoordDetailsByXpornumAndBranchZid(xpornum, branchzid);
		if(poordDetailsList == null || poordDetailsList.isEmpty()) {  // if no detail exist
			responseHelper.setErrorStatusAndMessage("Requisition has no item added");
			return responseHelper.getResponse();
		}

		// Update status
		ph.setXstatuspor("Confirmed");
		long count = poordService.update(ph);
		if(count == 0) {
			responseHelper.setStatus(ResponseStatus.ERROR);
			return responseHelper.getResponse();
		}

		// Create sales order header
		Opordheader oh = new Opordheader();
		oh.setXtypetrn(TransactionCodeType.SALES_ORDER.getCode());
		oh.setXtrn(TransactionCodeType.SALES_ORDER.getdefaultCode());
		oh.setXpornum(ph.getXpornum());
		oh.setXdate(new Date());
		oh.setXcus(ph.getZid());
		oh.setXstatus("Open");
		long ohCount = opordService.saveOpordHeader(oh);
		if(ohCount == 0) {
			responseHelper.setErrorStatusAndMessage("Can't crete sales order");
			return responseHelper.getResponse();
		}

		// if header saved successfully, then find it again from db to get xordernum
		// find oh by  xpornum, xdate, xtypetrn and xcus 
		Opordheader savedoh = opordService.findOpordHeaderByXtypetrnAndXpornumAndXdateAndXcus(oh.getXtypetrn(), oh.getXpornum(), oh.getXcus(), oh.getXdate());
		if(savedoh == null) {
			responseHelper.setErrorStatusAndMessage("Can't found any sales order");
			return responseHelper.getResponse();
		}

		// if detail data exist
		for(PoordDetail pd : poordDetailsList) {
			// create all sales details from requisition details
			Oporddetail od = new Oporddetail();
			od.setXordernum(savedoh.getXordernum());
			od.setXitem(pd.getXitem());
			od.setXunit(pd.getXunitpur());
			od.setXqtyord(pd.getXqtyord());
			od.setXrate(pd.getXrate());
			long countOD = opordService.saveOpordDetail(od);
			if(countOD == 0) {
				//TODO: need to revoke all previous transaction from database
				responseHelper.setErrorStatusAndMessage("Can't create sales order detail");
				return responseHelper.getResponse();
			}
		}


		// reload page
		responseHelper.setSuccessStatusAndMessage("Requisition confirmed successfully");
		responseHelper.setReloadSectionIdWithUrl("branchesorderrequisitiontable", "/purchasing/bqls/query?date=" + sdf.format(ph.getXdate()));
		return responseHelper.getResponse();
	}

	private void generateMatrixData2(Date date, MatrixReport mr, Model model) {
		List<BranchesRequisitions> bqList = requisitionListService.getAllBranchesRequisitionDetails(date);

		List<String> items = new ArrayList<>();
		List<String> branches = new ArrayList<>();
		List<TableColumn> columns = new ArrayList<>();
		for(BranchesRequisitions b : bqList) {
			if(!items.contains(b.getXitem())) {
				items.add(b.getXitem());
				TableColumn tc = new TableColumn();
				tc.setXitem(b.getXitem());
				tc.setXdesc(b.getXdesc());
				tc.setXunitpur(b.getXunitpur());
				columns.add(tc);
			}
			if(!branches.contains(b.getZorg())) {
				branches.add(b.getZorg());
			}
		}
		Collections.sort(items);
		columns.sort(Comparator.comparing(TableColumn::getXitem));

		Map<String, BranchItem> browMap = new TreeMap<>();
		for(BranchesRequisitions br : bqList) {
			if(browMap.get(br.getZorg() + "|" + br.getXitem()) != null) {
				BranchItem brow = browMap.get(br.getZorg() + "|" + br.getXitem());
				brow.setXqtyord(brow.getXqtyord().add(br.getXqtyord()));
			} else {
				BranchItem brow = new BranchItem();
				brow.setZorg(br.getZorg());
				brow.setXitem(br.getXitem());
				brow.setXqtyord(br.getXqtyord() != null ? br.getXqtyord() : BigDecimal.ZERO);
				browMap.put(br.getZorg() + "|" + br.getXitem(), brow);
			}
		}

		for(String item : items) {
			for(String branch : branches) {
				if(browMap.get(branch + "|" + item) == null) {
					BranchItem brow = new BranchItem();
					brow.setZorg(branch);
					brow.setXitem(item);
					brow.setXqtyord(BigDecimal.ZERO);
					browMap.put(branch + "|" + item, brow);
				}
			}
		}

		Map<String, Total> totalmap = new TreeMap<>();
		for(Map.Entry<String, BranchItem> m : browMap.entrySet()) {
			if(totalmap.get(m.getValue().getXitem()) != null) {
				Total total = totalmap.get(m.getValue().getXitem());
				total.setXqtyord(total.getXqtyord().add(m.getValue().getXqtyord()));
			} else {
				Total total = new Total();
				total.setXitem(m.getValue().getXitem());
				total.setXqtyord(m.getValue().getXqtyord());
				totalmap.put(m.getValue().getXitem(), total);
			}
		}

		Map<String, List<BranchItem>> branchWiseItems = new TreeMap<>();
		for(Map.Entry<String, BranchItem> m : browMap.entrySet()) {
			if(branchWiseItems.get(m.getValue().getZorg()) != null) {
				branchWiseItems.get(m.getValue().getZorg()).add(m.getValue());
			} else {
				List<BranchItem> list = new ArrayList<>();
				list.add(m.getValue());
				branchWiseItems.put(m.getValue().getZorg(), list);
			}
		}

		List<Total> totals = new ArrayList<>();
		totalmap.entrySet().stream().forEach(m -> totals.add(m.getValue()));
		totals.sort(Comparator.comparing(Total::getXitem));

		Map<String, BranchRow> browtracker = new HashMap<>();
		int chunk = 0;
		List<MatrixReportData> dataList = new ArrayList<>();
		MatrixReportData mrd = null;
		for(int i = 0; i < columns.size(); i++) {
			if(chunk == 9) {
				for(Map.Entry<String, BranchRow> m : browtracker.entrySet()) {
					mrd.getRows().add(m.getValue());
				}
				browtracker = new HashMap<>();
				chunk = 0;
			}
			if(chunk == 0) {
				mrd = new MatrixReportData();
				dataList.add(mrd);
			}
			mrd.getColumns().add(columns.get(i));
			mrd.getTotals().add(totals.get(i));

			for(int m = 0; m < branchWiseItems.size(); m++) {
				BranchItem item = branchWiseItems.get(branches.get(m)).get(i);
				BranchRow brow = null;
				if(browtracker.get(item.getZorg()) != null) {
					brow = browtracker.get(item.getZorg());
					brow.getItems().add(item);
					browtracker.put(item.getZorg(), brow);
				} else {
					brow = new BranchRow();
					brow.setZorg(item.getZorg());
					brow.getItems().add(item);
					browtracker.put(item.getZorg(), brow);
				}
			}

			chunk++;

			if(i == columns.size() - 1) {
				for(Map.Entry<String, BranchRow> m : browtracker.entrySet()) {
					mrd.getRows().add(m.getValue());
				}
				browtracker = new HashMap<>();
			}
		}

		mr.getDatas().addAll(dataList);
	}
	
	
	private void generateMatrixData(Date date, Model model) {
		List<TableColumn> distinctItems = new ArrayList<>();
		List<BranchRow> distinctBranch = new ArrayList<>();

		List<BranchesRequisitions> bqList = requisitionListService.getAllBranchesRequisitionDetails(date);

		Map<String, TableColumn> columnRowMap = new HashMap<>();
		Map<String, BranchRow> branchRowMap = new HashMap<>();
		for(BranchesRequisitions bq : bqList) {
			String item = bq.getXitem();
			if(columnRowMap.get(item) != null) {
				TableColumn c = columnRowMap.get(item);
				c.setTotalQty(c.getTotalQty().add(bq.getXqtyord()));
				columnRowMap.put(item, c);
			} else {
				TableColumn c = new TableColumn();
				c.setXitem(item);
				c.setXdesc(bq.getXdesc());
				c.setTotalQty(bq.getXqtyord());
				c.setXunitpur(bq.getXunitpur());
				columnRowMap.put(item, c);
			}

			String zorg = bq.getZorg();
			if(branchRowMap.get(zorg) != null) {
				BranchRow br = branchRowMap.get(zorg);

				boolean found = false;
				BigDecimal val = BigDecimal.ZERO;
				for(BranchItem bi : br.getItems()) {
					if(bi.getXitem().equalsIgnoreCase(bq.getXitem())) {
						bi.setXqtyord(bi.getXqtyord().add(bq.getXqtyord()));
						val = val.add(bi.getXqtyord());
						found = true;
						break;
					}
				}

				if(!found) {
					BranchItem bi = new BranchItem(bq.getZorg(), bq.getXitem(), bq.getXqtyord());
					br.getItems().add(bi);
					val = val.add(bi.getXqtyord());
				}

				br.setTotalItemOrdered(br.getTotalItemOrdered().add(val));

				branchRowMap.put(zorg, br);
			} else {
				BranchRow br = new BranchRow();
				br.setZorg(zorg);

				BranchItem bi = new BranchItem(bq.getZorg(), bq.getXitem(), bq.getXqtyord());
				br.getItems().add(bi);

				br.setTotalItemOrdered(bi.getXqtyord());

				branchRowMap.put(zorg, br);
			}
		}

		columnRowMap.entrySet().stream().forEach(c -> distinctItems.add(c.getValue()));
		branchRowMap.entrySet().stream().forEach(b -> distinctBranch.add(b.getValue()));

		SimpleDateFormat pdate = new SimpleDateFormat("yyyy-MM-dd");
		model.addAttribute("datadate", pdate.format(date));
		model.addAttribute("distinctBranch", distinctBranch);
		model.addAttribute("distinctItems", distinctItems);
		model.addAttribute("bqlsDetailsList", bqList);
	}
}


