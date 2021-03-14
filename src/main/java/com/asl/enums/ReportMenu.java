package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// Procurements report
	SUPPLIERLIST(1, "SUPPLIERLIST", "Supplier List", ReportParamMap.SUPPLIER_LIST, "supplierList.rpt", "supplierList", "Y", "/report/SUPPLIERLIST");

	private int group;
	private String code;
	private String description;
	private Map<String, String> paramMap;
	private String reportFile;
	private String profileField;
	private String defaultAccess;
	private String menuPath;

	private ReportMenu(int group, String code, String des, Map<String, String> paramMap, String reportFile, String profileField, String defaultAccess, String path) {
		this.group = group;
		this.code = code;
		this.description = des;
		this.paramMap = paramMap;
		this.reportFile = reportFile;
		this.profileField = profileField;
		this.defaultAccess = defaultAccess;
		this.menuPath = path;
	}

	public int getGroup() {
		return this.group;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	public String getReportFile() {
		return this.reportFile;
	}

	public String getProfileField() {
		return this.profileField;
	}

	public String getDefaultAccess() {
		return this.defaultAccess;
	}

	public String getMenuPath() {
		return this.menuPath;
	}
}
