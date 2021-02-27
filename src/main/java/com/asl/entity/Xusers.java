package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Data
@Entity
@Table(name = "xusers")
@IdClass(XusersPK.class)
@EqualsAndHashCode(of = { "zid","zemail" }, callSuper = false)
public class Xusers extends AbstractModel<String> {

	private static final long serialVersionUID = -6408600048875043467L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xpassword")
	private String xpassword;

	@Column(name = "xrole")
	private String xrole;

	@Column(name = "xaccess")
	private String xaccess;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xsp")
	private String xsp;

	@Column(name = "xdformat")
	private String xdformat;

	@Column(name = "xdsep")
	private String xdsep;

	@Column(name = "xlastlogdate")
	private String xlastlogdate;

	@Column(name = "xname")
	private String xname;

	@Column(name = "xoldpass")
	private String xoldpass;

	@Column(name = "xposition")
	private String xposition;

	@Column(name = "xpriority")
	private String xpriority;
}
