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
@Table(name = "opcrnheader")
@IdClass(OpcrnheaderPK.class)
@EqualsAndHashCode(of = { "zid","xcrnnum" }, callSuper = false)
public class Opcrnheader extends AbstractModel<String> {
	
	private static final long serialVersionUID = 688623128335682517L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private String xcrnnum;
	
	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;
	
	@Column(name = "xcus")
	private String xcus;
	
	@Column(name = "xstatuscrn")
	private String xstatuscrn;
	
	@Column(name = "xcur")
	private String xcur;
	
	@Column(name = "xexch")
	private String xexch;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xtotamt")
	private BigDecimal xtotamt;
	
	@Column(name = "xvatamt")
	private BigDecimal xvatamt;
	
	@Column(name = "xaitamt")
	private BigDecimal xaitamt;
	
	@Column(name = "xdiscamt")
	private BigDecimal xdiscamt;
	
	@Column(name = "xvatait")
	private String xvatait;
	
	@Column(name = "xordernum")
	private String xordernum;
	
	@Column(name = "xstatusar")
	private String xstatusar;
	
	@Column(name = "xamount")
	private BigDecimal xamount;
	
	@Column(name = "xvatrate")
	private BigDecimal xvatrate;
	
	@Column(name = "xdisc")
	private String xdisc;
	
	@Column(name = "xdiscf")
	private String xdescf;
	
	@Column(name = "xpaymenttype")
	private String xpaymenttype;
	
	@Column(name = "xstatusjv")
	private String xstatusjv;
	
	@Column(name = "xvoucher")
	private String xvoucher;
	
	@Column(name = "xwh")
	private String xwh;
	
	@Column(name ="xnote")
	private String xnote;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xtypetrn")
	private String xtypetrn;
	
	@Column(name = "xtrn")
	private String xtrn;
	
	@Column(name = "xdornum")
	private String xdornum;
	
	@Column(name = "xdocnum")
	private String xdocnum;
	
	@Column(name = "xtrncrn")
	private String xtrncrn;
	
	@Column(name = "xgrandtot")
	private BigDecimal xgrandtot;
	

}
