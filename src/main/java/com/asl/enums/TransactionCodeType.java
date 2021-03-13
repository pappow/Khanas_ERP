package com.asl.enums;

import java.util.Map;
import com.asl.model.TransactionCodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum TransactionCodeType {

	SUPPLIER_NUMBER("Supplier Number", "SU-", TransactionCodes.SUPPLIER_NUMBER),
	CUSTOMER_NUMBER("Customer Number", "CUS-", TransactionCodes.CUSTOMER_NUMBER),
	PURCHASE_ORDER("Purchase Order", "PO-", TransactionCodes.PURCHASE_ORDER),
	REQUISITION_ORDER("Requisition Order", "REQ-", TransactionCodes.REQUISITION_ORDER),
	ITEM_NUMBER("Item Number", "IC-", TransactionCodes.ITEM_NUMBER),
	GRN_NUMBER("GRN Number", "GRN-", TransactionCodes.GRN_NUMBER),
	PO_GRN_NUMBER("PO GRN Number", "POGRN-", TransactionCodes.PO_GRN_NUMBER),
	INVENTORY_NUMBER("Inventory Number", "IMT-", TransactionCodes.INVENTORY_NUMBER),
	SALES_ORDER("Sales Order", "SO-", TransactionCodes.SALES_ORDER),
	CHALAN_NUMBER("Chalan Number", "CH-", TransactionCodes.CHALAN_NUMBER);

	private String code;
	private String defaultCode;
	private Map<String, String> xtrnCodes;

	private TransactionCodeType(String code, String defaultCode, Map<String, String> xtrnCodes) {
		this.code = code;
		this.defaultCode = defaultCode;
		this.xtrnCodes = xtrnCodes;
	}

	public String getCode() {
		return this.code;
	}
	
	public String getdefaultCode() {
		return this.defaultCode;
	}

	public Map<String, String> getXtrnCodes(){
		return this.xtrnCodes;
	}
}
