package com.asl.service.report.impl;

import java.io.ByteArrayOutputStream;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.asl.entity.Imstock;
import com.asl.entity.Xcodes;
import com.asl.enums.CodeType;
import com.asl.model.DropdownOption;
import com.asl.model.FormFieldBuilder;
import com.asl.model.report.STOCKLReport;
import com.asl.service.ImstockService;
import com.asl.service.XcodesService;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Service("RM0601Service")
public class RM0601ServiceImpl extends AbstractReportService {
	@Autowired
	private XcodesService xcodesService;
	@Autowired
	private ImstockService imstockService;

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
		fieldsList.add(FormFieldBuilder.generateSearchField(2, "Item", "search/report/stock/xitem", "", false));

		// xwh
		fieldsList.add(FormFieldBuilder.generateDropdownField(3, "Warehouse", options, " ", false));

//		// xorg
//		fieldsList.add(FormFieldBuilder.generateInputField(3, "XDESC", "Chicken Wings", true));

		fieldsList.sort(Comparator.comparing(FormFieldBuilder::getSeqn));
		return fieldsList;
	}

	@Override
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams)
			throws JAXBException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {

		String xitem = (String) reportParams.get("XITEM");
		String xwh = (String) reportParams.get("XWH");

		List<Imstock> stocks = imstockService.search(xwh, xitem);
		if (stocks == null || stocks.isEmpty())
			return new byte[0];

		Imstock firstRow = stocks.stream().findFirst().get();

		STOCKLReport report = new STOCKLReport();
		report.setBusinessName(firstRow.getZorg());
		report.setBusinessAddress(firstRow.getXmadd());
		report.setReportName("Stock List Report");
		report.setPrintDate(SDF.format(new Date()));
		report.setReportLogo(appConfig.getReportLogo());

		report.getStocks().addAll(stocks);

		String xml = printingService.parseXMLString(report);
		if (StringUtils.isBlank(xml))
			return new byte[0];

		Document doc = printingService.getDomSourceForXML(xml);
		if (doc == null)
			return new byte[0];

		ByteArrayOutputStream baos = printingService.transfromToPDFBytes(doc, templatePath);
		if (baos == null)
			return new byte[0];

		return baos.toByteArray();
	}
}
