package com.asl.service.report.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asl.service.ASLSessionManager;
import com.asl.service.report.ReportFieldService;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Component
public abstract class AbstractReportService implements ReportFieldService {

	@Autowired protected ASLSessionManager sessionManager;
}
