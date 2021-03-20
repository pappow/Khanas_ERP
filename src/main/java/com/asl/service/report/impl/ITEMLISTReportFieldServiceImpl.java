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
@Service("itemlistService")
public class ITEMLISTReportFieldServiceImpl extends AbstractReportService {

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		//xcus
		fieldsList.add(FormFieldBuilder.generateInputField(2, "XITEM", "IC-000004", true));
		
		//xorg
		fieldsList.add(FormFieldBuilder.generateInputField(3, "XDESC", "Chicken Pastrami", true));
		
		//xphone
		fieldsList.add(FormFieldBuilder.generateInputField(4, "XCATITEM", "Chicken", true));
		
		//xgcus
		fieldsList.add(FormFieldBuilder.generateInputField(5, "XGITEM", "Finished Good", true));
		
		//xstatuscus
		fieldsList.add(FormFieldBuilder.generateInputField(6, "XPRODITEM", "1", true));
				
		
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
