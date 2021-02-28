package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum TransactionCodeType {

	SUPPLIER_GROUP("Supplier Group"),
	SUPPLIER_STATUS("Supplier Status"),
	SUPPLIER_NUMBER("Supplier Number"),
	CUSTOMER_NUMBER("Customer Number"),
	CUSTOMER_GROUP("Customer Group"),
	CUSTOMER_STATUS("Customer Status");

	private String code;

	private TransactionCodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
