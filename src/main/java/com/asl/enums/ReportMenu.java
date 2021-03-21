package com.asl.enums;

import java.util.Map;

import com.asl.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// Procurements report
	SUPPLIERLIST(1, "SUPPLIERLIST", "Supplier List", ReportParamMap.SUPPLIER_LIST, "SupplierList.rptdesign", "supplierList", "Y", "/report/SUPPLIERLIST"),
	CUSTOMERLIST(2, "CUSTOMERLIST", "Customer List", ReportParamMap.CUSTOMER_LIST, "CustomerList.rptdesign", "customerList", "Y", "/report/CUSTOMERLIST"),
	STOCKLIST(3, "STOCKLIST", "Stock List", ReportParamMap.STOCK_LIST, "Imstock.rptdesign", "stockList", "Y", "/report/STOCKLIST"),
	ITEMLIST(4, "ITEMLIST", "Item List", ReportParamMap.ITEM_LIST, "ItemList.rptdesign", "itemList", "Y", "/report/ITEMLIST"),
	PURCHASEORDERLIST(5, "PURCHASEORDERLIST", "Purchase Order List", ReportParamMap.PURCHASEORDER_LIST, "PurchaseOrderListing.rptdesign", "purchaseorderList", "Y", "/report/PURCHASEORDERLIST"),
	PURCHASEDEVIATION(6, "PURCHASEDEVIATION", "Purchase Deviation List", ReportParamMap.PURCHASED_DEVIATION, "PurchaseDeviation.rptdesign", "purchaseDivisionList", "Y", "/report/PURCHASEDEVIATION"),
	GRNLIST(7, "GRNLIST", "GRN List", ReportParamMap.GRN_LIST, "GrnList.rptdesign", "grnList", "Y", "/report/GRNLIST");
	
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
