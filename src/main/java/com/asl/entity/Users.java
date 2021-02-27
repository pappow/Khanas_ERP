package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.asl.enums.UserRole;
import com.asl.util.BooleanToYesNo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Administrator
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = { "username" }, callSuper = false)
public class Users extends AbstractModel<String> {

	private static final long serialVersionUID = -2612771101344210891L;

	@Column(name = "zid", nullable = false)
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "username")
	private String username;

	@Column(name = "pswd")
	private String pswd;

	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;

	@Column(name = "systemAdmin", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean systemAdmin;

	@Column(name = "admin", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean admin;

	@Column(name = "appUser", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean appUser;

	@Column(name = "driver", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean driver;

	@Column(name = "subscriber", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean subscriber;

	@Column(name = "role1", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role1;

	@Column(name = "role2", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role2;

	@Column(name = "role3", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role3;

	@Column(name = "role4", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role4;

	@Column(name = "role5", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role5;

	@Column(name = "role6", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role6;

	@Column(name = "role7", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role7;

	@Column(name = "role8", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role8;

	@Column(name = "role9", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role9;

	@Column(name = "role10", length = 1)
	@Convert(converter = BooleanToYesNo.class)
	private Boolean role10;

	@Transient
	private String roles;

	public String getRoles() {
		this.roles = "";
		if(Boolean.TRUE.equals(systemAdmin)) roles += UserRole.SYSTEM_ADMIN.getCode() + ',';
		if(Boolean.TRUE.equals(admin)) roles += UserRole.ADMIN.getCode() + ',';
		if(Boolean.TRUE.equals(appUser)) roles += UserRole.APPUSER.getCode() + ',';
		if(Boolean.TRUE.equals(driver)) roles += UserRole.DRIVER.getCode() + ',';
		if(Boolean.TRUE.equals(subscriber)) roles += UserRole.SUBSCRIBER.getCode() + ',';

		if(Boolean.TRUE.equals(role1)) roles += UserRole.ROLE1.getCode() + ',';
		if(Boolean.TRUE.equals(role2)) roles += UserRole.ROLE2.getCode() + ',';
		if(Boolean.TRUE.equals(role3)) roles += UserRole.ROLE3.getCode() + ',';
		if(Boolean.TRUE.equals(role4)) roles += UserRole.ROLE4.getCode() + ',';
		if(Boolean.TRUE.equals(role5)) roles += UserRole.ROLE5.getCode() + ',';
		if(Boolean.TRUE.equals(role6)) roles += UserRole.ROLE6.getCode() + ',';
		if(Boolean.TRUE.equals(role7)) roles += UserRole.ROLE7.getCode() + ',';
		if(Boolean.TRUE.equals(role8)) roles += UserRole.ROLE8.getCode() + ',';
		if(Boolean.TRUE.equals(role9)) roles += UserRole.ROLE9.getCode() + ',';
		if(Boolean.TRUE.equals(role10)) roles += UserRole.ROLE10.getCode() + ',';

		if(StringUtils.isBlank(roles)) return roles;

		int lastComma = roles.lastIndexOf(',');
		String finalString = roles.substring(0, lastComma);
		roles = finalString;
		return roles;
	}
}
