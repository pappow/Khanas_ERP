package com.asl.enums;

import java.util.Map;
import com.asl.model.TransactionCodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum TransactionCodeType {

	ACCOUNT_PAYABLE("Account Payable", "AP-", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_PAYMENT("Account Payment", "PM--", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_OBAP("Supplier Opening Entry", "OBAP", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_OBAR("Customer Opening Entry", "OBAR", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_ADAP("Supplier Adjustment", "ADAP-", TransactionCodes.AP_TRANSACTION),
	ACCOUNT_ADAR("Customer Adjustment", "ADAR-", TransactionCodes.AR_TRANSACTION),
	AGENT_TRANSFER_ORDER("Agent Transfer Order", "ASTO", TransactionCodes.AGENT_TRANSFER_ORDER),

	BOM_NUMBER("BOM Number", "BM--", TransactionCodes.BOM_NUMBER),
	BATCH_NUMBER("Batch Number", "BN--", null),
	BANK_CODE("Bank Code","BNK-",null),

	COMMITTEEINFO_ID("Committee ID", "CMT-", TransactionCodes.COMMITTEEINFO_ID),
	CUSTOMER_NUMBER("Customer", "CUS-", TransactionCodes.CUSTOMER_NUMBER),
	CHALAN_NUMBER("Chalan Number", "CH--", TransactionCodes.CHALAN_NUMBER),

	DOCUMENT_NAME("Document ID", "DOCM--",TransactionCodes.DOCUMENT_ID),

	EMPLOYEE_ID("Employee ID", "EID-", TransactionCodes.EMPLOYEE_ID),

	GRN_NUMBER("GRN Number", "GRN-", TransactionCodes.GRN_NUMBER),
	GL_VOUCHER("GL Voucher", "JV--", TransactionCodes.GL_VOUCHER),

	HALL_BOOKING_SALES_ORDER("Hall Booking SO", "HBSO", null),
	HR_EMPLOYEE_ID("HR Employee ID", "EID-", TransactionCodes.HR_EMPLOYEE_ID),

	ITEM_NUMBER("Item Number", "IC-", TransactionCodes.ITEM_NUMBER),
	INVENTORY_NUMBER("Inventory Number", "IMT-", TransactionCodes.INVENTORY_NUMBER),
	INVENTORY_TRANSACTION("Inventory Transaction", "PO--", TransactionCodes.INVENTORY_TRANSACTION),
	INVENTORY_TRANSFER_ORDER("Stock Transfer Order", "TO-", TransactionCodes.INVENTORY_TRANSFER_ORDER),

	LAND_ID("Land ID", "LND-", TransactionCodes.LAND_ID),
	LANDMEMBER_ID("Land Member ID", "MEM-", TransactionCodes.LAND_ID),
	LANDCOMEVENT_ID("Land Com Event", "EVCM", TransactionCodes.LANDCOMEVENT_ID),
	LANDOTHEREVENT_ID("Land Other Event", "EVOH", TransactionCodes.LANDOTHEREVENT_ID),
	LIST_CODE("List Code","LIST",null),

	MONEY_RECEIPTS("AR Transactions", "MR--", TransactionCodes.AR_TRANSACTION),

	PERSON_ID("Persone ID", "PRS-", TransactionCodes.PERSON_ID),
	PURCHASE_ORDER("Purchase Order", "PO--", TransactionCodes.PURCHASE_ORDER),
	PRN_NUMBER("PRN Number", "PR--", null),
	PROC_ERROR("PROC Error", "ERROR", null),
	PROFILE_LINE("Profile Line", "PRL-", null),
	PROFILE_ALLOCATION("Profile Allocation", "PAL-", null),

	REQUISITION_ORDER("Requisition Order", "REQ-", TransactionCodes.REQUISITION_ORDER),
	ROOM_BOOKING_SALES_ORDER("Room Booking SO", "RBSO-", null),

	SUPPLIER_NUMBER("Supplier", "SU-", TransactionCodes.SUPPLIER_NUMBER),
	SURVEYOR_ID("Surveyor ID", "SRVR--", TransactionCodes.LAND_ID),
	STOCK_TAKE("Stock Take", "TAG-", null),
	SALES_ORDER("Sales Order", "SO--", TransactionCodes.SALES_ORDER),
	SALES_AND_INVOICE_NUMBER("Sales & Invoice Number", "DO--", null),
	SRN_RETURN("Sales Return", "SR--", null),

	TRANSACTION_TRANSFER("Transfer Transaction", "PROD", null),

	VOUCHER_NUMBER("Voucher Number", "VCH-",null);
	

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
