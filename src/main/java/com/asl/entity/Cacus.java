package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Feb 27, 2021
 */
@Data
@Entity
@Table(name = "xusers")
@IdClass(CacusPK.class)
@EqualsAndHashCode(of = { "zid","xcus" }, callSuper = false)
public class Cacus extends AbstractModel<String> {

	private static final long serialVersionUID = -8448108466106389501L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xorg")
	private String xorg; 

	@Column(name = "xmadd")
	private String xmadd;

	@Column(name = "xwh")
	private String xwh;

	@Column(name = "xcontact")
	private String xcontact;

	@Column(name = "xphone")
	private String xphone;

	@Column(name = "xgcus")
	private String xgcus;

	@Column(name = "xgsup")
	private String xgsup;

	@Column(name = "xvatregno")
	private String xvatregno;

	@Column(name = "xstatuscus")
	private String xstatuscus;

	@Column(name = "xcrlimit")
	private BigDecimal xcrlimit;

	@Column(name = "xtype")
	private String xtype;
}
