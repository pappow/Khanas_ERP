package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.asl.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("pdlService")
public class PDLServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// xpornum - PO Number
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "PO-Number", "search/report/ponumber", "", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
