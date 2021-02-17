package com.asl.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.asl.service.ASLSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public abstract class AbstractGenericService {

	protected static final String ERROR = "Error is {}, {}";
	//@PersistenceContext protected EntityManager em;
	//@Autowired protected JdbcTemplate jdbcTemplate;
	@Autowired protected ASLSessionManager sessionManager;

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
}
