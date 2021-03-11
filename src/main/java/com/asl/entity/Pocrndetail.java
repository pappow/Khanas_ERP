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
@Table(name = "pocrndetail")
@IdClass(PocrndetailPK.class)
@EqualsAndHashCode(of = { "zid","xrow", "xcrnnum" }, callSuper = false)
public class Pocrndetail extends AbstractModel<String>{
	
	private static final long serialVersionUID = -3661015633860005572L;
	
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
	@Column(name = "xcrnnum")
	private String xcrnnum;
	
	@Column(name = "zemail")
	private String zemail;
	
	@Column(name = "xunit")
	private String xunit;
	
	@Column(name = "xitem")
	private String xitem;
	
	@Column(name = "xqtyord")
	private int xqtyord;
	
	@Column(name = "xqtygrn")
	private int xqtygrn;
	
	@Column(name = "xrate")
	private int xrate;
	
	@Column(name = "xdocrow")
	private int xdocrow;
	
	@Column(name = "xlong")
	private String xlong;

}
