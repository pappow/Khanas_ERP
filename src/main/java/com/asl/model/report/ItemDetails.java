package com.asl.model.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemDetails {

	private String itemCode;
	private String itemName;
	private String itemQty;
	private String itemUnit;
	private String itemCategory;
	private String itemGroup;
	
	//Added by Sajjad
	private String itemRate;
	private String itemTotalAmount;
}
