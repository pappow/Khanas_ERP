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
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0305Service")
public class RM0305ServiceImpl extends AbstractReportService {

	@Autowired
	private XcodesService xcodesService;

	public List<FormFieldBuilder> getReportFields() {
		return generateFields();
	}

	private List<FormFieldBuilder> generateFields() {

		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		List<Xcodes> statusList = xcodesService.findByXtype(CodeType.STATUS.getCode(), Boolean.TRUE);
		List<DropdownOption> options = new ArrayList<>();
		options.add(new DropdownOption("", "-- Select --"));
		statusList.stream().forEach(x -> options.add(new DropdownOption(x.getXcode(), x.getXcode())));
		
		List<Xcodes> groupList = xcodesService.findByXtype(CodeType.ITEM_GROUP.getCode(), Boolean.TRUE);
		List<DropdownOption> group = new ArrayList<>();
		group.add(new DropdownOption("", "-- Select --"));
		groupList.stream().forEach(x -> group.add(new DropdownOption(x.getXcode(), x.getXcode())));

		// zid
		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId()));
		
		
		// From Date
		fieldsList.add(FormFieldBuilder.generateDateField(2, "From Date", new Date(), true));

		// To Date
		fieldsList.add(FormFieldBuilder.generateDateField(3, "To Date", new Date(), true));

		// xitem
		fieldsList.add(FormFieldBuilder.generateSearchField(4, "Supplier", "search/report/sup", "", false));
		
		// Item Code
		fieldsList.add(FormFieldBuilder.generateSearchField(5, "Item Code", "search/report/stock/xitem", "", false));
		
		// Item Group
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Item Group", group, "", false));

		// Item Name
		fieldsList.add(FormFieldBuilder.generateSearchField(7, "Item Name", "search/report/caitemname", "", false));
		
		// xgrnstatus
		fieldsList.add(FormFieldBuilder.generateDropdownField(8, "Status", options, "", false));
				
		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {

		String xwh = (String) reportParams.get("XWH");
		String sup = (String) reportParams.get("SUP");
		String status = (String) reportParams.get("XGRNSTATUS");
		String fromDate = (String) reportParams.get("FromDate");
		String toDate = (String) reportParams.get("ToDate");

		// List<Imstock> stocks = imstockService.search(xwh, xitem);
		// List<PogrnHeader> allgrns = pogrnService.search(xwh, sup, status, fromDate,
		// toDate);
		/*
		 * List<PogrnHeader> grns = pogrnService.search(xwh, xitem); if (grns == null ||
		 * grns.isEmpty()) return new byte[0];
		 * 
		 * Imstock firstRow = grns.stream().findFirst().get();
		 * 
		 * STOCKLReport report = new STOCKLReport();
		 * report.setBusinessName(firstRow.getZorg());
		 * report.setBusinessAddress(firstRow.getXmadd());
		 * report.setReportName("Stock List Report"); report.setPrintDate(SDF.format(new
		 * Date()));
		 * 
		 * report.getStocks().addAll(stocks);
		 * 
		 * String xml = printingService.parseXMLString(report); if
		 * (StringUtils.isBlank(xml)) return new byte[0];
		 * 
		 * Document doc = printingService.getDomSourceForXML(xml); if (doc == null)
		 * return new byte[0];
		 * 
		 * ByteArrayOutputStream baos = printingService.transfromToPDFBytes(doc,
		 * templatePath); if (baos == null) return new byte[0];
		 * 
		 * return baos.toByteArray();
		 */
		return new byte[0];
	}

}
