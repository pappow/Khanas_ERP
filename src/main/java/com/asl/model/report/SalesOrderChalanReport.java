package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2021
 */
@Data
@XmlRootElement(name = "chalanreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesOrderChalanReport {

	private String businessName;
	private String businessAddress;
	private String reportName;
	private String fromDate;
	private String toDate;
	private String printDate;
	private String copyrightText;

	private String chalanNumber;
	private String chalanDate;
	private String chalanStatus;

	@XmlElementWrapper(name = "salesorders")
	@XmlElement(name = "salesorder")
	private List<SalesOrder> salesorders = new ArrayList<>();
}
