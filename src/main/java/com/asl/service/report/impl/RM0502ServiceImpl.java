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
@Service("RM0502Service")
public class RM0502ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;
	
	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();	
		
		List<Xcodes> statusList = xcodesService.findByXtype(CodeType.WAREHOUSE.getCode(), Boolean.TRUE);
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		statusList.stream().forEach(x -> options.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));

		// Batch No
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Batch No", "search/report/xbatch", "", false));
		
		// Order Status
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Order Status", "search/report/xtype", "", false));
		
				
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}


}
