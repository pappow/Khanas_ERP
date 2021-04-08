package com.asl.model;

import java.util.HashMap;
import java.util.Map;

import com.asl.enums.ReportParamDataType;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public class ReportParamMap {

	// Procurements  -- M0300
	public static final Map<String, String> RM0301 = new HashMap<>();
	public static final Map<String, String> RM0302 = new HashMap<>();
	public static final Map<String, String> RM0303 = new HashMap<>();
	public static final Map<String, String> RM0304 = new HashMap<>();
	public static final Map<String, String> RM0305 = new HashMap<>();
	public static final Map<String, String> RM0306 = new HashMap<>();
	public static final Map<String, String> RM0307 = new HashMap<>();
	public static final Map<String, String> RM0308 = new HashMap<>();
	public static final Map<String, String> RM0309 = new HashMap<>();
	public static final Map<String, String> RM0310 = new HashMap<>();
	public static final Map<String, String> RM0311 = new HashMap<>();

	// Sales & Invoicing  -- M0400
	public static final Map<String, String> RM0401 = new HashMap<>();
	public static final Map<String, String> RM0402 = new HashMap<>();
	public static final Map<String, String> RM0403 = new HashMap<>();
	public static final Map<String, String> RM0404 = new HashMap<>();
	public static final Map<String, String> RM0405 = new HashMap<>();
	public static final Map<String, String> RM0406 = new HashMap<>();
	public static final Map<String, String> RM0407 = new HashMap<>();
	public static final Map<String, String> RM0408 = new HashMap<>();
	public static final Map<String, String> RM0409 = new HashMap<>();

	// Production  -- M0500
	public static final Map<String, String> RM0501 = new HashMap<>();
	public static final Map<String, String> RM0502 = new HashMap<>();
	public static final Map<String, String> RM0503 = new HashMap<>();
	public static final Map<String, String> RM0504 = new HashMap<>();

	// Inventory  -- M0600
	public static final Map<String, String> RM0601 = new HashMap<>();
	public static final Map<String, String> RM0602 = new HashMap<>();
	public static final Map<String, String> RM0603 = new HashMap<>();
	public static final Map<String, String> RM0604 = new HashMap<>();
	public static final Map<String, String> RM0605 = new HashMap<>();
	public static final Map<String, String> RM0606 = new HashMap<>();
	public static final Map<String, String> RM0607 = new HashMap<>();

	static {
		RM0301.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0301.put("param2", "FDATE|" + ReportParamDataType.DATESTRING.name());
		RM0301.put("param3", "TDATE|" + ReportParamDataType.DATESTRING.name());
		RM0301.put("param4", "XSTATUSPOR|" + ReportParamDataType.STRING.name());
		RM0301.put("param5", "XCUS|" + ReportParamDataType.STRING.name());
		RM0301.put("param6", "XITEM|" + ReportParamDataType.STRING.name());

		RM0311.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0311.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		RM0311.put("param3", "XORG|" + ReportParamDataType.STRING.name());
		RM0311.put("param4", "XPHONE|" + ReportParamDataType.STRING.name());
		RM0311.put("param5", "XGCUS|" + ReportParamDataType.STRING.name());
		RM0311.put("param6", "XSTATUSCUS|" + ReportParamDataType.STRING.name());

		RM0408.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0408.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		RM0408.put("param3", "XORG|" + ReportParamDataType.STRING.name());
		RM0408.put("param4", "XPHONE|" + ReportParamDataType.STRING.name());
		RM0408.put("param5", "XGCUS|" + ReportParamDataType.STRING.name());
		RM0408.put("param6", "XSTATUSCUS|" + ReportParamDataType.STRING.name());

		RM0601.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0601.put("param2", "XITEM|" + ReportParamDataType.STRING.name());
		RM0601.put("param3", "XWH|" + ReportParamDataType.STRING.name());

		RM0607.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0607.put("param2", "XITEM|" + ReportParamDataType.STRING.name());
		RM0607.put("param3", "XDESC|" + ReportParamDataType.STRING.name());
		RM0607.put("param4", "XCAITEM|" + ReportParamDataType.STRING.name());
		RM0607.put("param5", "XGITEM|" + ReportParamDataType.STRING.name());
		RM0607.put("param6", "XPRODITEM|" + ReportParamDataType.STRING.name());

		RM0304.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0304.put("param2", "XPORNUM|" + ReportParamDataType.STRING.name());

		RM0305.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0305.put("param2", "XWH|" + ReportParamDataType.STRING.name());
		RM0305.put("param3", "SUP|" + ReportParamDataType.STRING.name());
		RM0305.put("param4", "XGRNSTATUS|" + ReportParamDataType.STRING.name());
		RM0305.put("param5", "FromDate|" + ReportParamDataType.DATE.name());
		RM0305.put("param6", "ToDate|" + ReportParamDataType.DATE.name());

		RM0302.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0302.put("param2", "CHALAN|" + ReportParamDataType.STRING.name());

		RM0307.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0307.put("param2", "FromDate|" + ReportParamDataType.DATE.name());
		RM0307.put("param3", "ToDate|" + ReportParamDataType.DATE.name());

		RM0405.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0405.put("param2", "FromDate|" + ReportParamDataType.DATE.name());
		RM0405.put("param3", "ToDate|" + ReportParamDataType.DATE.name());

		RM0501.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0501.put("param2", "BOM|" + ReportParamDataType.STRING.name());

		RM0303.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0303.put("param2", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0303.put("param3", "FromDate|" + ReportParamDataType.DATE.name());
		RM0303.put("param4", "ToDate|" + ReportParamDataType.DATE.name());

		RM0401.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0401.put("param2", "XPORNUM|" + ReportParamDataType.STRING.name());
		RM0401.put("param3", "FromDate|" + ReportParamDataType.DATE.name());
		RM0401.put("param4", "ToDate|" + ReportParamDataType.DATE.name());

		RM0310.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		RM0310.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
	}
}
