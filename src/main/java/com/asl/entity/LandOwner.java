package com.asl.entity;

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
@Table(name = "landowner")
@IdClass(LandOwnerPK.class)
@EqualsAndHashCode(of = { "zid", "xland", "xperson" }, callSuper = false)
public class LandOwner extends AbstractModel<String> {

	private static final long serialVersionUID = 8511409160374470441L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xland")
	private String xland;

	@Id
	@Basic(optional = false)
	@Column(name = "xperson")
	private String xperson;

	@Column(name = "xtype")
	private String xtype;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xqty")
	private Integer xqty;

	@Column(name = "xunit")
	private String xunit;

	@Transient
	private boolean newData;
}
