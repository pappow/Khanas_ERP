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
@Service("cuslService")
public class CUSLServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// xcus
		fieldsList.add(FormFieldBuilder.generateInputField(2, "XCUS", "CUS-000021", true));

		// xorg
		fieldsList.add(FormFieldBuilder.generateInputField(3, "XORG", "Dhanmondi", true));

		// xphone
		fieldsList.add(FormFieldBuilder.generateInputField(4, "XPHONE", "", false));

		// xgcus
		fieldsList.add(FormFieldBuilder.generateInputField(5, "XGCUS", "Branch", true));

		// xstatuscus
		fieldsList.add(FormFieldBuilder.generateInputField(6, "XSTATUSCUS", "Active", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}