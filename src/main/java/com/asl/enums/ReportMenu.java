package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// Procurements  -- M0300
	RM0301(999, "Purchase Order Listing", ReportParamMap.RM0301, "Y", false, false, 1000),
	RM0302(999, "Suggestion List", ReportParamMap.RM0302, "Y", false, false, 1000),
	RM0303(999, "Purchase Listing", ReportParamMap.RM0303, "Y", false, false, 1000),
	RM0304(999, "Purchase Deviation Listing", ReportParamMap.RM0304, "Y", false, false, 1000),
	RM0305(999, "GRN Listing", ReportParamMap.RM0305, "Y", true, false, 1000),
	RM0306(999, "GRN Return Listing", ReportParamMap.RM0306, "Y", true, false, 1000),
	RM0307(999, "Supplier Ledger Detail", ReportParamMap.RM0307, "Y", false, false, 1000),
	RM0308(999, "Supplier Ledger Summary", ReportParamMap.RM0308, "Y", true, false, 1000),
	RM0309(999, "Supplier Payment Listing", ReportParamMap.RM0309, "Y", true, false, 1000),
	RM0310(999, "Supplier Opening Balanace", ReportParamMap.RM0310, "Y", false, false, 1000),
	RM0311(999, "Supplier List", ReportParamMap.RM0311, "Y", false, false, 1000),

	// Sales & Invoicing  -- M0400
	RM0401(999, "Sales Order Listing", ReportParamMap.RM0401, "Y", false, false, 1000),
	RM0402(999, "Sales Listing Detail", ReportParamMap.RM0402, "Y", false, false, 1000),
	RM0403(999, "Sales Listing Summary", ReportParamMap.RM0403, "Y", false, false, 1000),
	RM0404(999, "Money Receipt Listing", ReportParamMap.RM0404, "Y", false, false, 1000),
	RM0405(999, "Customer Ledger Detail", ReportParamMap.RM0405, "Y", false, false, 1000),
	RM0406(999, "Customer Ledger Summary", ReportParamMap.RM0406, "Y", false, false, 1000),
	RM0407(999, "Both Party Ledger", ReportParamMap.RM0407, "Y", false, false, 1000),
	RM0408(999, "Customer List", ReportParamMap.RM0408, "Y", false, false, 1000),
	RM0409(999, "Receivable and Collection Statement", ReportParamMap.RM0409, "Y", false, false, 1000),

	// Production  -- M0500
	RM0501(999, "BOM List", ReportParamMap.RM0501, "Y", false, false, 1000),
	RM0502(999, "Batch wise production", ReportParamMap.RM0502, "Y", false, false, 1000),
	RM0503(999, "Batch wise material consumptions", ReportParamMap.RM0503, "Y", false, false, 1000),
	RM0504(999, "Production Analysis (Expected vs Actual)", ReportParamMap.RM0504, "Y", false, false, 1000),

	// Inventory  -- M0600
	RM0601(999, "Stock List", ReportParamMap.RM0601, "Y", true, false, 1000),
	RM0602(999, "Date Wise Stock Status", ReportParamMap.RM0602, "Y", true, false, 1000),
	RM0603(999, "Stock Low Status", ReportParamMap.RM0603, "Y", true, false, 1000),
	RM0604(999, "Inventory Consumption Report", ReportParamMap.RM0604, "Y", true, false, 1000),
	RM0606(999, "Item Transaction Detail", ReportParamMap.RM0605, "Y", true, false, 1000),
	RM0607(999, "Item Transaction Summary", ReportParamMap.RM0606, "Y", true, false, 1000),
	RM0608(999, "Item List", ReportParamMap.RM0607, "Y", false, false, 1000);


	private int seqn;
	private String description;
	private Map<String, String> paramMap;
	private String defaultAccess;
	private boolean fopEnabled;
	private boolean chunkDownload;
	private int chunkLimit;

	private ReportMenu(int seqn, String des, Map<String, String> paramMap, String defaultAccess, boolean fopEnabled, boolean chunkDownload, int chunkLimit) {
		this.seqn = seqn;
		this.description = des;
		this.paramMap = paramMap;
		this.defaultAccess = defaultAccess;
		this.fopEnabled = fopEnabled;
		this.chunkDownload = chunkDownload;
		this.chunkLimit = chunkLimit;
	}

	public int getSeqn() {
		return this.seqn;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	public String getDefaultAccess() {
		return this.defaultAccess;
	}

	public boolean isFopEnabled() {
		return this.fopEnabled;
	}

	public boolean isChunkDownload() {
		return this.chunkDownload;
	}

	public int getChunkLimit() {
		return this.chunkLimit;
	}
}
