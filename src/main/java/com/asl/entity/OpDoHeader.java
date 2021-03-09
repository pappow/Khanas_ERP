package com.asl.entity;

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
@Table(name = "opdoheader")
@IdClass(OpDoHeaderPK.class)
@EqualsAndHashCode(of = { "zid","xdornum" }, callSuper = false)
public class OpDoHeader extends AbstractModel<String> {/**
	 * 
	 */
	private static final long serialVersionUID = -3094969631197911920L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private String xdornum;
	
	@Column(name = "xemail")
	private String xemail;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xcus")
	private String xcus;
	
	@Column(name = "xtotamt")
	private int xtotamt;
	
	@Column(name = "xvatamt")
	private int xvatamt;
	
	@Column(name = "xperson")
	private String xperson;
	
	@Column(name = "xpaymenttype")
	private String xpaymenttype;
	
	@Column(name = "xdiscamt")
	private int xdiscamt;
	
	@Column(name = "xordernum")
	private String xordernum;
	
	@Column(name = "xstatusord")
	private String xstatusord;
	
	@Column(name = "xstatusjv")
	private String xstatusjv;
	
	@Column(name = "xpaid")
	private int xpaid;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Column(name = "xchange")
	private int xchange;
	
	@Column(name = "xpaystatus")
	private String xpaystatus;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xstatusar")
	private String xstatusar;
	
	@Column(name = "xstaff")
	private String xstaff;
	
	@Column(name = "xwh")
	private String xwh;
	
	@Column(name = "xdatecom")
	@Temporal(TemporalType.DATE)
	private Date xdatecom;
	
	@Column(name = "xdatedue")
	@Temporal(TemporalType.DATE)
	private Date xdatedue;
	
	@Column(name = "xcur")
	private String xcur;
	
	@Column(name = "xexch")
	private int xexch;
	
	@Column(name = "xvoucher")
	private String xvoucher;
	
}
