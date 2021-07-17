package com.asl.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asl.config.AppConfig;
import com.asl.entity.Arhed;
import com.asl.entity.Zbusiness;
import com.asl.service.ASLSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public abstract class AbstractGenericService  {

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	protected static final String ERROR = "Error is {}, {}";
	@Autowired protected ASLSessionManager sessionManager;
	@Autowired protected AppConfig appConfig;

	/**
	 * Generate slug name
	 * @param name
	 * @return {@link String}
	 */
	protected String generateSlug(String name) {
		if(StringUtils.isBlank(name)) return "NOSULG";
		name = name.trim().toLowerCase();
		name = name.replace(" ", "-");
		return name;
	}

	protected String getBusinessId() {
		Zbusiness z = sessionManager.getZbusiness();
		if(Boolean.TRUE.equals(z.getCentral())) return z.getZid();
		return z.getCentralzid();
	}

	protected String getAuditUser() {
		return sessionManager.getLoggedInUserDetails().getUsername();
	}

	public List<Arhed> getAllAdars() {
		// TODO Auto-generated method stub
		return null;
	}
}
