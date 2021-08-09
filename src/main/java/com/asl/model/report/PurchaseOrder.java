package com.asl.model.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "purchaseorder")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrder {
	
	private String orderNumber;
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
	private BigDecimal totalQty;
	
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
