package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum TransactionCodeType {

	SUPPLIER_NUMBER("Supplier Number"),
	CUSTOMER_NUMBER("Customer Number");

	private String code;

	private TransactionCodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
