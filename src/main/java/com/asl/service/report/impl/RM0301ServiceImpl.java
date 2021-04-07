package com.asl.service.report.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.model.report.RM0301;
import com.asl.model.report.RM0301Report;
import com.asl.service.PoordService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0301Service")
public class RM0301ServiceImpl extends AbstractReportService {

	@Autowired
	private PoordService poordService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		// ZID
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));

		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));

		// Status
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		options.add(new DropdownOption("Open", "Open"));
		options.add(new DropdownOption("Confirmed", "Confirmed"));
		options.add(new DropdownOption("GRN Created", "GRN Created"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(4, "Status", options, "Open", false));

		// xcus - Customer / Supplier
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Supplier", "search/report/sup", "", true));

		// Item
		fieldsList.add(FormFieldBuilder.generateSearchField(6, "Item", "search/report/stock/xitem", "", false));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {

		String fdate = (String) reportParams.get("FDATE");
		String tdate = (String) reportParams.get("TDATE");
		String xcus = (String) reportParams.get("XCUS");
		String xstatuspor = (String) reportParams.get("XSTATUSPOR");
		String xitem = (String) reportParams.get("XITEM");

		List<RM0301> list = poordService.getRM0301(fdate, tdate, xcus, xstatuspor, xitem);
		if(list == null || list.isEmpty()) return new byte[0];

		RM0301Report report = new RM0301Report();
		report.setBusinessName(sessionManager.getZbusiness().getZorg());
		report.setBusinessAddress(sessionManager.getZbusiness().getXmadd());
		report.setReportName("Purchase Order");
		report.setFromDate(fdate);
		report.setToDate(tdate);
		report.setPrintDate(SDF.format(new Date()));
		report.setCopyrightText("");

		list.sort(Comparator.comparing(RM0301::getXcus)
				.thenComparing(Comparator.comparing(RM0301::getXpornum))
				.thenComparing(Comparator.comparing(RM0301::getXitem)));

		
		

		return super.getPDFReportByte(templatePath, reportParams);
	}

}
