package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
public enum MenuProfile {

	M0100(9999, "M0100", "100","MASTER", "System Administration", "Y","/system"),
	M0101(9999, "M0101", "100","M0100", "User & Profile", "Y","/system/userentry"),
	M0102(9999, "M0102", "100","M0101", "Manage Users", "Y","/system/xusers"),
	M0103(9999, "M0103", "100","M0101", "Manage Profile", "Y","/system/profile"),
	M0104(9999, "M0104", "100","M0101", "Profile Allocation", "Y","/system/profileallocation"),
	M0105(9999, "M0105", "100","M0100", "List", "Y","/system/list"),

	M0200(9999, "M0200", "200", "MASTER", "Master Setup", "Y","/mastersetup"),
	M0201(9999, "M0201", "200", "M0200", "Party Master", "Y","/mastersetup/partymaster"),
	M0202(9999, "M0202", "200", "M0200", "Item Master", "Y","/mastersetup/itemmaster"),
	M0203(9999, "M0203", "200", "M0200", "Code & Parameters", "Y","/mastersetup/cap"),
	M0204(9999, "M0204", "200", "M0200", "Vat & Tax", "Y","/mastersetup/vataitmaster"),
	M0205(9999, "M0205", "200", "M0201", "Supplier Entry", "Y","/mastersetup/cacus"),
	M0206(9999, "M0206", "200", "M0201", "Customer Entry", "Y","/mastersetup/cacus"),
	M0207(9999, "M0207", "200", "M0202", "Item Entry", "Y","/mastersetup/caitem"),
	M0208(9999, "M0208", "200", "M0203", "Codes", "Y","/mastersetup/xcodes"),
	M0209(9999, "M0209", "200", "M0203", "Transaction Codes", "Y","/mastersetup/xtrn"),
	M0210(9999, "M0210", "200", "M0204", "Vat & Tax Entry", "Y","/mastersetup/vatait"),

	M0300(9999, "M0300", "300", "MASTER", "Procurements", "Y","/purchasing"),
	M0301(9999, "M0301", "300", "M0300", "Branches Requisition", "Y","/purchasing/bqls"),
	M0302(9999, "M0302", "300", "M0300", "Branches Requisition Details", "Y","/purchasing/bqls/details"),
	M0303(9999, "M0303", "300", "M0300", "All Open Requisitions of Branches", "Y","/purchasing/bqls/allopenreq"),
	M0304(9999, "M0304", "300", "M0300", "Purchase Order", "Y","/purchasing/poord"),
	M0305(9999, "M0305", "300", "M0300", "Direct GRN", "N","/purchasing/grn"),
	M0306(9999, "M0306", "300", "M0300", "GRN", "Y","/purchasing/pogrn"),
	M0307(9999, "M0307", "300", "M0300", "Supplier Payment", "Y","/purchasing/supplierpayment"),
	M0308(9999, "M0308", "300", "M0300", "Supplier Adjustment", "Y","/procurement/supplieradjustment"),
	M0309(9999, "M0309", "300", "M0300", "Supplier Opening Entry", "Y","/purchasing/supopenentry"),
	M0310(9999, "M0310", "300", "M0300", "GRN Return", "Y","/procurement/grnreturn"),
	M0311(9999, "M0311", "300", "M0300", "Order Requisition", "Y","/purchasing/requisition"),
	M0312(9999, "M0312", "300", "M0300", "PO to GRN", "Y","/procurement/potogrn"),

	M0400(9999, "M0400", "400", "MASTER", "Sales & Invoicing", "Y","/salesninvoice"),
	M0401(9999, "M0401", "400", "M0400", "Sales Orders (Branch)", "Y","/salesninvoice/salesorder"),
	M0402(9999, "M0402", "400", "M0400", "All Open Sales Orders (Branch)", "Y","/salesninvoice/salesorder/allopensalesorder"),
	M0403(9999, "M0403", "400", "M0400", "Sales Orders Matrix (Branch)", "Y","/salesninvoice/salesorder/detailmatrix"),
	M0404(9999, "M0404", "400", "M0400", "Sales Orders Chalan (Branch)", "Y","/salesninvoice/salesorderchalan"),
	M0405(9999, "M0405", "400", "M0400", "Sales Invoice", "Y","/salesninvoice/salesandinvoice"),
	M0406(9999, "M0406", "400", "M0400", "Sales Invoice (Direct)", "Y","/salesninvoice/directsales"),
	M0407(9999, "M0407", "400", "M0400", "Delivery Orders chalan", "Y","/salesninvoice/deliveryorderchalan"),
	M0408(9999, "M0408", "400", "M0400", "Money Receipt", "Y","/salesninvoice/moneyreceipt"),
	M0409(9999, "M0409", "400", "M0400", "Sales Return", "Y","/salesninvoice/returnsales"),
	M0410(9999, "M0410", "400", "M0400", "Customer Adjustment", "Y","/salesninvoice/customeradjustment"),

	M0500(9999, "M0500", "500", "MASTER", "Production", "Y","/production"),
	M0501(9999, "M0501", "500", "M0500", "BOM Entry/Receipe Setting", "Y","/production/bom"),
	M0502(9999, "M0502", "500", "M0500", "Production Planning against sales order (Requisition)", "Y","/production/suggestion"),
	M0503(9999, "M0503", "500", "M0500", "Production Process / Batch", "Y","/production/batch"),

	M0600(9999, "M0600", "600", "MASTER", "Inventory Management", "Y","/inventory"),
	M0601(9999, "M0601", "600", "M0600", "Stock Opening Entry", "Y","/inventory/openingentry"),
	M0602(9999, "M0602", "600", "M0600", "Product Issue Entry", "N","/inventory/productissue"),
	M0603(9999, "M0603", "600", "M0600", "Inventory", "N","/inventory/imtrn"),
	M0604(9999, "M0604", "600", "M0600", "Stock-Take Entry", "Y","/inventory/stocktake"),
	M0605(9999, "M0605", "600", "M0600", "Transfer Order", "Y","/inventory/transferorder"),
	M0606(9999, "M0606", "600", "M0600", "Production Chalan to Transfer Order", "Y","/inventory/pchtotrord"),

	M0700(9999, "M0700", "700", "MASTER", "Report", "Y","/report");

	private int seqn;
	private String code;
	private String group;
	private String parent;
	private String description;
	private String defaultAccess;
	private String menuPath;

	private MenuProfile(int seqn, String code, String group, String parent, String desc, String defaultAccess, String path) {
		this.seqn = seqn;
		this.code = code;
		this.group = group;
		this.parent = parent;
		this.description = desc;
		this.defaultAccess = defaultAccess;
		this.menuPath = path;
	}

	public int getSeqn() {
		return seqn;
	}

	public String getCode() {
		return code;
	}

	public String getGroup() {
		return group;
	}

	public String getParent() {
		return parent;
	}

	public String getDescription() {
		return description;
	}

	public String getDefaultAccess() {
		return defaultAccess;
	}

	public String getMenuPath() {
		return menuPath;
	}
}
