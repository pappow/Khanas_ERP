package com.asl.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class RequisitionList {

	private String xpornum;
	private String zorg;
	private BigDecimal xtotamt;
	private String xstatuspor;
	private Date xdate;
}
