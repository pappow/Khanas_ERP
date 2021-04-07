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
@Service("RM0408Service")
public class RM0408ServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// xcus
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Customer", "search/report/cus", "", true));

//		// xorg
//		fieldsList.add(FormFieldBuilder.generateSearchField(3, "Organization", "search/report/xorg", "", false));
//
//		// xphone
//		fieldsList.add(FormFieldBuilder.generateInputField(4, "Phone", "", false));
//
//		// xgcus
//		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Customer Group", "search/report/xgcus","", false));
//
//		// xstatuscus
//		fieldsList.add(FormFieldBuilder.generateInputField(6, "Status", "Active", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
