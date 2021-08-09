package com.asl.model.report;

import java.io.Serializable;
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
@XmlRootElement(name = "grnorder")
@XmlAccessorType(XmlAccessType.FIELD)
public class GRNOrder implements Serializable {

	private static final long serialVersionUID = -6915726758526307512L;

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
	private String taxAmount;
	private String discountAmount;
	private String grandTotalAmount;
	private String returnNumber;
	private String vatAit;
	private BigDecimal totalQty;

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<ItemDetails> items = new ArrayList<>();

}
