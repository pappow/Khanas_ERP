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

/**
 * @author Zubayer Ahamed
 * @since Jun 20, 2021
 */
@Data
@Entity
@Table(name = "opreqheader")
@IdClass(OpreqheaderPK.class)
@EqualsAndHashCode(of = {"zid", "xdoreqnum"}, callSuper = true)
public class Opreqheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4672975819056396659L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdoreqnum")
	private String xdoreqnum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xstatusreq")
	private String xstatusreq;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xamount")
	private BigDecimal xamount;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xsignreject")
	private String xsignreject;

	@Column(name = "xdatereject")
	private String xdatereject;

	@Column(name = "xreqtype")
	private String xreqtype;

	@Column(name = "xpreparer")
	private String xpreparer;

	@Column(name = "xpreparertime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xpreparertime;

	@Column(name = "xapreparer")
	private String xapreparer;

	@Column(name = "xapreparertime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xapreparertime;
	
	@Transient
	private String xorg;
	
	@Transient
	private String xdesc;
}
