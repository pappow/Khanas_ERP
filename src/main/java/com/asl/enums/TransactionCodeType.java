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
	PURCHASE_ORDER("Purchase Order", "PO--", TransactionCodes.PURCHASE_ORDER),
	REQUISITION_ORDER("Requisition Order", "REQ-", TransactionCodes.REQUISITION_ORDER),
	ITEM_NUMBER("Item Number", "IC-", TransactionCodes.ITEM_NUMBER),
	GRN_NUMBER("GRN Number", "GRN-", TransactionCodes.GRN_NUMBER),
	PRN_NUMBER("PRN Number", "PR--", null),
	INVENTORY_NUMBER("Inventory Number", "IMT-", TransactionCodes.INVENTORY_NUMBER),
	INVENTORY_TRANSACTION("Inventory Transaction", "PO--", TransactionCodes.INVENTORY_TRANSACTION),
	STOCK_TAKE("Stock Take", "TAG-", null),
	SALES_ORDER("Sales Order", "SO-", TransactionCodes.SALES_ORDER),
	CHALAN_NUMBER("Chalan Number", "CH-", TransactionCodes.CHALAN_NUMBER),
	VOUCHER_NUMBER("Voucher Number", "VCH-",null),
	ACCOUNT_PAYABLE("Account Payable", "AP-", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_PAYMENT("Account Payment", "PM-", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_OBAP("Supplier Opening Entry", "OBAP-", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_ADAP("Supplier Adjustment", "ADAP-", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_ADAR("Customer Adjustment", "ADAR-", TransactionCodes.AR_TRANSACTION),
	ACCOUNT_MR("Money Receipt", "MR-", TransactionCodes.AR_TRANSACTION),
	INVENTORY_TRANSFER_ORDER("Stock Transfer Order", "TO-", TransactionCodes.INVENTORY_TRANSFER_ORDER),
	BOM_NUMBER("BOM Number", "BM--", TransactionCodes.BOM_NUMBER),
	BATCH_NUMBER("Batch Number", "BN--", null),
	PROC_ERROR("PROC Error", "ERROR", null),
	SALES_AND_INVOICE_NUMBER("Sales & Invoice Number", "DO--", null),
	TRANSACTION_TRANSFER("Transfer Transaction", "PROD", null);

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
