package com.asl.service.report.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.enums.ReportParamDataType;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("grnlService")
public class GRNLServiceImpl extends AbstractReportService {

	@Autowired private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {
		
		List<FormFieldBuilder> fieldsList = new ArrayList<>();
		
		List<Xcodes> statusList = xcodesService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE);
		List<DropdownOption> options = new ArrayList<>();
		statusList.stream().forEach(x -> options.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		// xgrnstatus
		fieldsList.add(FormFieldBuilder.generateDropdownField(2, "GRN Status", options, "Confirmed", true));

		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(4, "To Date", new Date(), true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {
		
		String status = (String) reportParams.get("XGRNSTATUS");
		String fromDate = (String) reportParams.get("FromDate");
		String toDate = (String) reportParams.get("ToDate");
		
		
		
		return super.getPDFReportByte(templatePath, reportParams);
	}

	
}
