package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0312Service")
public class RM0312ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		
		List<Xcodes> groupList = xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE);
		List<DropdownOption> group = new ArrayList<>();
		group.add(new DropdownOption("", "-- Select --"));
		groupList.stream().forEach(x -> group.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));

		// xitem
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Supplier", "search/report/sup", "", false));
		
		// status
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Status", "search/report/xstatuscrn", "", false));
		
		// Item Code
		fieldsList.add(FormFieldBuilder.generateSearchField(6, "Item Code", "search/report/stock/xitem", "", false));
		
		// Item Name
		fieldsList.add(FormFieldBuilder.generateSearchField(7, "Item Name", "search/report/caitemname", "", false));
		
		// Item Group
		fieldsList.add(FormFieldBuilder.generateDropdownField(8, "Item Group", group, "", false));
	
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

}
