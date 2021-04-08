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

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
@XmlRootElement(name = "RM0301")
@XmlAccessorType(XmlAccessType.FIELD)
public class RM0301Report extends AbstractReportModel {

	@XmlElementWrapper(name = "suppliers")
	@XmlElement(name = "supplier")
	private List<RM0301Supplier> suppliers = new ArrayList<>();

	private BigDecimal totalQtyOrder;
	private BigDecimal totalQtyPurchased;
	private BigDecimal totalAmount;
}
