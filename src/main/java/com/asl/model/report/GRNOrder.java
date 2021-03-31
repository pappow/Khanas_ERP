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
@XmlRootElement(name = "grnorder")
@XmlAccessorType(XmlAccessType.FIELD)
public class GRNOrder {
	
	private String orderNumber;
	private String poNumber;
	private String date;
	private String status;
	private String supplier;
	private String supplierName;
	private String supplierAddress;
	private String warehouse;
	private String totalAmount;
	private String vatAmount;
	private String discountAmount;
	private String grandTotalAmount;
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
