package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// Procurements report
	SUPL(1, "SUPL", "Supplier List", ReportParamMap.SUPL, "SUPL.rptdesign", "SUPL", "Y", "/report/SUPL", false, "SUPL.xsl", false, 1000),
	CUSL(2, "CUSL", "Customer List", ReportParamMap.CUSL, "CUSL.rptdesign", "CUSL", "Y", "/report/CUSL", false, "CUSL.xsl", false, 1000),
	STOCKL(3, "STOCKL", "Stock List", ReportParamMap.STOCKL, "STOCKL.rptdesign", "STOCKL", "Y", "/report/STOCKL", true, "STOCKL.xsl", false, 1000),
	ITEML(4, "ITEML", "Item List", ReportParamMap.ITEML, "ITEML.rptdesign", "ITEML", "Y", "/report/ITEML", false, "ITEML.xsl", false, 1000),
	POL(5, "POL", "Purchase Order List", ReportParamMap.POL, "POL.rptdesign", "POL", "Y", "/report/POL", false, "POL.xsl", false, 1000),
	PDL(6, "PDL", "Purchase Deviation List", ReportParamMap.PDL, "PDL.rptdesign", "PDL", "Y", "/report/PDL", false, "PDL.xsl", false, 1000),
	GRNL(7, "GRNL", "GRN List", ReportParamMap.GRNL, "GRNL.rptdesign", "GRNL", "Y", "/report/GRNL", true, "GRNL.xsl", false, 1000),
	SUGL(8, "SUGL", "Suggestion List", ReportParamMap.SUGL, "SUGL.rptdesign", "SUGL", "Y", "/report/SUGL", false, "SUGL.xsl", false, 1000),
	SL(9, "SL", "Supplier Ledger List", ReportParamMap.SL, "SL.rptdesign", "SL", "Y", "/report/SL", false, "SL.xsl", false, 1000),
	CL(10, "CL", "Customer Ledger List", ReportParamMap.CL, "CL.rptdesign", "CL", "Y", "/report/CL", false, "CL.xsl", false, 1000),
	BOML(11, "BOML", "BOM List", ReportParamMap.BOML, "BOML.rptdesign", "BOML", "Y", "/report/BOML", false, "BOML.xsl", false, 1000),
	PL(12, "PL", "Purchase Order List", ReportParamMap.PL, "PL.rptdesign", "PL", "Y", "/report/PL", false, "PL.xsl", false, 1000),
	SOL(13, "SOL", "Sales Order List", ReportParamMap.SOL, "SOL.rptdesign", "SOL", "Y", "/report/SOL", false, "SOL.xsl", false, 1000),
	SOE(14, "SOE", "Supplier Opening Balanace", ReportParamMap.SOE, "SOE.rptdesign", "SOE", "Y", "/report/SOE", false, "SOE.xsl", false, 1000);

	private int group;
	private String code;
	private String description;
	private Map<String, String> paramMap;
	private String reportFile;
	private String profileField;
	private String defaultAccess;
	private String menuPath;
	private boolean fopEnabled;
	private String xslFile;
	private boolean chunkDownload;
	private int chunkLimit;

	private ReportMenu(int group, String code, String des, Map<String, String> paramMap, String reportFile, String profileField, String defaultAccess, String path, boolean fopEnabled, String xslFile, boolean chunkDownload, int chunkLimit) {
		this.group = group;
		this.code = code;
		this.description = des;
		this.paramMap = paramMap;
		this.reportFile = reportFile;
		this.profileField = profileField;
		this.defaultAccess = defaultAccess;
		this.menuPath = path;
		this.fopEnabled = fopEnabled;
		this.xslFile = xslFile;
		this.chunkDownload = chunkDownload;
		this.chunkLimit = chunkLimit;
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

	public boolean isFopEnabled() {
		return this.fopEnabled;
	}

	public String getXslFile() {
		return this.xslFile;
	}

	public boolean isChunkDownload() {
		return this.chunkDownload;
	}

	public int getChunkLimit() {
		return this.chunkLimit;
	}
}
