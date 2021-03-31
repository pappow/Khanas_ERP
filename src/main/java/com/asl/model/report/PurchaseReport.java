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
@XmlRootElement(name = "purchasereport")
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseReport {
	
	private String businessName;
	private String businessAddress;
	private String reportName;
	private String fromDate;
	private String toDate;
	private String printDate;
	private String copyrightText;
	private String status;

	@XmlElementWrapper(name = "purchaseorders")
	@XmlElement(name = "purchaseorder")
	private List<PurchaseOrder> purchaseorders = new ArrayList<>();

}
