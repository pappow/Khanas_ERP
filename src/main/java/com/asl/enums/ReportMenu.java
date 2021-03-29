package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// Procurements report
	SUPL(1, "SUPL", "Supplier List", ReportParamMap.SUPL, "SUPL.rptdesign", "SUPL", "Y", "/report/SUPL"),
	CUSL(2, "CUSL", "Customer List", ReportParamMap.CUSL, "CUSL.rptdesign", "CUSL", "Y", "/report/CUSL"),
	STOCKL(3, "STOCKL", "Stock List", ReportParamMap.STOCKL, "STOCKL.rptdesign", "STOCKL", "Y", "/report/STOCKL"),
	ITEML(4, "ITEML", "Item List", ReportParamMap.ITEML, "ITEML.rptdesign", "ITEML", "Y", "/report/ITEML"),
	POL(5, "POL", "Purchase Order List", ReportParamMap.POL, "POL.rptdesign", "POL", "Y", "/report/POL"),
	PDL(6, "PDL", "Purchase Deviation List", ReportParamMap.PDL, "PDL.rptdesign", "PDL", "Y", "/report/PDL"),
	GRNL(7, "GRNL", "GRN List", ReportParamMap.GRNL, "GRNL.rptdesign", "GRNL", "Y", "/report/GRNL"),
	SUGL(8, "SUGL", "Suggestion List", ReportParamMap.SUGL, "SUGL.rptdesign", "SUGL", "Y", "/report/SUGL"),
	SL(9, "SL", "Supplier Ledger List", ReportParamMap.SL, "SL.rptdesign", "SL", "Y", "/report/SL"),
	CL(10, "CL", "Customer Ledger List", ReportParamMap.CL, "CL.rptdesign", "CL", "Y", "/report/CL"),
	BOML(11, "BOML", "BOM List", ReportParamMap.BOML, "BOML.rptdesign", "BOML", "Y", "/report/BOML"),
	PL(12, "PL", "Purchase Order List", ReportParamMap.PL, "PL.rptdesign", "PL", "Y", "/report/PL"),
	SOL(13, "SOL", "Sales Order List", ReportParamMap.SOL, "SOL.rptdesign", "SOL", "Y", "/report/SOL");

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
