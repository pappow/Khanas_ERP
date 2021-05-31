package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
public enum ProfileType {

	U("U", "User Profile"),
	M("M", "Menu Profile"),
	R("R", "Report Profile"),
	
	//for posbuziness
	C("C", "Casual Dine"),
	F("F", "Fine Dine");

	private String code;
	private String description;

	private ProfileType(String code, String des) {
		this.code = code;
		this.description = des;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
