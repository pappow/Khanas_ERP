package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "salesorder")
@XmlAccessorType(XmlAccessType.FIELD)
public class GrnReport {
	
	private String grnNumber;
	private String poNumber;
	private String supplierCode;
	private String supplierName;
	private String date;
	private String status;
	private String warehouse;
	

	private String totalAmount;
	private String vatAmount;
	private String discountAmount;
	private String grandTotalAmount;
	private String customer;
	private String customerAddress;
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
