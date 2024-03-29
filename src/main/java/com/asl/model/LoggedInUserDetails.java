package com.asl.model;

import java.util.ArrayList;
import java.util.List;

import com.asl.entity.UserAuditRecord;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Data
public class LoggedInUserDetails {

	private String username;
	private String emailAddress;
	private String businessId;
	private List<String> roles = new ArrayList<>();
	private String ipAddress;

	public UserAuditRecord getAuditRecord(LoggedInUserDetails liud) {
		UserAuditRecord uar = new UserAuditRecord();
		uar.setUserId(liud.getUsername());
		uar.setBusinessId(liud.getBusinessId());
		uar.setIpAddress(liud.getIpAddress());
		return uar;
	}
}
