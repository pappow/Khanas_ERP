package com.asl.entity;

import java.math.BigDecimal;

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
@Table(name = "poorddetail")
@IdClass(PoordDetailPK.class)
@EqualsAndHashCode(of = { "zid", "xpornum", "xrow" }, callSuper = false)
public class PoordDetail extends AbstractModel<String> {

	private static final long serialVersionUID = 4124498529281042988L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private String xpornum;

	@Id
	@Column(name = "xrow")
	private int xrow;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xunitpur")
	private String xunitpur;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xqtygrn")
	private BigDecimal xqtygrn;

	@Column(name = "xcfpur")
	private BigDecimal xcfpur;

	@Column(name = "xqtypur")
	private BigDecimal xqtypur;

	@Transient
	private String xitemdesc;

}
