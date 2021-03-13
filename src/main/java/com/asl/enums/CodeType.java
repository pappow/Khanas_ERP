package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum CodeType {

	ITEM_GROUP("Item Group"),
	ITEM_CATEGORY("Item Category"),
	ITEM_SUB_CATEGORY("Item Sub-Category"),
	PURCHASE_UNIT("Purchase Unit"),
	SELLING_UNIT("Selling Unit"),
	CURRENCY_OF_COST("Currency of Cost"),
	CURRENCY_OF_PRICE("Currency of Price"),
	STOCK_TYPE("Stock Type"),
	TAX_CATEGORY("Tax Category"),
	STOCKING_UNIT("Stocking Unit"),
	SUPPLIER_GROUP("Supplier Group"),
	SUPPLIER_STATUS("Supplier Status"),
	CUSTOMER_GROUP("Customer Group"),
	CUSTOMER_STATUS("Customer Status"),
	WAREHOUSE("Warehouse"),
	PURCHASE_ORDER_STATUS("Purchase Order Status"),
	GRN_STATUS("GRN Status"),
	REQUISITION_ORDER_STATUS("Requisition Order Status"),
	PAYMENT_TYPE("Payment Type"),
	CHEQUE_STATUS("Cheque Status"),
	BANK_STATUS("Bank Status"),
	JOURNAL_VOUCHER_STATUS("Journal Voucher Status"),
	PAYMENT_MODE("Payment Mode");
	
	

	private String code;

	private CodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
