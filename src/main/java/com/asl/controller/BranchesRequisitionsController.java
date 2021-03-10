package com.asl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.entity.Poordheader;
import com.asl.enums.ResponseStatus;
import com.asl.model.BranchesRequisitions;
import com.asl.service.PoordService;
import com.asl.service.RequisitionListService;

import lombok.AllArgsConstructor;
import lombok.Data;

@Controller
@RequestMapping("/purchasing/bqls")
public class BranchesRequisitionsController extends ASLAbstractController {

	@Autowired private RequisitionListService requisitionListService;
	@Autowired private PoordService poordService;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping
	public String loadBqls(Model model) {
		model.addAttribute("bqlsList", requisitionListService.getAllBranchesRequisitions(new Date()));
		return "pages/purchasing/branchesrequisitions/bqls";
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

	@PostMapping("/ordreqconfirm/{branchzid}/{xpornum}")
	public @ResponseBody Map<String, Object> confirmReqOrderAndCreateSOAndChalan(@PathVariable String branchzid, @PathVariable String xpornum, Model model){
		// Change requisition order status
		Poordheader ph = poordService.findBranchPoordHeaderByXpornumForCentral(xpornum);

		
		return responseHelper.getResponse();
	}

	private void generateMatrixData(Date date, Model model) {
		List<TableColumn> distinctItems = new ArrayList<>();
		List<BranchRow> distinctBranch = new ArrayList<>();

		List<BranchesRequisitions> bqList = requisitionListService.getAllBranchesRequisitionDetails(date);

		Map<String, TableColumn> columnRowMap = new HashMap<>();
		Map<String, BranchRow> branchRowMap = new HashMap<>();
		bqList.stream().forEach(bq -> {
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

				BranchItem bi = new BranchItem(bq.getXitem(), bq.getXqtyord());
				br.getItems().add(bi);

				br.setTotalItemOrdered(br.getTotalItemOrdered().add(bi.getXqtyord()));
			} else {
				BranchRow br = new BranchRow();
				br.setZorg(zorg);

				BranchItem bi = new BranchItem(bq.getXitem(), bq.getXqtyord());
				br.getItems().add(bi);

				br.setTotalItemOrdered(bi.getXqtyord());

				branchRowMap.put(zorg, br);
			}
		});

		columnRowMap.entrySet().stream().forEach(c -> distinctItems.add(c.getValue()));
		branchRowMap.entrySet().stream().forEach(b -> distinctBranch.add(b.getValue()));

		model.addAttribute("distinctBranch", distinctBranch);
		model.addAttribute("distinctItems", distinctItems);
		model.addAttribute("bqlsDetailsList", bqList);
	}
}

@Data
class TableColumn{
	private String xitem;
	private String xdesc;
	private BigDecimal totalQty;
	private String xunitpur;
}

@Data
class BranchRow{
	private String zorg;
	private List<BranchItem> items = new ArrayList<>();
	private BigDecimal totalItemOrdered;
}

@Data
@AllArgsConstructor
class BranchItem{
	private String xitem;
	private BigDecimal xqtyord;
}
