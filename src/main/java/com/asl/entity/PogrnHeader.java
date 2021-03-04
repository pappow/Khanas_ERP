package com.asl.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "pogrnheader")
@IdClass(PogrnHeaderPK.class)
@EqualsAndHashCode(of = { "zid", "xgrnnum" }, callSuper = false)
public class PogrnHeader extends AbstractModel<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3246354454094927618L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xgrnnum")
	private String xgrnnum;

	@Column(name = "xdate")
	private Date xdate;
	
	@Column(name = "xcus")
	private String xcus;
	
	@Column(name = "xtype")
	private String xtype;
	
	@Column(name = "xwh")
	private String xwh;
	
	@Column(name = "xstatusgrn")
	private String xstatusgrn;
	
	@Column(name = "xinvnum")
	private String xinvnum;
	
	@Column(name = "xpcnum")
	private String xpcnum;
	
	@Column(name = "xstatusap")
	private String xstatusap;
	
	@Column(name = "xref")
	private String xref;
	
	@Column(name = "xtotamt")
	private BigDecimal xtotamt;
	
	@Column(name = "xpornum")
	private String xpornum;
	
	@Column(name = "xstatusjv")
	private String xstatusjv;
	
	@Column(name = "xstatuscrn")
	private String xstatuscrn;
	
	@Column(name = "xvoucher")
	private String xvoucher;
	
	@Column(name = "xnote")
	private String xnote;
	
	@Transient
	private String xtrngrn;
	
}
