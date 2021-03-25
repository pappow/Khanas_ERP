package com.asl.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.lang3.StringUtils;
import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.asl.entity.Opordheader;
import com.asl.entity.Zbusiness;
import com.asl.enums.TransactionCodeType;
import com.asl.model.ProductionSuggestion;
import com.asl.model.report.ProductionPlanningsReport;
import com.asl.model.report.SalesOrderChalan;
import com.asl.model.report.Suggestion;
import com.asl.service.OpordService;
import com.asl.service.ProductionSuggestionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Mar 10, 2021
 */
@Slf4j
@Controller
@RequestMapping("/production/suggestion")
public class ProductionSuggestionController extends ASLAbstractController {

	@Autowired private OpordService opordService;
	@Autowired private ProductionSuggestionService productionSuggestionService;

	@GetMapping
	public String loadSuggestion(@RequestParam(required = false) String xordernum, Model model) {
		List<Opordheader> allChalans = new ArrayList<>();
		Opordheader chalan = null;
		if(StringUtils.isBlank(xordernum)) {
			allChalans.addAll(opordService.findAllOpordHeaderByXtypetrnAndXtrnAndXdate(TransactionCodeType.CHALAN_NUMBER.getCode(), TransactionCodeType.CHALAN_NUMBER.getdefaultCode(), new Date()));
		} else {
			chalan = opordService.findOpordHeaderByXordernum(xordernum);
			allChalans.add(chalan);
		}

		Map<String, List<ProductionSuggestion>> allSuggestions = new HashMap<>();
		for(Opordheader c : allChalans) {
			List<ProductionSuggestion> list = new ArrayList<>();
			if(c != null) {
				list = productionSuggestionService.getProductionSuggestion(c.getXordernum(), c.getXdate());
			}
			allSuggestions.put(c.getXordernum(), list);
		}

		model.addAttribute("allSuggestions", allSuggestions);
		return "pages/production/suggestion/suggestion";
	}

	@GetMapping("/create/{xordernum}")
	public String createSuggestionAndLoadSuggestionPage(@PathVariable String xordernum, Model model) {
		Opordheader opordHeader = opordService.findOpordHeaderByXordernum(xordernum);
		if(opordHeader == null) return "redirect:/production/suggestion";

		// delete suggestion table where xordernum
		productionSuggestionService.deleteSuggestion(xordernum, opordHeader.getXdate());

		// create suggestion
		productionSuggestionService.createSuggestion(xordernum);

		return "redirect:/production/suggestion?xordernum=" + xordernum;
	}

	@GetMapping("/print/{xordernum}")
	public ResponseEntity<byte[]> printChalan(@PathVariable String xordernum) {
		String message;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Opordheader oh = opordService.findOpordHeaderByXordernum(xordernum);
		if(oh == null) {
			message = "Chalan not found to do print";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<ProductionSuggestion> details = productionSuggestionService.getProductionSuggestion(oh.getXordernum(), oh.getXdate());
		if(details == null || details.isEmpty()) {
			message = "Suggestion not found for this chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Zbusiness zb = sessionManager.getZbusiness();
		ProductionPlanningsReport report = new ProductionPlanningsReport();
		report.setBusinessName(zb.getZorg());
		report.setBusinessAddress(zb.getXmadd());
		report.setReportName("Sales Order Chalan Report");
		report.setFromDate(SDF.format(oh.getXdate()));
		report.setToDate(SDF.format(oh.getXdate()));
		report.setPrintDate(SDF.format(new Date()));

		List<SalesOrderChalan> chalans = new ArrayList<>();
		SalesOrderChalan chalan = new SalesOrderChalan();
		chalan.setChalanName(oh.getXordernum());
		chalan.setChalanDate(SDF.format(oh.getXdate()));
		chalan.setStatus(oh.getXstatus());
		chalans.add(chalan);

		List<Suggestion> suggestions = new ArrayList<>();
		details.stream().forEach(d -> {
			Suggestion s = new Suggestion();
			s.setProductionItem(d.getXitemdes());
			s.setProductionItemQty(d.getXqtyord());
			s.setProductionItemUnit(d.getXitemunit());
			s.setRawMaterial(d.getXrawdes());
			s.setRawMaterialQty(d.getXrawqty());
			s.setRawMaterialUnit(d.getXrawunit());
			suggestions.add(s);
		});
		chalan.getSuggestions().addAll(suggestions);
		report.getChalans().addAll(chalans);

		String xml = null;
		try {
			xml = printingService.parseXMLString(report);
		} catch (JAXBException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		if(StringUtils.isBlank(xml)) {
			message = "Can't generate xml for chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Document doc = null;
		try {
			doc = printingService.getDomSourceForXML(xml);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		if(doc == null) {
			message = "Can't generate document object from xml for chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ByteArrayOutputStream out = null;
		try {
			out = printingService.transfromToPDFBytes(doc, appConfig.getXslPath() + "/productionplanningsreport.xsl");
		} catch (FOPException | TransformerFactoryConfigurationError | TransformerException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		if(out == null) {
			message = "Can't generate pdf for chalan";
			return new ResponseEntity<>(message.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		byte[] byt = out.toByteArray();
		headers.setContentType(new MediaType("application", "pdf"));
		return new ResponseEntity<>(byt, headers, HttpStatus.OK);
	}
}
