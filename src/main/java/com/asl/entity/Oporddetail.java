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

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Data
@Entity
@Table(name = "oporddetail")
@IdClass(OporddetailPK.class)
@EqualsAndHashCode(of = { "zid", "xordernum", "xrow" }, callSuper = false)
public class Oporddetail extends AbstractModel<String> {

	private static final long serialVersionUID = -8200619366025104933L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private String xordernum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xitem")
	private String xitem;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xcomtype")
	private String  xcomtype;

	@Column(name = "xqtydel")
	private BigDecimal xqtydel;

	@Column(name = "xlong")
	private String xlong;

	@Transient
	private String xdesc;
	@Transient
	private String xgitem;
	@Transient
	private String xcatitem;
}
