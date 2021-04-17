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
 * @since Mar 8, 2021
 */
@Data
@Entity
@Table(name = "opordheader")
@IdClass(OpordheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xordernum" }, callSuper = false)
public class Opordheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4164878691134692756L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private String xordernum;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xstatusord")
	private String xstatusord;

	@Column(name = "xstatus")
	private String xstatus;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xpornum")
	private String xpornum;

	@Column(name= "xtypetrn")
	private String xtypetrn;

	@Column(name = "xtrn")
	private String xtrn;

	@Column(name = "xchalanref")
	private String xchalanref;

	@Column(name = "batchcreated")
	private boolean batchcreated;

	@Column(name = "productioncompleted")
	private boolean productioncompleted;

	@Column(name = "invoicecreated")
	private boolean invoicecreated;

	@Transient
	private String branchname;
	
	@Column(name="xfunction")
	private String xfunction;

	@Column(name="xtotguest")
	private int xtotguest;

	@Column(name="xbookdate")
	private Date xboodate;

	@Column(name="xfuncdate")
	private Date xfuncdate;

	@Column(name="xstarttime")
	private String xstarttime;

	@Column(name="xendtime")
	private String xendtime;

	@Column(name="xfacamt")
	private BigDecimal xfacamt;

	@Column(name="xfoodamt")
	private BigDecimal xfoodamt;

	@Column(name="xhallamt")
	private BigDecimal xhallamt;
	
	@Column(name="xdiscamt")
	private BigDecimal xdiscamt;
	
	@Column(name = "xtotamt")
	private BigDecimal xtotamt;
	
	@Column(name = "xgrandtot")
	private BigDecimal xgrandtot;
}
