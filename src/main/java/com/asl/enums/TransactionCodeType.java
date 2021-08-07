package com.asl.enums;

import java.util.Map;
import com.asl.model.TransactionCodes;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum TransactionCodeType {

	ACCOUNT_PAYABLE("AR Transactions", "AP--", TransactionCodes.AR_TRANSACTION),
	ACCOUNT_PAYMENT("AR Transactions", "PM--", TransactionCodes.AR_TRANSACTION),
	ACCOUNT_OBAP("AR Transactions", "OBAP", TransactionCodes.AR_TRANSACTION),
	ACCOUNT_OBAR("AR Transactions", "OBAR", TransactionCodes.AR_TRANSACTION),
	ACCOUNT_ADAP("AR Transactions", "ADAP", TransactionCodes.AR_TRANSACTION),
	ACCOUNT_ADAR("AR Transactions", "ADAR", TransactionCodes.AR_TRANSACTION),
	ASSET_TRANSFER_ORDER("ASTO Number", "ASTO", TransactionCodes.ASSET_TRANSFER_ORDER),

	BOM_NUMBER("BOM Number", "BOM-", TransactionCodes.BOM_NUMBER),
	BATCH_NUMBER("Batch Number", "BN--", null),
	BANK_CODE("Bank Code","BNK-",null),

	COMMITTEEINFO_ID("Committee ID", "CMT-", TransactionCodes.COMMITTEEINFO_ID),
	CUSTOMER_NUMBER("Customer Number", "CUS-", TransactionCodes.CUSTOMER_NUMBER),
	CHALAN_NUMBER("CH Number", "CH--", TransactionCodes.CHALAN_NUMBER),

	DOCUMENT_NAME("Document ID", "DOCM--",TransactionCodes.DOCUMENT_ID),

	EMPLOYEE_ID("Employee ID", "EID-", TransactionCodes.EMPLOYEE_ID),

	GRN_NUMBER("GRN Number", "GRN-", TransactionCodes.GRN_NUMBER),
	GL_VOUCHER("GL Voucher", "JV--", TransactionCodes.GL_VOUCHER),

	HALL_BOOKING_SALES_ORDER("Hall Booking SO", "HBSO", null),
	HR_EMPLOYEE_ID("HR Employee ID", "EID-", TransactionCodes.HR_EMPLOYEE_ID),

	ITEM_NUMBER("Item Number", "IC-", TransactionCodes.ITEM_NUMBER),
	INVENTORY_NUMBER("Inventory Number", "IMT-", TransactionCodes.INVENTORY_NUMBER),
	INVENTORY_TRANSACTION("Inventory Transaction", "PO--", TransactionCodes.INVENTORY_TRANSACTION),
	INVENTORY_TRANSACTION2("Inventory Transaction", "RE--", TransactionCodes.INVENTORY_TRANSACTION),
	INVENTORY_TRANSACTION3("Inventory Transaction", "DMIS", TransactionCodes.INVENTORY_TRANSACTION),
	INVENTORY_TRANSFER_ORDER("TO Number", "TO--", TransactionCodes.INVENTORY_TRANSFER_ORDER),

	KEMPLOYEE_ID("Employee Number", "EID-", TransactionCodes.KEMPLOYEE_ID),
	
	LAND_ID("Land ID", "LND-", TransactionCodes.LAND_ID),
	LANDMEMBER_ID("Land Member ID", "MEM-", TransactionCodes.LAND_ID),
	LANDCOMEVENT_ID("Land Com Event", "EVCM", TransactionCodes.LANDCOMEVENT_ID),
	LANDOTHEREVENT_ID("Land Other Event", "EVOH", TransactionCodes.LANDOTHEREVENT_ID),
	LIST_CODE("List Code","LIST",null),

	MONEY_RECEIPTS("AR Transactions", "MR--", TransactionCodes.AR_TRANSACTION),

	PAYROLL_EMPLOYEE_ID("Payroll Employee ID", "EID-", TransactionCodes.PAYROLL_EMPLOYEE_ID),
	PERSON_ID("Persone ID", "PRS-", TransactionCodes.PERSON_ID),
	PURCHASE_ORDER("PO Number", "PO--", TransactionCodes.PURCHASE_ORDER),
	PURCHASE_RETURN("PRN Number", "PRN-", null),
	PROC_ERROR("PROC Error", "ERROR", null),
	PROFILE_LINE("Profile Line", "PRL-", null),
	PROFILE_ALLOCATION("Profile Allocation", "PAL-", null),

	REQUISITION_ORDER("Requisition Order", "REQ-", TransactionCodes.REQUISITION_ORDER),
	ROOM_BOOKING_SALES_ORDER("Room Booking SO", "RBSO-", null),

	SUPPLIER_NUMBER("Supplier Number", "SUP-", TransactionCodes.SUPPLIER_NUMBER),
	SURVEYOR_ID("Surveyor ID", "SRVR--", TransactionCodes.LAND_ID),
	STOCK_TAKE("TAG Number", "TAG-", null),
	SALES_ORDER("SO Number", "SO--", TransactionCodes.SALES_ORDER),
	SALES_AND_INVOICE_NUMBER("DO Number", "DO--", null),
	SALES_RETURN("CRN Number", "SR--", null),

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
