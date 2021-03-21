package com.asl.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asl.enums.ReportMenu;
import com.asl.enums.ReportParamDataType;
import com.asl.model.RequestParameters;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
@Slf4j
@Controller
@RequestMapping("/report")
public class ReportController extends ASLAbstractController {

	@GetMapping
	public String loadReportPage(Model model) {
		return "pages/report/reportlist";
	}

	@GetMapping("/{menuCode}")
	public String loadReportPage(@PathVariable String menuCode, Model model) {
		ReportMenu rm = null;
		try {
			rm = ReportMenu.valueOf(menuCode);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			return "redirect:/accessdenied";
		}

		model.addAttribute("fieldsList", getReportFieldService(rm).getReportFields());
		model.addAttribute("menuGroup", rm.getGroup());
		model.addAttribute("selectedReport", rm.getCode());
		model.addAttribute("reportCode", rm.getCode().toLowerCase());
		model.addAttribute("reportName", rm.getDescription());
		return "pages/report/report";
	}

	@PostMapping("/print")
	public @ResponseBody String print(RequestParameters params) throws IOException {
		ReportMenu rm = ReportMenu.valueOf(params.getReportCode().toUpperCase());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html"));
		headers.add("X-Content-Type-Options", "nosniff");

		Map<String, Object> reportParams = new HashMap<>();
		for(Map.Entry<String, String> m : rm.getParamMap().entrySet()) {
			String reportParamFieldName = m.getKey();
			String[] arr = m.getValue().split("\\|");
			String cristalReportParamName = arr[0];
			ReportParamDataType paramType = ReportParamDataType.valueOf(arr[1]);
			Object method = RequestParameters.invokeGetter(params, reportParamFieldName);
			convertObjectAndPutIntoMap(cristalReportParamName, paramType, method, reportParams);
		}

		StringBuilder url = new StringBuilder(rm.getReportFile()).append("&__toolbar=false&__showtitle=false&__title=report");
		reportParams.entrySet().parallelStream().forEach(m -> {
			url.append("&" + m.getKey() + "=" + m.getValue());
		});

		return url.toString();
	}

	private void convertObjectAndPutIntoMap(String paramName, ReportParamDataType paramType, Object method, Map<String, Object> reportParams) {
		SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MMM-yyyy");
		SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yyyy");
		switch (paramType) {
			case DATE:
				try {
					reportParams.put(paramName, sdf3.format(sdf.parseObject((String) method)).toUpperCase());
				} catch (ParseException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				break;
			case DATESTRING:
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = (Date) sdf.parseObject((String) method);
					reportParams.put(paramName, sdf2.format(date));
				} catch (ParseException e) {
					log.error(ERROR, e.getMessage(), e);
				}
				break;
			default:
				reportParams.put(paramName, method);
				break;
		}
	}
}
