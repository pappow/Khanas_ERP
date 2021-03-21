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
@Service("grnlistService")
public class GRNLISTReportFieldServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		//xgrnstatus
		fieldsList.add(FormFieldBuilder.generateInputField(2, "GRN Status", "Confirmed", true));
		
		//From Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "From Date", new Date(), true));
		
		//To Date
		fieldsList.add(FormFieldBuilder.generateDateField(4, "To Date", new Date(), true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}