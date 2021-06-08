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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "landinfo")
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

	@Column(name="xblock")
	private String xblock;

	@Column(name="xroad")
	private String xroad;

	@Column(name="xcslandqty")
	private Integer xcslandqty;

	@Column(name="xcslandunit")
	private String xcslandunit;

	@Column(name="xrslandqty")
	private Integer xrslandqty;

	@Column(name="xrslandunit")
	private String xrslandunit;

	@Column(name="xcitylandqty")
	private Integer xcitylandqty;

	@Column(name="xcitylandunit")
	private String xcitylandunit;

	@Column(name="xlandgrsqty")
	private Integer xlandgrsqty;

	@Column(name="xlanggrsunit")
	private String xlanggrsunit;

	@Column(name="xlanddedroad")
	private BigDecimal xlanddedroad;

	@Column(name="xlanddedother")
	private BigDecimal xlanddedother;

	@Column(name="xlandnetqty")
	private Integer xlandnetqty;

	@Column(name="xlandnetunit")
	private String xlandnetunit;

	@Column(name="xriversideqty")
	private String xriversideqty;

	@Column(name="xriversideunit")
	private String xriversideunit;

	@Column(name="xnote")
	private String xnote;

	@Column(name="xlandparent")
	private String xlandparent;

	@Column(name="xstatus")
	private String xstatus;

	@Column(name="xdateborn")
	@Temporal(TemporalType.DATE)
	private Date xdateborn;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;
}
