package com.asl.service;

import java.io.BufferedInputStream;
import java.util.Map;

import com.asl.enums.ReportType;

/**
 * @author Zubayer Ahamed
 *
 */
public interface PrintingService {

	public BufferedInputStream getDataBytes(String reportName, String reportTitle, boolean attachment, Map<String, Object> reportParams, ReportType reportType);
//	public void changeDataSource(ReportClientDocument clientDoc, String reportName, String tableName, DBConfig dbConfig) throws ReportSDKException;
//	public void logonDataSource(ReportClientDocument clientDoc, DBConfig dbConfig) throws ReportSDKException;
//	public void addDiscreteParameterValue(ReportClientDocument clientDoc, String reportName, String parameterName, Object newValue) throws ReportSDKException;
}
