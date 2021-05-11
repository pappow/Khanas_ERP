package com.asl.service.report.impl;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import com.asl.model.FormFieldBuilder;


/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0502Service")
public class RM0502ServiceImpl extends AbstractReportService {


	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();	

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
