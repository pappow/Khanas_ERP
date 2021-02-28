package com.asl.enums;

/**
 * @author Zubayer Ahamed
 * @since Feb 28, 2021
 */
public enum ItemCodeType {

	ITEM_GROUP("Item Group"),
	ITEM_CATEGORY("Item Category"),
	ITEM_SUB_CATEGORY("Item Sub-Category"),
	PURCHASE_UNIT("Purchase Unit"),
	SELLING_UNIT("Selling Unit"),
	CURRENCY_OF_COST("Currency of Cost"),
	CURRENCY_OF_PRICE("Currency of Price"),
	STOCK_TYPE("Stock Type"),
	TAX_CATEGORY("Tax Category"),
	STOCKING_UNIT("Stocking Unit");

	private String code;

	private ItemCodeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
