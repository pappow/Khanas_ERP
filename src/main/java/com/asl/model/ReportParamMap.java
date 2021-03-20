package com.asl.model;

import java.util.HashMap;
import java.util.Map;

import com.asl.enums.ReportParamDataType;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public class ReportParamMap {

	public static final Map<String, String> SUPPLIER_LIST = new HashMap<>();
	public static final Map<String, String> CUSTOMER_LIST = new HashMap<>();
	public static final Map<String, String> STOCK_LIST = new HashMap<>();
	public static final Map<String, String> ITEM_LIST = new HashMap<>();
	public static final Map<String, String> PURCHASEORDER_LIST = new HashMap<>();
	public static final Map<String, String> GRN_LIST = new HashMap<>();
	public static final Map<String, String> PURCHASED_DEVIATION = new HashMap<>();

	public static final Map<String, String> SRS = new HashMap<>();
	public static final Map<String, String> SRSV = new HashMap<>();
	public static final Map<String, String> SRD = new HashMap<>();
	public static final Map<String, String> SRDV = new HashMap<>();
	public static final Map<String, String> CBR = new HashMap<>();
	public static final Map<String, String> CSR = new HashMap<>();
	public static final Map<String, String> IWSS = new HashMap<>();
	public static final Map<String, String> IWSSV = new HashMap<>();
	public static final Map<String, String> SMWSS = new HashMap<>();
	public static final Map<String, String> IWSST = new HashMap<>();
	public static final Map<String, String> IL = new HashMap<>();
	public static final Map<String, String> IPH = new HashMap<>();
	public static final Map<String, String> IPHV = new HashMap<>();
	public static final Map<String, String> CRS = new HashMap<>();
	public static final Map<String, String> CRSV = new HashMap<>();
	public static final Map<String, String> CRD = new HashMap<>();
	public static final Map<String, String> CRDV = new HashMap<>();
	public static final Map<String, String> CCCD = new HashMap<>();
	public static final Map<String, String> CCCDV = new HashMap<>();
	public static final Map<String, String> SET = new HashMap<>();
	public static final Map<String, String> SETV = new HashMap<>();
	public static final Map<String, String> VR = new HashMap<>();
	public static final Map<String, String> SS = new HashMap<>();
	public static final Map<String, String> DR = new HashMap<>();
	public static final Map<String, String> DSR = new HashMap<>();

	public static final Map<String, String> OOSS = new HashMap<>();
	public static final Map<String, String> OHCAS = new HashMap<>();
	public static final Map<String, String> OISS = new HashMap<>();
	public static final Map<String, String> OOSB = new HashMap<>();
	public static final Map<String, String> OBDWAS = new HashMap<>();
	public static final Map<String, String> ODWSS = new HashMap<>();
	public static final Map<String, String> OSS = new HashMap<>();

	static {
		SUPPLIER_LIST.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		SUPPLIER_LIST.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		SUPPLIER_LIST.put("param3", "XORG|" + ReportParamDataType.STRING.name());
		SUPPLIER_LIST.put("param4", "XPHONE|" + ReportParamDataType.STRING.name());
		SUPPLIER_LIST.put("param5", "XGCUS|" + ReportParamDataType.STRING.name());
		SUPPLIER_LIST.put("param6", "XSTATUSCUS|" + ReportParamDataType.STRING.name());

		CUSTOMER_LIST.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		CUSTOMER_LIST.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		CUSTOMER_LIST.put("param3", "XORG|" + ReportParamDataType.STRING.name());
		CUSTOMER_LIST.put("param4", "XPHONE|" + ReportParamDataType.STRING.name());
		CUSTOMER_LIST.put("param5", "XGCUS|" + ReportParamDataType.STRING.name());
		CUSTOMER_LIST.put("param6", "XSTATUSCUS|" + ReportParamDataType.STRING.name());
		
		STOCK_LIST.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		STOCK_LIST.put("param2", "XITEM|" + ReportParamDataType.STRING.name());
		STOCK_LIST.put("param3", "XDESC|" + ReportParamDataType.STRING.name());
		
		ITEM_LIST.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		ITEM_LIST.put("param2", "XITEM|" + ReportParamDataType.STRING.name());
		ITEM_LIST.put("param3", "XDESC|" + ReportParamDataType.STRING.name());
		ITEM_LIST.put("param4", "XCATITEM|" + ReportParamDataType.STRING.name());
		ITEM_LIST.put("param5", "XGITEAM|" + ReportParamDataType.STRING.name());
		ITEM_LIST.put("param6", "XPRODITEM|" + ReportParamDataType.STRING.name());
		
		PURCHASEORDER_LIST.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		PURCHASEORDER_LIST.put("param2", "XCUS|" + ReportParamDataType.STRING.name());
		PURCHASEORDER_LIST.put("param3", "FromDate|" + ReportParamDataType.DATE.name());
		PURCHASEORDER_LIST.put("param4", "ToDate|" + ReportParamDataType.DATE.name());
		
		GRN_LIST.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		GRN_LIST.put("param2", "Xstatusgrn|" + ReportParamDataType.STRING.name());
//		GRN_LIST.put("param3", "Warehouse|" + ReportParamDataType.STRING.name());
		GRN_LIST.put("param3", "FromDate|" + ReportParamDataType.DATE.name());
		GRN_LIST.put("param4", "ToDate|" + ReportParamDataType.DATE.name());
		
		PURCHASED_DEVIATION.put("param1", "ZID|" + ReportParamDataType.STRING.name());
		PURCHASED_DEVIATION.put("param2", "XPORNUM|" + ReportParamDataType.STRING.name());
		
		SRS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SRS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SRS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SRS.put("param4", "div|" + ReportParamDataType.STRING.name());
		SRS.put("param5", "terminal|" + ReportParamDataType.STRING.name());
		SRS.put("param6", "rpttype|" + ReportParamDataType.STRING.name());
		SRS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SRD.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SRD.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SRD.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SRD.put("param4", "div|" + ReportParamDataType.STRING.name());
		SRD.put("param5", "terminal|" + ReportParamDataType.STRING.name());
		SRD.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CBR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		CBR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		CBR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		CBR.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		CBR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CSR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		CSR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		CSR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		CSR.put("param4", "div|" + ReportParamDataType.STRING.name());
		CSR.put("param5", "citem|" + ReportParamDataType.STRING.name());
		CSR.put("param6", "item|" + ReportParamDataType.STRING.name());
		CSR.put("param7", "reptype|" + ReportParamDataType.STRING.name());
		CSR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		IWSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		IWSS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		IWSS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		IWSS.put("param4", "div|" + ReportParamDataType.STRING.name());
		IWSS.put("param5", "item|" + ReportParamDataType.STRING.name());
		IWSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SMWSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SMWSS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SMWSS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SMWSS.put("param4", "div|" + ReportParamDataType.STRING.name());
		SMWSS.put("param5", "item|" + ReportParamDataType.STRING.name());
		SMWSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		IWSST.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		IWSST.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		IWSST.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		IWSST.put("param4", "div|" + ReportParamDataType.STRING.name());
		IWSST.put("param5", "item|" + ReportParamDataType.STRING.name());
		IWSST.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		IL.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		IL.put("param2", "item|" + ReportParamDataType.STRING.name());
		IL.put("param3", "category|" + ReportParamDataType.STRING.name());
		IL.put("param4", "status|" + ReportParamDataType.STRING.name());
		IL.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		IPH.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		IPH.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		IPH.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		IPH.put("param4", "item|" + ReportParamDataType.STRING.name());
		IPH.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CRS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		CRS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		CRS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		CRS.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		CRS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CRD.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		CRD.put("param2", "@puser|" + ReportParamDataType.STRING.name());
		CRD.put("param3", "@pfterminal|" + ReportParamDataType.STRING.name());
		CRD.put("param4", "@ptterminal|" + ReportParamDataType.STRING.name());
		CRD.put("param5", "@fdate|" + ReportParamDataType.DATESTRING.name());
		CRD.put("param6", "@tdate|" + ReportParamDataType.DATESTRING.name());
		CRD.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CCCD.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		CCCD.put("param2", "@user|" + ReportParamDataType.STRING.name());
		CCCD.put("param3", "@terminal|" + ReportParamDataType.STRING.name());
		CCCD.put("param4", "@date|" + ReportParamDataType.DATESTRING.name());
		CCCD.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SET.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SET.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SET.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SET.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		SET.put("param5", "pack|" + ReportParamDataType.STRING.name());
		SET.put("param6", "item|" + ReportParamDataType.STRING.name());
		SET.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		VR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		VR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		VR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		VR.put("param4", "div|" + ReportParamDataType.STRING.name());
		VR.put("param5", "shop|" + ReportParamDataType.STRING.name());
		VR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		DR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		DR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		DR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		DR.put("param4", "div|" + ReportParamDataType.STRING.name());
		DR.put("param5", "terminal|" + ReportParamDataType.STRING.name());
		DR.put("param6", "rpttype|" + ReportParamDataType.STRING.name());
		DR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		DSR.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		DSR.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		DSR.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		DSR.put("param4", "div|" + ReportParamDataType.STRING.name());
		DSR.put("param5", "terminal|" + ReportParamDataType.STRING.name());
		DSR.put("param6", "rpttype|" + ReportParamDataType.STRING.name());
		DSR.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SRSV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SRSV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SRSV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SRSV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		SRSV.put("param5", "rpttype|" + ReportParamDataType.STRING.name());
		SRSV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SRDV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SRDV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SRDV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SRDV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		SRDV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		IWSSV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		IWSSV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		IWSSV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		IWSSV.put("param4", "item|" + ReportParamDataType.STRING.name());
		IWSSV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		IPHV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		IPHV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		IPHV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		IPHV.put("param4", "item|" + ReportParamDataType.STRING.name());
		IPHV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CRSV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		CRSV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		CRSV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		CRSV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		CRSV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CRDV.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		CRDV.put("param2", "@puser|" + ReportParamDataType.STRING.name());
		CRDV.put("param3", "@pfterminal|" + ReportParamDataType.STRING.name());
		CRDV.put("param4", "@ptterminal|" + ReportParamDataType.STRING.name());
		CRDV.put("param5", "@fdate|" + ReportParamDataType.DATESTRING.name());
		CRDV.put("param6", "@tdate|" + ReportParamDataType.DATESTRING.name());
		CRDV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		CCCDV.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		CCCDV.put("param2", "@user|" + ReportParamDataType.STRING.name());
		CCCDV.put("param3", "@terminal|" + ReportParamDataType.STRING.name());
		CCCDV.put("param4", "@date|" + ReportParamDataType.DATESTRING.name());
		CCCDV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		SETV.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		SETV.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		SETV.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		SETV.put("param4", "terminal|" + ReportParamDataType.STRING.name());
		SETV.put("param5", "pack|" + ReportParamDataType.STRING.name());
		SETV.put("param6", "item|" + ReportParamDataType.STRING.name());
		SETV.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		OOSS.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		OOSS.put("param2", "@shop|" + ReportParamDataType.STRING.name());
		OOSS.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
		OOSS.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
		OOSS.put("param5", "@reporttype|" + ReportParamDataType.STRING.name());
		OOSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		OHCAS.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		OHCAS.put("param2", "@shop|" + ReportParamDataType.STRING.name());
		OHCAS.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
		OHCAS.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
		OHCAS.put("param5", "@ftime|" + ReportParamDataType.STRING.name());
		OHCAS.put("param6", "@ttime|" + ReportParamDataType.STRING.name());
		OHCAS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		OISS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		OISS.put("param2", "shop|" + ReportParamDataType.STRING.name());
		OISS.put("param3", "fdate|" + ReportParamDataType.DATE.name());
		OISS.put("param4", "tdate|" + ReportParamDataType.DATE.name());
		OISS.put("param5", "ftime|" + ReportParamDataType.STRING.name());
		OISS.put("param6", "ttime|" + ReportParamDataType.STRING.name());
		OISS.put("param7", "reporttype|" + ReportParamDataType.STRING.name());
		OISS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		OOSB.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		OOSB.put("param2", "@shop|" + ReportParamDataType.STRING.name());
		OOSB.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
		OOSB.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
		OOSB.put("param5", "@reporttype|" + ReportParamDataType.STRING.name());
		OOSB.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		OBDWAS.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		OBDWAS.put("param2", "@shop|" + ReportParamDataType.STRING.name());
		OBDWAS.put("param3", "@fdate|" + ReportParamDataType.DATESTRING.name());
		OBDWAS.put("param4", "@tdate|" + ReportParamDataType.DATESTRING.name());
		OBDWAS.put("param5", "@ftime|" + ReportParamDataType.STRING.name());
		OBDWAS.put("param6", "@ttime|" + ReportParamDataType.STRING.name());
		OBDWAS.put("param7", "@reporttype|" + ReportParamDataType.STRING.name());
		OBDWAS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		ODWSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		ODWSS.put("param2", "fdate|" + ReportParamDataType.DATE.name());
		ODWSS.put("param3", "tdate|" + ReportParamDataType.DATE.name());
		ODWSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		OSS.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		OSS.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
	}
}
