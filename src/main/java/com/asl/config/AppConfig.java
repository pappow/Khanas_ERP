package com.asl.config;

import java.io.File;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
@Service
public class AppConfig {

	@Value("${default.logo-name:asl.png}")
	private String defaultLogoFileName;
	
	@Value("${default.report.template-path}")
	private String reportTemplatepath;

	@Value("${default.site-title:ASL <span class=\"title-divider\">POS Report</span>}")
	protected String defaultSystemName;

	@Value("${default.im-ex-path:/asl/exportimport/}")
	private String defaultExportImportPath;

	@Value("${default.copyright-text:Powered by <a href=\"http://www.automationservicesbd.com\">ASL</a> &copy; Copyright}")
	private String copyRightText;

	@Value("${default.allow-system-admin:N}")
	private String allowSystemAdmin;

	@Value("${default.birtUrl:http://localhost:8080/birt/frameset}")
	private String birtUrl;

	@Value("${default.report.xsl-path}")
	private String xslPath;

	@Value("${default.report.saved-xml-path}")
	private String savedXmlPath;

	@Value("${default.document-path}")
	private String documentPath;

	//@Value("${default.report.logo}")
	private String reportLogo;

	public String getReportLogo() {
		try {
			return new StringBuilder(this.getClass().getClassLoader().getResource("static").toURI().getPath())
			.append(File.separator).append("img").append(File.separator)
			.append("khanas_logo.png").toString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
