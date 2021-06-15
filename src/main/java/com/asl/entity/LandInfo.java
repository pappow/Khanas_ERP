package com.asl.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "calandinfo")
@IdClass(LandInfoPK.class)
@EqualsAndHashCode(of = { "zid", "xland" }, callSuper = false)
public class LandInfo extends AbstractModel<String>{

	
	private static final long serialVersionUID = 4352784604780796645L;
	
	@Id
	@Basic(optional=false)
	@Column(name="zid")
	private String zid;

	@Id
	@Basic(optional=false)
	@Column(name="xland")
	private String xland;

	@Column(name="xlname")
	private String xlname;
	
	@Column(name="xblock")
	private String xblock;

	@Column(name="xroad")
	private String xroad;
	
	@Column(name="xlandqty")
	private BigDecimal xlandqty;

	@Column(name="xlandunit")
	private String xlandunit;

	@Column(name="xlandgrsqty")
	private BigDecimal xlandgrsqty;

	@Column(name="xlanggrsunit")
	private String xlanggrsunit;

	@Column(name="xlanddedroad")
	private Integer xlanddedroad;

	@Column(name="xlanddedother")
	private Integer xlanddedother;

	@Column(name="xlandnetqty")
	private BigDecimal xlandnetqty;

	@Column(name="xlandnetunit")
	private String xlandnetunit;

	@Column(name="xriversideqty")
	private BigDecimal xriversideqty;

	@Column(name="xriversideunit")
	private String xriversideunit;

	@Column(name="xnote")
	private String xnote;

	@Column(name="xlandparent")
	private String xlandparent;

	@Column(name="xstatus")
	private String xstatus;
	
	@Column(name="xroadred")
	private BigDecimal xroadred;

	@Column(name="xotherred")
	private BigDecimal xotherred;

	@Column(name="xdateborn")
	@Temporal(TemporalType.DATE)
	private Date xdateborn;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
	
	@Transient
	private String xname;
}
