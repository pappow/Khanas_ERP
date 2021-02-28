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
 * @since Feb 24, 2021
 */
@Data
@Entity
@Table(name = "caitem")
@IdClass(CaitemPK.class)
@EqualsAndHashCode(of = { "zid","xitem" }, callSuper = false)
public class Caitem extends AbstractModel<String> {

	private static final long serialVersionUID = 304110246928300496L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xunitpur")
	private String xunitpur;

	@Column(name = "xcfpur")
	private BigDecimal xcfpur;

	@Column(name = "xunitsel")
	private String xunitsel;

	@Column(name = "xcfsel")
	private BigDecimal xcfsel;

	@Column(name = "xcodeold")
	private String xcodeold;

	@Column(name = "xcost")
	private BigDecimal xcost;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xdealerp")
	private BigDecimal xdealerp;

	@Column(name = "xminqty")
	private BigDecimal xminqty;

	@Column(name = "xmrp")
	private BigDecimal xmrp;

	@Column(name = "xcus")
	private String xcus;

	@Column(name = "xlong")
	private String xlong;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xmodel")
	private String xmodel;
}
