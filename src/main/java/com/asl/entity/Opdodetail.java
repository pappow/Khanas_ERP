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

@Data
@Entity
@Table(name = "opdodetail")
@IdClass(OpdodetailPK.class)
@EqualsAndHashCode(of = { "zid","xdornum","xrow" }, callSuper = false)
public class Opdodetail  extends AbstractModel<String>{
	
	private static final long serialVersionUID = -2050527945264418934L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private String zid;
	
	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private String xdornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private int xrow;
	
	@Column(name = "xunitsel")
	private String xunitsel;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqtyord")
	private BigDecimal xqtyord;
	
	@Column(name = "xrate")
	private BigDecimal xrate;
	
	@Column(name = "xlineamt")
	private BigDecimal xlineamt;
	
	@Column(name = "xcomtype")
	private String xcomtype;
	
	@Column(name = "xdorrow")
	private int xdorrow;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xrategrn")
	private BigDecimal xrategrn;
	
	@Column(name = "xqtycrn")
	private BigDecimal xqtycrn;
}
