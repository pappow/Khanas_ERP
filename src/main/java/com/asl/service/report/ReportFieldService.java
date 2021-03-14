package com.asl.service.report;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Component
public interface ReportFieldService {

	public List<FormFieldBuilder> getReportFields();
}
