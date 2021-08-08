package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
public enum MenuProfile {

	M0100(9999, "M0100", "100","MASTER", "Administration", "Y","/system"),
		M0101(9999, "M0101", "100","SUB-MASTER", "Users", "Y", null),
			M0102(9999, "M0102", "100","M0101", "Manage Users", "Y","/system/xusers"),
			M0103(9999, "M0103", "100","M0101", "Manage Access Profile", "Y","/system/profile"),
			M0104(9999, "M0104", "100","M0101", "Profile Allocation to User", "Y","/system/profileallocation"),
		M0105(9999, "M0105", "100","SUB-MASTER", "System Administration", "Y", null),
			M0106(9999, "M0106", "100", "M0105", "Codes & Parameters", "Y","/mastersetup/cap"),
			M0107(9999, "M0107", "100", "M0105", "Transaction Codes", "Y","/mastersetup/xtrn"),
			M0108(9999, "M0108", "100", "M0105", "List Maintenance", "Y","/system/list"),
			M0109(9999, "M0109", "100", "M0105", "Business Profile", "Y","/system/businessprof"),
		M0110(9999, "M0110", "100","SUB-MASTER", "Logs", "Y", null),
			M0111(9999, "M0111", "100", "M0110", "Error Logs", "Y","/system/aslprocerror"),


	M0200(9999, "M0200", "200", "MASTER", "Master Setup", "Y","/mastersetup"),
		M0201(9999, "M0201", "200", "M0200", "Item Master", "Y","/mastersetup/caitem"),
		M0202(9999, "M0202", "200", "M0200", "Supplier Master", "Y","/mastersetup/cacus"),
		M0203(9999, "M0203", "200", "M0200", "Customer Master", "Y","/mastersetup/cacus"),
		M0204(9999, "M0204", "200", "M0200", "Employee Information", "Y","/employeeinfo"),
		M0205(9999, "M0205", "200", "M0200", "BOM/Recipe Master", "Y","/production/bom"),
		M0206(9999, "M0206", "200", "M0200", "Bank Entry", "Y","/account/bankentry"),


	M0300(9999, "M0300", "300", "MASTER", "Procurement", "Y","/procurements"),
		M0301(9999, "M0301", "300", "M0300", "Purchase Order", "Y","/procurements/poord"),
		M0302(9999, "M0302", "300", "M0300", "GRN", "Y","/procurements/pogrn"),
		M0303(9999, "M0303", "300", "M0300", "Purchase Return", "Y","/procurements/purchasereturn"),
		M0304(9999, "M0304", "300", "M0300", "Purchase Requisition", "Y","/procurement/requisition"),
		M0305(9999, "M0305", "300", "M0300", "Packaging Item Requisition", "Y","/procurement/requisition?reqtype=Packaging Item"),
		M0306(9999, "M0306", "300", "M0300", "All Order Requisitions", "Y","/procurement/requisition/allrequisition"),
		M0307(9999, "M0307", "300", "M0300", "Direct Purchase", "Y","/procurements/pogrndirect"),


	M0400(9999, "M0400", "400", "MASTER", "Sales & Invoice", "Y","/salesninvoice"),
		M0401(9999, "M0401", "400","SUB-MASTER", "Branch Requisition", "Y", null),
			M0402(9999, "M0402", "400", "M0401", "Requisitions Matrix", "Y","/purchasing/bqls/details"),
			M0403(9999, "M0403", "400", "M0401", "Requisitions to Sales Order", "Y","/purchasing/bqls"),
		M0404(9999, "M0404", "400", "M0400", "Sales Orders", "Y","/salesninvoice/opord"),
		M0405(9999, "M0405", "400", "M0400", "Sales Orders Matrix", "Y","/salesninvoice/salesorder/detailmatrix"),
		M0406(9999, "M0406", "400", "M0400", "Invoice Entry", "Y","/salesninvoice/salesandinvoice"),
		M0407(9999, "M0407", "400", "M0400", "Delivery Chalan (Group)", "Y","/salesninvoice/deliveryorderchalan"),
		M0408(9999, "M0408", "400", "M0400", "Sales Return", "Y","/salesninvoice/salesreturn"),
		M0409(9999, "M0409", "400","SUB-MASTER", "Report", "Y", null),
			M0410(9999, "M0410", "400", "M0409", "Daily Distribution Report", "Y","/salesninvoice/ddisr"),

