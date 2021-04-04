package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
public enum MenuProfile {

	M0100(9999, "M0100", "System Administration", "Y","/system"),

	M0200(9999, "M0200", "Master Setup", "Y","/mastersetup"),
	M0201(9999, "M0201", "Party Master", "Y","/mastersetup/partymaster"),
	M0202(9999, "M0202", "Item Master", "Y","/mastersetup/itemmaster"),
	M0203(9999, "M0203", "Code & Parameters", "Y","/mastersetup/cap"),
	M0204(9999, "M0204", "Vat & Tax", "Y","/mastersetup/vataitmaster"),
	M0205(9999, "M0205", "Supplier Entry", "Y","/mastersetup/cacus"),
	M0206(9999, "M0206", "Customer Entry", "Y","/mastersetup/cacus"),
	M0207(9999, "M0207", "Item Entry", "Y","/mastersetup/caitem"),
	M0208(9999, "M0208", "Codes", "Y","/mastersetup/xcodes"),
	M0209(9999, "M0209", "Transaction Codes", "Y","/mastersetup/xtrn"),
	M0210(9999, "M0210", "Vat & Tax Entry", "Y","/mastersetup/vatait"),

	M0300(9999, "M0300", "Procurements", "Y","/purchasing"),
	M0301(9999, "M0301", "Branches Requisition", "Y","/purchasing/bqls"),
	M0302(9999, "M0302", "Branches Requisition Details", "Y","/purchasing/bqls/details"),
	M0303(9999, "M0303", "All Open Requisitions of Branches", "Y","/purchasing/bqls/allopenreq"),
	M0304(9999, "M0304", "Purchase Order", "Y","/purchasing/poord"),
	M0305(9999, "M0305", "Direct GRN", "Y","/purchasing/grn"),
	M0306(9999, "M0306", "Purchase Order GRN", "Y","/purchasing/pogrn"),
	M0307(9999, "M0307", "Supplier Payment", "Y","/purchasing/supplierpayment"),
	M0308(9999, "M0308", "Supplier Adjustment", "Y","/procurement/supplieradjustment"),
	M0309(9999, "M0309", "Supplier Opening Entry", "Y","/purchasing/supopenentry"),
	M0310(9999, "M0310", "GRN Return", "Y","/procurement/grnreturn"),

	M0400(9999, "M0400", "Sales & Invoicing", "Y","/salesninvoice"),
	M0401(9999, "M0401", "Sales Orders (Branch)", "Y","/salesninvoice/salesorder"),
	M0402(9999, "M0402", "All Open Sales Orders (Branch)", "Y","/salesninvoice/salesorder/allopensalesorder"),
	M0403(9999, "M0403", "Sales Orders Matrix (Branch)", "Y","/salesninvoice/salesorder/detailmatrix"),
	M0404(9999, "M0404", "Sales Orders Chalan (Branch)", "Y","/salesninvoice/salesorderchalan"),
	M0405(9999, "M0405", "Sales Invoice", "Y","/salesninvoice/salesandinvoice"),
	M0406(9999, "M0406", "Sales Invoice (Direct)", "Y","/salesninvoice/directsales"),
	M0407(9999, "M0407", "Delivery Orders chalan", "Y","/salesninvoice/deliveryorderchalan"),
	M0408(9999, "M0408", "Money Receipt", "Y","/salesninvoice/moneyreceipt"),
	M0409(9999, "M0409", "Sales Return", "Y","/salesninvoice/returnsales"),
	M0410(9999, "M0410", "Customer Adjustment", "Y","/salesninvoice/customeradjustment"),

	M0500(9999, "M0500", "Production", "Y","/production"),
	M0501(9999, "M0501", "BOM Entry/Receipe Setting", "Y","/production/bom"),
	M0502(9999, "M0502", "Production Planning against sales order (Requisition)", "Y","/production/suggestion"),
	M0503(9999, "M0503", "Production Process / Batch", "Y","/production/batch"),

	M0600(9999, "M0600", "Inventory Management", "Y","/inventory"),
	M0601(9999, "M0601", "Stock Opening Entry", "Y","/inventory/openingentry"),
	M0602(9999, "M0602", "Product Issue Entry", "Y","/inventory/productissue"),
	M0603(9999, "M0603", "Inventory", "Y","/inventory/imtrn"),
	M0604(9999, "M0604", "Stock-Take Entry", "Y","/inventory/stocktake"),
	M0605(9999, "M0605", "Transfer Order", "Y","/inventory/transferorder"),

	M0700(9999, "M0700", "Report", "Y","/report");

	private int seqn;
	private String code;
	private String description;
	private String defaultAccess;
	private String menuPath;

	private MenuProfile(int seqn, String code, String desc, String defaultAccess, String path) {
		this.seqn = seqn;
		this.code = code;
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
