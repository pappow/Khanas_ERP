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
@Table(name = "modetail")
@IdClass(ModetailPK.class)
@EqualsAndHashCode(of = { "zid", "xrow", "xbatch" }, callSuper = false)
public class Modetail extends AbstractModel<String> {

	private static final long serialVersionUID = -4998991037047476853L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;

	@Id
	@Basic(optional = false)
	@Column(name = "xbatch")
	private String xbatch;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xstype")
	private String xstype;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtyreq")
	private int xqtyreq;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xtype")
	private String xtype;
}