	M0500(9999, "M0500", "500", "MASTER", "Production", "Y","/production"),
		M0502(9999, "M0502", "500", "M0500", "Production Planning", "Y","/production/suggestion"),
		M0503(9999, "M0503", "500", "M0500", "Production Process", "Y","/production/batch"),
		M0504(9999, "M0504", "500", "M0500", "Production Chalan", "Y","/salesninvoice/salesorderchalan"),

	M0600(9999, "M0600", "600", "MASTER", "Inventory", "Y","/inventory"),
		M0601(9999, "M0601", "600", "M0600", "Stock Opening Entry", "Y","/inventory/openingentry"),
		M0602(9999, "M0602", "600", "M0600", "Stock Take", "Y","/inventory/stocktake"),
		M0603(9999, "M0603", "600", "M0600", "Store Transfer Order", "Y","/inventory/transferorder"),
		M0604(9999, "M0604", "600", "M0600", "Asset Transfer to Branch", "Y","/inventory/asset"),
		M0605(9999, "M0605", "600", "M0600", "Stock List", "Y","/inventory/stocklist"),

	M0700(9999, "M0700", "700", "MASTER", "Report", "Y","/report"),

	M0800(9999, "M0800", "800", "MASTER", "Accounts Receivable", "Y", null),
		M0801(9999, "M0801", "800", "M0800", "Money Receipt", "Y", "/salesninvoice/moneyreceipt"),
		M0802(9999, "M0802", "800", "M0800", "Customer Adjustment", "Y", "/cusadjustment"),
		M0803(9999, "M0803", "800", "M0800", "Customer Opening", "Y", "/cusopening"),

	M0900(9999, "M0900", "900", "MASTER", "Accounts Payable", "Y", null),
		M0901(9999, "M0901", "900", "M0900", "Supplier Payment", "Y", "/purchasing/supplierpayment"),
		M0902(9999, "M0902", "900", "M0900", "Supplier Adjustment", "Y", "/supplieradjustment"),
		M0903(9999, "M0903", "900", "M0900", "Supplier Opening", "Y", "/supopening");
		

	
	
//	M0602(9999, "M0602", "600", "M0600", "Product Issue Entry", "N","/inventory/productissue"),
//	M0603(9999, "M0603", "600", "M0600", "Inventory", "N","/inventory/imtrn"),
//	M0606(9999, "M0606", "600", "M0600", "Production Chalan to Transfer Order", "Y","/inventory/pchtotrord"),
	
			
			
			
			//	
//	
//	M0303(9999, "M0303", "300", "M0300", "Open Requisitions (From Branch)", "Y","/purchasing/bqls/allopenreq"),
	
//	M0305(9999, "M0305", "300", "M0300", "Direct GRN", "N","/purchasing/grn"),
//	
//	M0307(9999, "M0307", "300", "M0300", "Supplier Payment", "Y","/purchasing/supplierpayment"),
//	M0308(9999, "M0308", "300", "M0300", "Supplier Adjustment", "Y","/procurement/supplieradjustment"),
//	M0309(9999, "M0309", "300", "M0300", "Supplier Opening", "Y","/purchasing/supopenentry"),
//	M0310(9999, "M0310", "300", "M0300", "GRN Return", "Y","/procurement/grnreturn"),
//	M0311(9999, "M0311", "300", "M0300", "Purchase Requisition", "Y","/procurement/allrequisition"),
//	M0312(9999, "M0312", "300", "M0300", "PO to GRN", "Y","/procurement/potogrn"),
//	M0313(9999, "M0313", "300", "M0300", "All Order Requisitions", "Y","/procurement/requisition/allrequisition"),

	
	
//	M0402(9999, "M0402", "400", "M0400", "All Open Sales Orders (Branch)", "Y","/salesninvoice/salesorder/allopensalesorder"),
//	M0403(9999, "M0403", "400", "M0400", "Sales Orders Matrix (Branch)", "Y","/salesninvoice/salesorder/detailmatrix"),
//	M0404(9999, "M0404", "400", "M0400", "Sales Orders Chalan (Branch)", "Y","/salesninvoice/salesorderchalan"),
//	
//	M0406(9999, "M0406", "400", "M0400", "Sales Invoice (Direct)", "Y","/salesninvoice/directsales"),
//	
//	M0408(9999, "M0408", "400", "M0400", "Money Receipt", "Y","/salesninvoice/moneyreceipt"),
//	M0409(9999, "M0409", "400", "M0400", "Sales Return", "Y","/salesninvoice/returnsales"),
//	M0410(9999, "M0410", "400", "M0400", "Customer Adjustment", "Y","/salesninvoice/customeradjustment"),

	

	

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
