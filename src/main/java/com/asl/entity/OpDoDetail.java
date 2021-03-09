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
@Table(name = "opdodetail")
@IdClass(OpDoDetailPK.class)
@EqualsAndHashCode(of = { "zid","xrow","xdornum" }, callSuper = false)
public class OpDoDetail  extends AbstractModel<String>{/**
	 * 
	 */
	private static final long serialVersionUID = -2050527945264418934L;

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
	@Column(name = "xdornum")
	private String xdornum;
	
	@Column(name = "zemail")
	private String zemail;
	
	@Column(name = "xunitsel")
	private String xunitsel;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqtyord")
	private int xqtyord;
	
	@Column(name = "xrate")
	private int xrate;
	
	@Column(name = "xcomtype")
	private String xcomtype;
	
	@Column(name = "xdorrow")
	private int xdorrow;
	
	@Column(name = "xlong")
	private String xlong;
	
	@Column(name = "xrategrn")
	private int xrategrn;
	
	@Column(name = "xqtycrn")
	private int xqtycrn;
}
