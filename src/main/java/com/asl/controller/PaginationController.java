package com.asl.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asl.model.pagination.BodyRecord;
import com.asl.model.pagination.HeaderColumn;
import com.asl.model.pagination.PagingTable;

/**
 * @author Zubayer Ahamed
 * @since Feb 22, 2021
 */
@Controller
@RequestMapping("/pagination")
public class PaginationController extends ASLAbstractController {

	@GetMapping
	public String loadPagingTaleWithData(Model model) {

		PagingTable pagingTable = new PagingTable();

		pagingTable.getHeaders().add(Collections.singletonList(new HeaderColumn(1, "SL")).get(0));
		pagingTable.getHeaders().add(Collections.singletonList(new HeaderColumn(2, "COL1")).get(0));
		pagingTable.getHeaders().add(Collections.singletonList(new HeaderColumn(3, "COL2")).get(0));

		List<List<BodyRecord>> records = new ArrayList<>();
		for(int j = 1; j <= 10; j++) {
			List<BodyRecord> record = new ArrayList<>();
			for(int i = 1; i <= 3; i++) {
				record.add(new BodyRecord(i, "rec" + j + " - val" + i));
			}
			records.add(record);
		}
		pagingTable.setRecords(records);

		model.addAttribute("pagingTable", pagingTable);
		
		return "pages/pagination/pagination";
	}
}
