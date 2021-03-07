package com.asl.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.enums.ResponseStatus;
import com.asl.model.BranchesRequisitions;
import com.asl.service.RequisitionListService;

import lombok.AllArgsConstructor;
import lombok.Data;

@Controller
@RequestMapping("/purchasing/bqls")
public class BranchesRequisitionsController extends ASLAbstractController {

	@Autowired private RequisitionListService requisitionListService;

	@GetMapping
	public String loadBqls(Model model) {
		model.addAttribute("bqlsList", requisitionListService.getAllBranchesRequisitions(new Date()));
		return "pages/purchasing/branchesrequisitions/bqls";
	}

	@GetMapping("/ordreqdetails/{branchzid}/{xpornum}/show")
	public String displayItemDetailsOfOrderRequistion(@PathVariable String branchzid, @PathVariable String xpornum, Model model) {
		model.addAttribute("poorddetailsList", requisitionListService.getDetailListByXpornumAndBranchZid(xpornum, branchzid));
		return "pages/purchasing/branchesrequisitions/branchorderreqdetailmodal::branchorderreqdetailmodal";
	}

	@PostMapping("/ordreqconfirm/{branchzid}/{xpornum}")
	public @ResponseBody Map<String, Object> confirmOrderRequisition(@PathVariable String branchzid, @PathVariable String xpornum, Model model) {
		
		responseHelper.setStatus(ResponseStatus.ERROR);
		return responseHelper.getResponse();
	}

	@GetMapping("/details")
	public String loadRqlsDetails(Model model) {
		List<TableColumn> distinctItems = new ArrayList<TableColumn>();
		List<BranchRow> distinctBranch = new ArrayList<BranchRow>();

		List<BranchesRequisitions> bqList = requisitionListService.getAllBranchesRequisitionDetails(new Date());

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

		BigDecimal grandTotal = BigDecimal.ZERO;
		for(TableColumn c : distinctItems) {
			grandTotal = grandTotal.add(c.getTotalQty());
		}

		model.addAttribute("grandTotal", grandTotal);
		model.addAttribute("distinctBranch", distinctBranch);
		model.addAttribute("distinctItems", distinctItems);
		model.addAttribute("bqlsDetailsList", bqList);
		return "pages/purchasing/branchesrequisitions/bqlsdetail";
	}
}

@Data
class TableColumn{
	private String xitem;
	private String xdesc;
	private BigDecimal totalQty;
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
