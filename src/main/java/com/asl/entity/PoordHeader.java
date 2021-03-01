package com.asl.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "poordheader")
@IdClass(PoordHeaderPK.class)
@EqualsAndHashCode(of = { "zid", "xpornum" }, callSuper = false)
public class PoordHeader extends AbstractModel<String> {

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
	private String xdate;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xtotamt")
	private String xtotamt;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xstatuspor")
	private String xstatuspor;

	@Column(name = "xnote")
	private String xnote;
}
