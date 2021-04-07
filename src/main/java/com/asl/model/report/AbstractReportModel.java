package com.asl.model.report;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 7, 2021
 */
@Data
public class AbstractReportModel {

	private String businessName;
	private String businessAddress;
	private String reportName;
	private String fromDate;
	private String toDate;
	private String printDate;
	private String copyrightText;
}
