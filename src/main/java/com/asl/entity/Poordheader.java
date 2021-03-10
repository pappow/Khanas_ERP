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
@Table(name = "poordheader")
@IdClass(PoordheaderPK.class)
@EqualsAndHashCode(of = { "zid", "xpornum" }, callSuper = false)
public class Poordheader extends AbstractModel<String> {

	private static final long serialVersionUID = -5079810075388406733L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private String xpornum;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xref")
	private String xref;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xstatuspor")
	private String xstatuspor;

	@Column(name = "xnote")
	private String xnote;

	@Transient
	private String xtrnpor;
}
