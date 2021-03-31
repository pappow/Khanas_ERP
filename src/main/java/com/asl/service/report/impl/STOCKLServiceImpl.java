package com.asl.service.report.impl;

import java.util.ArrayList;
import java.util.Comparator;
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
@Service("stocklService")
public class STOCKLServiceImpl extends AbstractReportService {
	@Autowired private XcodesService xcodesService;

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

		// xitem
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "ITEM", "search/report/stock/xitem", "", false));

		// xwh
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "WAREHOUSE", options, " ", false));
		
//		// xorg
//		fieldsList.add(FormFieldBuilder.generateInputField(3, "XDESC", "Chicken Wings", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}
}
