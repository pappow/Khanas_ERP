package com.asl.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zubayer Ahamed
 * @since Mar 13, 2021
 */
public class TransactionCodes {

	public static final Map<String, String> SUPPLIER_NUMBER = new HashMap<>();
	public static final Map<String, String> CUSTOMER_NUMBER = new HashMap<>();
	public static final Map<String, String> PURCHASE_ORDER = new HashMap<>();
	public static final Map<String, String> REQUISITION_ORDER = new HashMap<>();
	public static final Map<String, String> ITEM_NUMBER = new HashMap<>();
	public static final Map<String, String> GRN_NUMBER = new HashMap<>();
	public static final Map<String, String> PO_GRN_NUMBER = new HashMap<>();
	public static final Map<String, String> INVENTORY_NUMBER = new HashMap<>();
	public static final Map<String, String> INVENTORY_TRANSACTION = new HashMap<>();
	public static final Map<String, String> SALES_ORDER = new HashMap<>();
	public static final Map<String, String> CHALAN_NUMBER = new HashMap<>();
	public static final Map<String, String> EMPLOYEE_ID = new HashMap<>();
	public static final Map<String, String> AP_TRANSACTION = new HashMap<>();
	public static final Map<String, String> AR_TRANSACTION = new HashMap<>();
	public static final Map<String, String> INVENTORY_TRANSFER_ORDER = new HashMap<>();
	public static final Map<String, String> BOM_NUMBER = new HashMap<>();
	public static final Map<String, String> PERSON_ID = new HashMap<>();
	public static final Map<String, String> LAND_ID = new HashMap<>();
	public static final Map<String, String> SURVEYOR_ID = new HashMap<>();
	static {
		
		//AP Transactional Codes
		AP_TRANSACTION.put("ACCOUNT_PAYABLE", "AP-");
		AP_TRANSACTION.put("ACCOUNT_PAYMENT", "PM-");
		AP_TRANSACTION.put("OPENING_ENTRY", "OBAP-");
		
		//AR Transactional Codes
		AR_TRANSACTION.put("Customer Adjustment", "ADAR-");
		AR_TRANSACTION.put("Money Receipt", "MR-");
		

	}
}
