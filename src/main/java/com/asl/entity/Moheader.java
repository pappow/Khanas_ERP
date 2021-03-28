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
@Table(name = "moheader")
@IdClass(MoheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xbatch" }, callSuper = false)
public class Moheader extends AbstractModel<String> {

	private static final long serialVersionUID = -8270354822047219816L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbatch")
	private String xbatch;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xbomkey")
	private String xbomkey;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xqtyprd")
	private BigDecimal xqtyprd;

	@Column(name = "xqtycom")
	private BigDecimal xqtycom;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xsup")
	private String xsup;

	@Column(name = "xstatusmor")
	private String xstatusmor;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xordernum")
	private String xordernum;

	@Column(name ="xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xchalan")
	private String xchalan;

	@Column(name = "bomexploaded")
	private boolean bomexploaded;

	@Column(name = "xdesc")
	private String xdesc;

	@Transient
	private String Xqtyprdunit;
	
}
