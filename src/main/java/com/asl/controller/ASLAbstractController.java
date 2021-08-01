package com.asl.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.xml.sax.SAXException;

import com.asl.config.AppConfig;
import com.asl.entity.ProcErrorLog;
import com.asl.entity.Zbusiness;
import com.asl.enums.ReportMenu;
import com.asl.model.LoggedInUserDetails;
import com.asl.model.MenuProfile;
import com.asl.model.ReportProfile;
import com.asl.model.ResponseHelper;
import com.asl.model.validator.ModelValidator;
import com.asl.service.ASLSessionManager;
import com.asl.service.FormPagingService;
import com.asl.service.ListService;
import com.asl.service.PrintingService;
import com.asl.service.ProcErrorLogService;
import com.asl.service.ProfileService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;
import com.asl.service.report.ReportFieldService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Slf4j
@Component
public class ASLAbstractController {

	protected static final String PROLIFE_LIST_CODE = "ASLPOSPROFILE";
	protected static final String PROLIFE_LINES_LIST_CODE = "PROFILE";
	protected static final String DEFAULT_MENU = "DEFAULT_MENU";
	protected static final String DEFAULT_REPORT = "DEFAULT_REPORT";
	protected static final String ERROR = "Error is : {}, {}"; 
	protected static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat SDF2 = new SimpleDateFormat("E, dd-MMM-yyyy");

	@Autowired protected ApplicationContext appContext;
	@Autowired protected AppConfig appConfig;
	@Autowired protected ASLSessionManager sessionManager;
	@Autowired protected ResponseHelper responseHelper;
	@Autowired protected Validator validator;
	@Autowired protected ModelValidator modelValidator;
	@Autowired protected ProfileService profileService;
	@Autowired protected FormPagingService formPagingService;
	@Autowired protected XtrnService xtrnService;
	@Autowired protected XcodesService xcodesService;
	@Autowired protected ProcErrorLogService errorService;
	@Autowired protected PrintingService printingService;
	@Autowired protected Environment env;
	@Autowired protected ListService listService;

	@ModelAttribute("loggedInUser")
	protected LoggedInUserDetails loggedInUser() {
		return sessionManager.getLoggedInUserDetails();
	}

	@ModelAttribute("brandName")
	protected String brandName() {
		return appConfig.getDefaultSystemName();
	}

	@ModelAttribute("zbusiness")
	protected Zbusiness getZbusiness() {
		return sessionManager.getZbusiness();
	}

	@ModelAttribute("isCentral")
	public boolean isCentral() {
		return sessionManager.isCentral();
	}

	@ModelAttribute("isBranch")
	public boolean isBranch() {
		return sessionManager.isBranch();
	}

	@ModelAttribute("isConvention")
	public boolean isConventionCenter() {
		return Arrays.asList(env.getActiveProfiles()).contains("convention");
	}

	@ModelAttribute("isKhanas")
	public boolean isKhanas() {
		return Arrays.asList(env.getActiveProfiles()).contains("khanas");
	}

	@ModelAttribute("isTcc")
	public boolean isTCC() {
		return Arrays.asList(env.getActiveProfiles()).contains("tcc");
	}

	@ModelAttribute("isBoshila")
	public boolean isBoshila() {
		return Arrays.asList(env.getActiveProfiles()).contains("boshila");
	}

	@ModelAttribute("isGarments")
	public boolean isGarments() {
		return Arrays.asList(env.getActiveProfiles()).contains("garments");
	}
	
	@ModelAttribute("isDev")
	public boolean isDev() {
		return Arrays.asList(env.getActiveProfiles()).contains("dev");
	}

	@ModelAttribute("logoName")
	protected String defaultLogoName() {
		return appConfig.getDefaultLogoFileName();
	}

	@ModelAttribute("copyrightText")
	protected String copyRightText() {
		return appConfig.getCopyRightText();
	}

	@ModelAttribute("birtUrl")
	public String birtUrl() {
		return appConfig.getBirtUrl();
	}

	@ModelAttribute("reportProfile")
	public ReportProfile getLoggedInUserReportProfile() {
		ReportProfile rp = (ReportProfile) sessionManager.getFromMap("reportProfile");
		if(rp != null) return rp;

		rp = profileService.getLoggedInUserReportProfile();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(!"anonymousUser".equalsIgnoreCase(username)) {
			sessionManager.addToMap("reportProfile", rp);
		}

		return rp;
	}

	@ModelAttribute("menuProfile")
	public MenuProfile getLoggedInUserMenuProfile() {
		MenuProfile mp = (MenuProfile) sessionManager.getFromMap("menuProfile");
		if(mp != null) return mp;

		mp = profileService.getLoggedInUserMenuProfile();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if(!"anonymousUser".equalsIgnoreCase(username)) {
			sessionManager.addToMap("menuProfile", mp);
		}

		return mp;
	}

	protected ReportFieldService getReportFieldService(ReportMenu reportMenu) {
		if(reportMenu == null) return null;
		try {
			return (ReportFieldService) appContext.getBean(reportMenu.name() + "Service");
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
	}

	protected String getProcedureErrorMessages(String errorCode) {
		List<ProcErrorLog> errors = errorService.findByAction(errorCode);

		if(errors == null || errors.isEmpty()) return null;

		StringBuilder message = new StringBuilder();
		errors.stream().forEach(e -> {
			message.append(e.getOsqlCode() + " - " + e.getErrorMessage());
		});
		return message.toString();
	}

	protected byte[] getPDFByte(Object report, String templateName, HttpServletRequest request) {
		byte[] byt = null;
		try {
			byt = printingService.getPDFReportByte(report, appConfig.getXslPath() + "/" + templateName, request);
		} catch (JAXBException | ParserConfigurationException | SAXException | IOException
				| TransformerFactoryConfigurationError | TransformerException | ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
		return byt;
	}

	protected byte[] getBatchPDFByte(List<? extends Object> report, String templateName, HttpServletRequest request) {
		try {
			List<ByteArrayOutputStream> streams = new ArrayList<>();

			for(Object ob : report) {
				ByteArrayOutputStream baos = printingService.getPDFReportByteAttayOutputStream(ob, appConfig.getXslPath() + "/" + templateName, request);
				streams.add(baos);
			}

			ByteArrayOutputStream baus = new ByteArrayOutputStream();

			Document document = new com.itextpdf.text.Document();
			PdfCopy copy = new PdfSmartCopy(document, baus);
			document.open();
			PdfReader reader = null;
			for (ByteArrayOutputStream byt : streams) {
				reader = new PdfReader(byt.toByteArray());
				copy.addDocument(reader);
				reader.close();
			}
			baus.flush();
			document.close();	
			return baus.toByteArray();
		} catch (JAXBException | ParserConfigurationException | SAXException | IOException
				| TransformerFactoryConfigurationError | TransformerException | ParseException | DocumentException e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
	}

	

//	protected ImportExportService getImportExportService(String module) {
//		if(StringUtils.isBlank(module)) return null;
//		try {
//			return (ImportExportService) appContext.getBean(module + "Service");
//		} catch (Exception e) {
//			log.error(ERROR, e.getMessage(), e);
//			return null;
//		}
//	}
}
