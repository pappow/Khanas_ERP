package com.asl.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Data
@Entity
@Table(name = "oporddetail")
@IdClass(OporddetailPK.class)
@NoArgsConstructor
@AllArgsConstructor
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
	private int xrow;

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

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	public Oporddetail(String xitem, BigDecimal xqtyord) {
		this.xitem = xitem;
		this.xqtyord = xqtyord;
	}

}
