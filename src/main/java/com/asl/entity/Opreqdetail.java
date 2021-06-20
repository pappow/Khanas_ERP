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
 * @since Jun 20, 2021
 */
@Data
@Entity
@Table(name = "opreqdetail")
@IdClass(OpreqdetailPK.class)
@EqualsAndHashCode(of = {"zid", "xdoreqnum", "xrow"}, callSuper = true)
public class Opreqdetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1335364690831742660L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdoreqnum")
	private String xdoreqnum;

	@Id
	@Basic(optional = false)
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

	@Column(name = "xitemdesc")
	private String xitemdesc;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xgitem")
	private String xgitem;
}
