package com.asl.config;

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
}
