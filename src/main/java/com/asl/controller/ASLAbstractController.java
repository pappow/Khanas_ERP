package com.asl.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.asl.config.AppConfig;
import com.asl.entity.ProcErrorLog;
import com.asl.entity.Zbusiness;
import com.asl.enums.ReportMenu;
import com.asl.model.MenuProfile;
import com.asl.model.ReportProfile;
import com.asl.model.ResponseHelper;
import com.asl.model.validator.ModelValidator;
import com.asl.service.ASLSessionManager;
import com.asl.service.FormPagingService;
import com.asl.service.PrintingService;
import com.asl.service.ProcErrorLogService;
import com.asl.service.ProfileService;
import com.asl.service.XcodesService;
import com.asl.service.XtrnService;
import com.asl.service.ZbusinessService;
import com.asl.service.report.ReportFieldService;

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
	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
		return profileService.getLoggedInUserReportProfile();
	}

	@ModelAttribute("menuProfile")
	public MenuProfile getLoggedInUserMenuProfile() {
		return profileService.getLoggedInUserMenuProfile();
	}

	protected ReportFieldService getReportFieldService(ReportMenu reportMenu) {
		if(reportMenu == null) return null;
		try {
			return (ReportFieldService) appContext.getBean(reportMenu.getCode().toLowerCase() + "Service");
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return null;
		}
	}

	protected String getProcedureErrorMessages(String errorCode) {
		List<ProcErrorLog> errors = errorService.findByAction(errorCode);

		if(errors != null && !errors.isEmpty()) return null;

		StringBuilder message = new StringBuilder();
		errors.parallelStream().forEach(e -> {
			message.append(e.getOsqlCode() + " - " + e.getErrorMessage());
		});
		return message.toString();
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
