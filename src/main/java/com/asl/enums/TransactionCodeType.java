package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum TransactionCodeType {

	SUPPLIER_NUMBER("Supplier Number", "SU-"),
	CUSTOMER_NUMBER("Customer Number", "CUS-"),
	PURCHASE_ORDER("Purchase Order", "PO-"),
	REQUISITION_ORDER("Requisition Order", "REQ-"),
	ITEM_NUMBER("Item Number", "IC-"),
	GRN_NUMBER("GRN Number", "GRN-"),
	PO_GRN_NUMBER("PO GRN Number", "POGRN-"),
	INVENTORY_NUMBER("Inventory Number", "INV-"),
	SALES_ORDER("Sales Order", "SO-"),
	CHALAN_NUMBER("Chalan Number", "CH-");

	private String code;
	private String defaultCode;

	private TransactionCodeType(String code, String defaultCode) {
		this.code = code;
		this.defaultCode = defaultCode;
	}

	public String getCode() {
		return this.code;
	}
	
	public String getdefaultCode() {
		return this.defaultCode;
	}
}
