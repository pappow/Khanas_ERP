package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2021
 */
public enum AccountType {

	ASSET("ASSET", "Asset"), 
	LIABILITY("LIABILITY", "Liability"), 
	INCOME("INCOME", "Income"),
	EXPENDITURE("EXPENDITURE", "Expenditure");

	private String code;
	private String description;

	private AccountType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
