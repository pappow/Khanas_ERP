package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum CodeType {

	BANK_STATUS("Bank Status"),
	BLOOD_GROUP("Blood Group"),

	CURRENCY_OF_COST("Currency of Cost"),
	CURRENCY_OF_PRICE("Currency of Price"),
	CUSTOMER_GROUP("Customer Group"),
	CUSTOMER_STATUS("Customer Status"),
	CHEQUE_STATUS("Cheque Status"),

	DISCOUNT_TYPE("Discount Type"),

	EMPLOYEE_CATEGORY("Employee Category"),
	EMPLOYEE_DEPARTMENT("Employee Department"),
	EMPLOYEE_DESIGNATION("Employee Designation"),
	EMPLOYEE_STATUS("Employee Status"),
	EMPLOYEE_TYPE("Employment Type"),

	GRN_STATUS("GRN Status"),
	GRN_RETURN_STATUS("GRN Return Status"),

	JOURNAL_VOUCHER_STATUS("Journal Voucher Status"),

	ITEM_GROUP("Item Group"),
	//Land Experience Person Start
	EXPERIENCE_TRANSACTION_TYPE("Experience Transaction type"),
	EXPERIENCE_TYPE("Experience type"),
	//Land Experience Person Start End
		
	ITEM_CATEGORY("Item Category"),
	ITEM_SUB_CATEGORY("Item Sub-Category"),

	OBJECT_TYPE("Object Type"),
	OWNER_TYPE("Owner Type"),

	PURCHASE_UNIT("Purchase Unit"),
	PURCHASE_ORDER_STATUS("Purchase Order Status"),
	PAYMENT_TYPE("Payment Type"),
	PAYMENT_MODE("Payment Mode"),

	REQUISITION_ORDER_STATUS("Requisition Order Status"),
	RELIGION("Religion"),
	ROLE_TYPE("Role Type"),

	SELLING_UNIT("Selling Unit"),
	STOCK_TYPE("Stock Type"),
	STOCKING_UNIT("Stocking Unit"),
	SUPPLIER_GROUP("Supplier Group"),
	SUPPLIER_STATUS("Supplier Status"),
	STATUS("Status"),
	STATUS_TYPE("Status Type"),
	SALES_AND_INVOICE_STATUS("Sales & Invoice Status"),
	SEX("Sex"),

	TAX_CATEGORY("Tax Category"),
	TRANSFER_ORDER_STATUS("Transfer Order Status"),
	TAG_STATUS("TAG Status"),

	WAREHOUSE("Warehouse");

	private String code;

	private CodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
